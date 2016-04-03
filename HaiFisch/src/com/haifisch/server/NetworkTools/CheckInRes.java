package com.haifisch.server.NetworkTools;

import com.haifisch.server.utils.PointOfInterest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

//TODO UPDATE THE FIELDS WITH RELEVANT ATTRIBUTES AS NEEDED
public class CheckInRes implements Serializable {

    private static final long serialVersionUID = 26787521582L;

    private String requestId;//This will be generated from the master server to identify a client request
    private HashMap<String, PointOfInterest> map;
    private List<PointOfInterest> reducer_res;
    private int topK;


    public CheckInRes(String requestId, HashMap<String, PointOfInterest> map, int topK) {
        this.requestId = requestId;
        this.map = map;
        this.topK = topK;
    }

    public HashMap<String, PointOfInterest> getMap() {

        return map;
    }

    public String getRequest_id() {
        return requestId;
    }

    public void setRequest_id(String request_id) {
        this.requestId = request_id;
    }

    public int getTopK() {
        return topK;
    }

}
