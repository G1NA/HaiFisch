package com.haifisch.server.map;

import java.io.Serializable;


public class CheckinRequest implements Serializable {


    private String area_name;
    private double[] left_corner;
    private double[] right_corner;
    private double[] from_time;
    private double[] to_time;
    private String request_id; //This will be generated from the master server to identify a client request

    public CheckinRequest() {

    }

    public CheckinRequest(String id) {
        this.request_id = id;
    }


    public String getArea_name() {
        return area_name;
    }

    public void setAreaName(String area_name) {
        this.area_name = area_name;
    }

    public double[] getLeftCorner() {
        return left_corner;
    }

    public void setLeftCorner(double[] left_corner) {
        this.left_corner = left_corner;
    }

    public String getRequestId() {
        return request_id;
    }

    public double[] getRightCorner() {
        return right_corner;
    }

    public void setRightCorner(double[] right_corner) {
        this.right_corner = right_corner;
    }

    public double[] getFromTime() {
        return from_time;
    }

    public void setFromTime(double[] from_time) {
        this.from_time = from_time;
    }

    public double[] getToTime() {
        return to_time;
    }

    public void setToTime(double[] to_time) {
        this.to_time = to_time;
    }
}
