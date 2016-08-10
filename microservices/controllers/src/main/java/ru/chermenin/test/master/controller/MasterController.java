package ru.chermenin.test.master.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.chermenin.test.common.dto.Failed;
import ru.chermenin.test.common.dto.Project;
import ru.chermenin.test.common.service.Repository;
import ru.chermenin.test.master.service.ReplicationService;

import java.util.List;

/**
 * Master REST controller.
 *
 * @author chermenin
 */
@RestController
public class MasterController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Repository repository;

    private final ReplicationService replicationService;

    @Autowired
    public MasterController(Repository repository, ReplicationService replicationService) {
        this.repository = repository;
        this.replicationService = replicationService;
    }

    @RequestMapping("/projects")
    public List<Project> getProjects() {
        return repository.getProjects();
    }

    @RequestMapping("/failed")
    public List<Failed> getFailed() {
        return repository.getFailedProjects();
    }

    @RequestMapping(value = "/projects/{id}/data/{data}", method = RequestMethod.POST)
    public Project postProject(@PathVariable int id, @PathVariable String data) {
        Project project = new Project(id, 1, data);
        repository.updateProject(project);
        repository.removeFailed(id);
        replicationService.sendProject(repository.getProject(id));
        logger.info(String.format("Updated project [%s]", project));
        return project;
    }
}
