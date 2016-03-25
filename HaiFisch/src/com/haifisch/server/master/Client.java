package com.haifisch.server.master;

import java.util.HashMap;

public class Client {

    private final String clientAddress;
    private final int clientPort;
    private final String clientReqId;
    private final int mappersAssigned;
    private int mappersDone;
    private final HashMap<String, Assignment> assignments;

    public Client(String clientAddress, int port, String clientReqId, int mappersAssigned,
                  HashMap<String, Assignment> assignments) {
        this.clientAddress = clientAddress;
        this.clientPort = port;
        this.clientReqId = clientReqId;
        this.mappersAssigned = mappersAssigned;
        this.mappersDone = 0;
        this.assignments = assignments;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getClientReqId() {
        return clientReqId;
    }

    public void addAssignment(String key, Assignment value) {
        assignments.put(key, value);
    }

    public void markDone(String serverName) {
        mappersDone++;
        assignments.remove(serverName);
    }

    public Assignment getAssignment(String serverName) {
        return assignments.get(serverName);
    }

    public Assignment removeAssignment(String serverName) {
        return assignments.remove(serverName);
    }

}
