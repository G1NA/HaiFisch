package com.haifisch.server.map;

import java.io.Serializable;

public class CheckinRes implements Serializable{


    private String place;
    private int noOfPics;
    private String request_id;//This will be generated from the master server to identify a client request

    public CheckinRes(String place, int noOfPics, String request_id) {
        this.place = place;
        this.noOfPics = noOfPics;
        this.request_id = request_id;
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
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

}
