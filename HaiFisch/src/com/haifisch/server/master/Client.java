package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.CheckInRequest;

import java.util.HashMap;

class Client {

    private final String clientAddress;
    private final int clientPort;
    private final String clientReqId;
    private final int mappersAssigned;
    private int mappersDone;
    private final HashMap<String, CheckInRequest> assignments;

    /**
     * The client object represents a client currently being served
     * as well as the details concerning his request
     * Constructor
     * @param clientAddress The client address
     * @param port The client port
     * @param clientReqId The assigned request id
     * @param mappersAssigned The number of mappers assigned to the client
     * @param assignments The Map with mappers and their assignments concerning the client
     */
    Client(String clientAddress, int port, String clientReqId, int mappersAssigned,
           HashMap<String, CheckInRequest> assignments) {
        this.clientAddress = clientAddress;
        this.clientPort = port;
        this.clientReqId = clientReqId;
        this.mappersAssigned = mappersAssigned;
        this.mappersDone = 0;
        this.assignments = assignments;
    }

    /**  GETTERS */
    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getClientReqId() {
        return clientReqId;
    }

    /**
     * Add a new assignment
     * @param key The mapper name as mapperIP:mapperPort
     * @param value The assignment sent to the mapper
     */
    void addAssignment(String key, CheckInRequest value) {
        assignments.put(key, value);
    }

    /**
     * Mark a certain mapper done.
     * @param serverName The mapper name as mapperIP:mapperPort
     */
    void markDone(String serverName) {
        mappersDone++;
        assignments.remove(serverName);
    }

    /**
     * Assignment getter
     * @param serverName The mapper name as mapperIP:mapperPort
     * @return the CheckInRequest
     */
    public CheckInRequest getAssignment(String serverName) {
        return assignments.get(serverName);
    }

    /**
     * Remove an assignment
     * @param serverName The mapper name as mapperIP:mapperPort
     * @return The assignment removed
     */
    public CheckInRequest removeAssignment(String serverName) {
        return assignments.remove(serverName);
    }

    /**
     * Check if the client is ready to be served the results
     * @return true if true
     */
    boolean isDone() {
        return mappersAssigned == mappersDone;
    }
}
