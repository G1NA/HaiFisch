package com.haifisch.server.utils;

/** Class that represents the basic configuration for the communication with the server. */

public class Configuration {
	
    public final String masterServerName;
    public final int masterServerPort;
    
    
    /** Constructor */
    public Configuration(String masterServerName, int masterServerPort) {
        this.masterServerName = masterServerName;
        this.masterServerPort = masterServerPort;
    }


}
