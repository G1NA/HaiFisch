package com.haifisch.server.NetworkTools;

import com.haifisch.server.utils.PointOfInterest;

import java.io.Serializable;
import java.util.HashMap;

//TODO UPDATE THE FIELDS WITH RELEVANT ATTRIBUTES AS NEEDED
public class CheckInRes implements Serializable{

	private static final long serialVersionUID = 26787521582L;

    private String requestId;//This will be generated from the master server to identify a client request
    private HashMap<String, PointOfInterest> map;

    public CheckInRes(String requestId, HashMap map) {
    	this.requestId = requestId;
        this.map = map;
    }
    
    public HashMap<String, PointOfInterest> getMap(){
    	
    	return map;
    }
    
    public String getRequest_id() {
        return requestId;
    }

    public void setRequest_id(String request_id) {
        this.requestId = request_id;
    }

}
