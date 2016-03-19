package com.haifisch.server.NetworkTools;

import java.io.Serializable;
import java.sql.Timestamp;


public class CheckinRequest implements Serializable {


    private String area_name;
    //---> 8ewrw oti to 0 einai to longitude kai to 1 einai to latitude
    //---> parola auta isws einai kali kapou mia klassi Coordinates...
    private double[] left_corner;
    private double[] right_corner;
    private Timestamp from_time;
    private Timestamp to_time;
    private String request_id; //This will be generated from the master server to identify a client request

    public CheckinRequest() {

    }
    
    public CheckinRequest(String area, double[] left, double[] right, Timestamp from, Timestamp to, String id){
    	area_name = area;
    	left_corner = left;
    	right_corner = right;
    	from_time = from;
    	to_time = to;
    	request_id = id;
    	
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

    public Timestamp getFromTime() {
        return from_time;
    }

    public void setFromTime(Timestamp from_time) {
        this.from_time = from_time;
    }

    public Timestamp getToTime() {
        return to_time;
    }

    public void setToTime(Timestamp to_time) {
        this.to_time = to_time;
    }
}
