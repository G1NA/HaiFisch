package com.haifisch.server.master_node;

import java.io.Serializable;
import java.util.Date;

//TODO To be implemented for the 2nd part
public class CheckInAdd implements Serializable {

    private static final long serialVersionUID = 245687941544687L;
    private byte[] pictureData;
    private String place;
    private double[] coordinates;
    private Date date;

    //We'll see
    public CheckInAdd(){

    }
}
