package ru.chermenin.test.slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.chermenin.test.common.dto.Project;
import ru.chermenin.test.common.service.Repository;

import java.util.List;

/**
 * Slave REST controller.
 *
 * @author chermenin
 */
@RestController
public class SlaveController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Repository repository;

    @Autowired
    public SlaveController(Repository repository) {
        this.repository = repository;
    }

    @RequestMapping("/projects")
    public List<Project> getProjects() {
        return repository.getProjects();
    }

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.POST)
    public Project postProject(@PathVariable int id, @RequestBody Project project) throws Exception {
        Thread.sleep((long) (Math.random() * 10000));
        if (Math.random() > 0.2) throw new Exception();
        project.setId(id);
        repository.putProject(project);
        logger.info(String.format("Received project [%s]", project));
        return project;
    }
}
