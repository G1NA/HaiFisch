package com.haifisch.server.utils;

/**
 * Class that represents the basic configuration for the communication with the server.
 */

public class Configuration {

    public final String masterServerName;
    public final int masterServerPort;


    /**
     * Configuration constructor
     *
     * @param masterServerName The master_node node name
     * @param masterServerPort The master_node node port
     */
    public Configuration(String masterServerName, int masterServerPort) {
        this.masterServerName = masterServerName;
        this.masterServerPort = masterServerPort;
    }


}
