package com.haifisch.server.map;

import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;

public class MapperConfiguration extends Configuration {
    public String reducerName;
    public int reducerPort;
    public Point left;
    public Point right;

    public MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort,
                               Point left, Point right) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
        this.left = left;
        this.right = right;
    }

    public MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

    public MapperConfiguration(String masterServerName, int masterServerPort) {
        super(masterServerName, masterServerPort);
    }

    public void setPoint(int corner, Point point) {
        if (corner == 1)
            left = point;
        else
            right = point;
    }

    public void setReducer(String reducerName, int reducerPort) {
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

}
