package ru.chermenin.test.common.service;

import ru.chermenin.test.common.dto.Failed;
import ru.chermenin.test.common.dto.Project;

import java.util.List;

/**
 * Repository service interface.
 *
 * @author chermenin
 */
public interface Repository {

    /**
     * Get all projects.
     *
     * @return List of projects.
     */
    List<Project> getProjects();

    /**
     * Get project by id.
     *
     * @param id Project ID.
     * @return Founded project or null if not exist.
     */
    Project getProject(int id);

    /**
     * Put project to list with replace if exist.
     *
     * @param project Project pojo to update.
     */
    void putProject(Project project);

    /**
     * Update project.
     *
     * @param project Project pojo to update.
     */
    void updateProject(Project project);

    /**
     * Get all failed projects.
     *
     * @return List of failed projects.
     */
    List<Failed> getFailedProjects();

    /**
     * Remove failed project from list.
     *
     * @param id Project ID to remove.
     */
    void removeFailed(int id);

    /**
     * Remove failed project from list.
     *
     * @param id Project ID to remove.
     * @param port Slave port of failed project.
     */
    void removeFailed(int id, int port);

    /**
     * Add failed project to list.
     *
     * @param project Failed project.
     */
    void addFailed(Failed project);
}
