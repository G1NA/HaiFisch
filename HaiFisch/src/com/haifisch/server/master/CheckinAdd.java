package com.haifisch.server.master;

import java.io.Serializable;
import java.util.Date;

public class CheckinAdd implements Serializable {
    private byte[] pictureData;
    private String place;
    private double[] coordinates;
    private Date date;

    //We'll see
    public CheckinAdd(){

    }
}
