package com.haifisch.server.map;

import com.haifisch.server.Configuration;

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
}
