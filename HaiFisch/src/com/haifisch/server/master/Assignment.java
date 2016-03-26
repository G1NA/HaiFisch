package com.haifisch.server.master;


import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.utils.Point;
import com.sun.jmx.snmp.Timestamp;

public class Assignment {
    CheckInRequest request;
    Point left;
    Point right;
    Timestamp from;
    Timestamp to;

    public Assignment(CheckInRequest request, Point left, Point right, Timestamp from, Timestamp to) {
        this.request = request;
        this.left = left;
        this.right = right;
        this.from = from;
        this.to = to;
    }

}
