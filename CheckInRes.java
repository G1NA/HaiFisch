package com.haifisch.server.NetworkTools;

import java.io.Serializable;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

//TODO UPDATE THE FIELDS WITH RELEVANT ATTRIBUTES AS NEEDED
public class CheckInRes implements Serializable{

	private static final long serialVersionUID = 26787521582L;

    private String requestId;//This will be generated from the master server to identify a client request
    private CheckInMap<String, PointOfInterest>  map;

    public CheckInRes(String requestId, CheckInMap map) {
    	this.requestId = requestId;
        this.map = map;
    }
    
    public CheckInMap<String, PointOfInterest>  getMap(){
    	
    	return map;
    }
    
    public String getRequest_id() {
        return requestId;
    }

    public void setRequest_id(String request_id) {
        this.requestId = request_id;
    }

}
