package ru.chermenin.test.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Failed projects pojo.
 *
 * @author chermenin
 */
@JsonPropertyOrder({"slave_port", "response_status", "project_id", "version", "data"})
public class Failed extends Project {

    /**
     * Slave port.
     */
    @JsonProperty("slave_port")
    private int slavePort;

    /**
     * Slave response status.
     */
    @JsonProperty("response_status")
    private int responseStatus;

    /**
     * Timestamp of last send try.
     */
    @JsonIgnore
    private long lastTry = System.currentTimeMillis();

    /**
     * Delay to next try in milliseconds.
     */
    @JsonIgnore
    private long delay = 1000;

    /**
     * Sended flag.
     */
    @JsonIgnore
    private boolean sended = false;

    /**
     * Default constructor.
     */
    public Failed() {
    }

    public Failed(int id, int version, String data, int slavePort, int responseStatus) {
        super(id, version, data);
        this.slavePort = slavePort;
        this.responseStatus = responseStatus;
    }

    @Override
    @JsonProperty("project_id")
    public int getId() {
        return super.getId();
    }

    public int getSlavePort() {
        return slavePort;
    }

    public void setSlavePort(int slavePort) {
        this.slavePort = slavePort;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getLastTry() {
        return lastTry;
    }

    public void setLastTry(long lastTry) {
        this.lastTry = lastTry;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
    }

    @Override
    public String toString() {
        return String.format("Failed Project { slavePort=%d, responseStatus=%d, lastTry=%d }", slavePort, responseStatus, lastTry);
    }
}
