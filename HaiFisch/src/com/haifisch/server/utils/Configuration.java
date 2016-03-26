package com.haifisch.server.utils;

public class Configuration {
    public final String masterServerName;
    public final int masterServerPort;

    public Configuration(String masterServerName, int masterServerPort) {
        this.masterServerName = masterServerName;
        this.masterServerPort = masterServerPort;
    }


}
