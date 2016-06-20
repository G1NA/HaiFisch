package com.haifisch.server.master_node;

import commons.CheckInRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class Client {

    private final String clientAddress;
    private final int clientPort;
    private final String clientReqId;
    private final int checkInsAssigned;
    private int checkInsDone;
    private final HashMap<CheckInRequest, String> assignments;
    private HashMap<CheckInRequest, Integer> fails = new HashMap<>();
    private HashSet<CheckInRequest> reassigned = new HashSet<>();

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
           HashMap<CheckInRequest,String> assignments) {
        this.clientAddress = clientAddress;
        this.clientPort = port;
        this.clientReqId = clientReqId;
        this.checkInsAssigned = mappersAssigned;
        this.checkInsDone = 0;
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
    void addAssignment(CheckInRequest key, String mapper) {
        assignments.put(key, mapper);
    }

    /**
     * Mark a certain mapper done.
     * @param serverName The mapper name as mapperIP:mapperPort
     */
    void markDone(CheckInRequest req) {
        checkInsDone++;
        assignments.get(req);
    }

    /**
     * gets all the assignments of a specific  mapper
     * @param serverName The mapper name as mapperIP:mapperPort
     * @return the CheckInRequest
     */
    public ArrayList<CheckInRequest> getAssignment(String serverName) {
    	ArrayList<CheckInRequest> a = new ArrayList<CheckInRequest>();
    	for(CheckInRequest c : assignments.keySet()){
    		if(assignments.get(c).equals(serverName))
    			a.add(c);
        }
    	return a;
    }

    /**
     * Remove an assignment
     * @param serverName The mapper name as mapperIP:mapperPort
     * @return The assignment removed
     */
    public CheckInRequest removeAssignment(CheckInRequest req) {
        assignments.remove(req);
        return req;
    }

    /**
     * Check if the client is ready to be served the results
     * @return true if true
     */
    boolean isDone() {
        return checkInsAssigned == checkInsDone;
    }

    public void addFail(CheckInRequest req){
    	if(fails.containsKey(req)){
    		fails.put(req,fails.get(req)+1);
    	}else{
    		fails.put(req, 1);
    	}
        
    }

    public boolean thresholdReached(CheckInRequest req){
        return fails.get(req) == 3;
    }
    
    public void reassign(CheckInRequest req, String new_mapper){
    	assignments.put(req, new_mapper);
    	reassigned.add(req);
    	fails.remove(req);
    }
    
    public boolean failedTwice(CheckInRequest name){
    	return reassigned.contains(name) && thresholdReached(name);
    }
}
