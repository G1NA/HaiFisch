package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.CheckInRequest;

import java.util.HashMap;

public class Client {

    private final String clientAddress;
    private final int clientPort;
    private final String clientReqId;
    private final int mappersAssigned;
    private int mappersDone;
    private final HashMap<String, CheckInRequest> assignments;

    public Client(String clientAddress, int port, String clientReqId, int mappersAssigned,
                  HashMap<String, CheckInRequest> assignments) {
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

    public void addAssignment(String key, CheckInRequest value) {
        assignments.put(key, value);
    }

    //servername = ip+":"+port
    public void markDone(String serverName) {
        mappersDone++;
        assignments.remove(serverName);
    }

    public CheckInRequest getAssignment(String serverName) {
        return assignments.get(serverName);
    }

    public CheckInRequest removeAssignment(String serverName) {
        return assignments.remove(serverName);
    }

    public boolean isDone() {
        return mappersAssigned == mappersDone;
    }
}
