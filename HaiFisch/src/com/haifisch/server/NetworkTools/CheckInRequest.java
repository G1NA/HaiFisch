package com.haifisch.server.NetworkTools;

import java.io.Serializable;
import java.sql.Timestamp;


public class CheckInRequest implements Serializable {

	private static final long serialVersionUID = 2547822657122L;
	
	private String areaName;
    //---> 8ewrw oti to 0 einai to longitude kai to 1 einai to latitude
    //---> parola auta isws einai kali kapou mia klassi Coordinates...
    private double[] leftCorner;
    private double[] rightCorner;
    private Timestamp fromTime;
    private Timestamp toTime;
    private String requestId; //This will be generated from the master server to identify a client request

    public CheckInRequest() {

    }
    
    public CheckInRequest(String area, double[] left, double[] right, Timestamp from, Timestamp to, String id){
    	areaName = area;
    	leftCorner = left;
    	rightCorner = right;
    	fromTime = from;
    	toTime = to;
    	requestId = id;
    }

    public CheckInRequest(String id) {
        this.requestId = id;
    }


    public String getArea_name() {
        return areaName;
    }

    public void setAreaName(String area_name) {
        this.areaName = area_name;
    }

    public double[] getLeftCorner() {
        return leftCorner;
    }

    public void setLeftCorner(double[] left_corner) {
        this.leftCorner = left_corner;
    }

    public String getRequestId() {
        return requestId;
    }

    public double[] getRightCorner() {
        return rightCorner;
    }

    public void setRightCorner(double[] right_corner) {
        this.rightCorner = right_corner;
    }

    public Timestamp getFromTime() {
        return fromTime;
    }

    public void setFromTime(Timestamp from_time) {
        this.fromTime = from_time;
    }

    public Timestamp getToTime() {
        return toTime;
    }

    public void setToTime(Timestamp to_time) {
        this.toTime = to_time;
    }
}
