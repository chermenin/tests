package ru.chermenin.test.common.service.impl;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import ru.chermenin.test.common.dto.Failed;
import ru.chermenin.test.common.dto.Project;
import ru.chermenin.test.common.service.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository service implementation.
 *
 * @author chermenin
 */
@Service
public class RepositoryImpl implements Repository {

    /**
     * General projects map.
     */
    private final Map<Integer, Project> projects = new HashMap<>();

    /**
     * Failed projects map.
     */
    private final Map<Pair<Integer, Integer>, Failed> failed = new HashMap<>();

    @Override
    public List<Project> getProjects() {
        return new ArrayList<>(projects.values());
    }

    @Override
    public Project getProject(int id) {
        return projects.get(id);
    }

    @Override
    public void putProject(Project project) {
        synchronized (projects) {
            projects.put(project.getId(), project);
        }
    }

    @Override
    public void updateProject(Project project) {
        synchronized (projects) {
            Project previousProject = projects.get(project.getId());
            if (previousProject == null) {
                projects.put(project.getId(), project);
            } else {
                previousProject.setVersion(previousProject.getVersion() + 1);
                previousProject.setData(project.getData());
                projects.put(previousProject.getId(), previousProject);
            }
        }
    }

    @Override
    public List<Failed> getFailedProjects() {
        synchronized (failed) {
            return new ArrayList<>(failed.values());
        }
    }

    @Override
    public void removeFailed(int id) {
        synchronized (failed) {
            List<Pair<Integer, Integer>> keys =
                    failed.keySet().stream()
                            .filter(p -> p.getKey().equals(id))
                            .collect(Collectors.toList());
            keys.forEach(failed::remove);
        }
    }

    @Override
    public void removeFailed(int id, int port) {
        synchronized (failed) {
            failed.remove(new Pair<>(id, port));
        }
    }

    @Override
    public void addFailed(Failed project) {
        synchronized (failed) {
            Pair<Integer, Integer> key = new Pair<>(project.getId(), project.getSlavePort());
            Failed previousFailed = this.failed.get(key);
            if (previousFailed == null) {
                failed.put(key, project);
            } else {
                previousFailed.setLastTry(System.currentTimeMillis());
                previousFailed.setDelay((long) (previousFailed.getDelay() * 1.3));
                previousFailed.setSended(false);
                failed.put(key, previousFailed);
            }
        }
    }
}
