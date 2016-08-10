package ru.chermenin.test.master.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import ru.chermenin.test.common.dto.Failed;
import ru.chermenin.test.common.dto.Project;
import ru.chermenin.test.common.service.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Replication service.
 *
 * @author chermenin
 */
@Service
public class ReplicationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String URL_TEMPLATE = "http://localhost:%d/projects/%d";

    private final static String SLAVE_PORTS_ARG = "slavePorts";

    /**
     * Data repository.
     */
    private final Repository repository;

    /**
     * Slave client ports list.
     */
    private final int[] ports;

    /**
     * Executor service to parallel send requests.
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    @Autowired
    public ReplicationService(Repository repository, ApplicationArguments args) {
        this.repository = repository;
        if (args.containsOption(SLAVE_PORTS_ARG)) {
            List<String> ports = args.getOptionValues(SLAVE_PORTS_ARG);
            this.ports = ports.stream()
                    .flatMap(s -> Stream.of(s.split(",")))
                    .mapToInt(Integer::parseInt).toArray();
        } else {
            this.ports = new int[0];
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void sendFailed() {
        final long currentTime = System.currentTimeMillis();
        repository.getFailedProjects().stream()
                .filter(p -> p.getLastTry() + p.getDelay() < currentTime)
                .filter(p -> !p.isSended())
                .forEach(p -> {
                    p.setSended(true);
                    executor.submit(() -> sendProject(p.getSlavePort(), p));
                });
    }

    public void sendProject(Project project) {
        IntStream.of(ports).forEach(port -> executor.submit(() -> sendProject(port, project)));
        logger.info(String.format("Send project [%s]", project));
        logger.info(String.format("Destinations ports %s", Arrays.toString(ports)));
    }

    private void sendProject(int port, Project project) {
        try {
            String url = String.format(URL_TEMPLATE, port, project.getId());

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Map object to JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(project);

            // Unite header with JSON to entity
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            // Send request
            RestTemplate rest = new RestTemplate();
            ResponseEntity<Project> response = null;
            try {
                response = rest.exchange(url, HttpMethod.POST, entity, Project.class);
            } catch (HttpStatusCodeException serverException) {
                response = new ResponseEntity<>(serverException.getStatusCode());
                logger.error("Request failed with code " + serverException.getStatusCode().value(), serverException);
            } catch (Exception e) {
                logger.error("REST error", e);
            }

            // Add project to failed list if response status is not successful
            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.info("Project was sent successfully");
                if (project instanceof Failed) {
                    repository.removeFailed(project.getId(), port);
                }
            } else {
                Failed failed = new Failed(
                        project.getId(),
                        project.getVersion(),
                        project.getData(),
                        port,
                        response != null ? response.getStatusCodeValue() : -1
                );
                repository.addFailed(failed);
                logger.info("Project sending failed");
            }
        } catch (JsonProcessingException e) {
            logger.error("Processing error", e);
        }
    }
}
