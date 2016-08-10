package ru.chermenin.test.common.test;

import org.junit.Test;
import ru.chermenin.test.common.dto.Project;
import ru.chermenin.test.common.service.Repository;
import ru.chermenin.test.common.service.impl.RepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Tests to check multithreading work of repository implementation.
 *
 * @author chermenin
 */
public class RepositoryTest {

    private Repository repository = new RepositoryImpl();

    private final static ExecutorService executor = Executors.newFixedThreadPool(1_000);

    @Test
    public void testUpdateProjects() {
        Project project = new Project(0, 1, "value");
        List<Future<?>> futures  = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            futures.add(executor.submit(() -> repository.updateProject(project)));
        }
        while (!futures.stream().allMatch(Future::isDone));
        int version = repository.getProject(0).getVersion();
        assert version == 100_000 : "Bad version = " + version;
    }
}
