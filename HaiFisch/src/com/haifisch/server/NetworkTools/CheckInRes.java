package com.haifisch.server.NetworkTools;

import java.io.Serializable;

public class CheckInRes implements Serializable{

	private static final long serialVersionUID = 26787521582L;
	
	private String place;
    private int noOfPics;
    private String requestId;//This will be generated from the master server to identify a client request

    public CheckInRes(String place, int noOfPics, String request_id) {
        this.place = place;
        this.noOfPics = noOfPics;
        this.requestId = request_id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getNoOfPics() {
        return noOfPics;
    }

    public void setNoOfPics(int noOfPics) {
        this.noOfPics = noOfPics;
    }

    public String getRequest_id() {
        return requestId;
    }

    public void setRequest_id(String request_id) {
        this.requestId = request_id;
    }

}
