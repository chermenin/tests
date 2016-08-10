package ru.chermenin.test.common.dto;

/**
 * Project pojo.
 *
 * @author chermenin
 */
public class Project {

    /**
     * Project ID.
     */
    private int id;

    /**
     * Project version.
     */
    private int version;

    /**
     * Project metadata.
     */
    private String data;

    /**
     * Default constructor.
     */
    public Project() {
    }

    public Project(int id, int version, String data) {
        this.id = id;
        this.version = version;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("Project { id=%d, version=%d, data='%s' }", id, version, data);
    }
}
