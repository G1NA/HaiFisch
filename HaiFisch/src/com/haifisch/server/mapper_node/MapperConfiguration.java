package com.haifisch.server.mapper_node;

import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;

public class MapperConfiguration extends Configuration {

    private static MapperConfiguration configInstance;

    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    String reducerName;
    int reducerPort;
    private Point left;
    private Point right;

    /**
     * Singleton retriever (DEPRECATED)
     *
     * @param masterServerName The name of the master_node node
     * @param masterServerPort The port of the master_node node
     * @param reducerName      The name of the reducer node
     * @param reducerPort      The port of the reducer node
     * @param left             The bottom left point of the mapper
     * @param right            The top right point of the mapper
     * @return the instance
     */
    public static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort,
                                                             Point left, Point right) {
        if (configInstance == null) {
            configInstance = new MapperConfiguration(masterServerName, masterServerPort, reducerName, reducerPort, left, right);
        } else {
            configInstance.setReducer(reducerName, reducerPort);
            configInstance.setPoint(LEFT, left);
            configInstance.setPoint(RIGHT, right);
        }
        return configInstance;
    }

    /**
     * Singleton retriever
     *
     * @param masterServerName The name of the master_node node
     * @param masterServerPort The port of the master_node node
     * @param reducerName      The name of the reducer node
     * @param reducerPort      The port of the reducer node
     * @return the instance
     */
    public static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort) {
        if (configInstance == null)
            configInstance = new MapperConfiguration(masterServerName, masterServerPort, reducerName, reducerPort);
        else
            configInstance.setReducer(reducerName, reducerPort);

        return configInstance;
    }

    /**
     * Singleton retriever
     *
     * @param masterServerName The name of the master_node node
     * @param masterServerPort The port of the master_node node
     * @return the instance
     */
    static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort) {
        if (configInstance == null)
            configInstance = new MapperConfiguration(masterServerName, masterServerPort);

        return configInstance;
    }

    static MapperConfiguration getMapperConfiguration() {
        return configInstance;
    }


    private MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort,
                                Point left, Point right) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
        this.left = left;
        this.right = right;
    }

    private MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

    private MapperConfiguration(String masterServerName, int masterServerPort) {
        super(masterServerName, masterServerPort);
    }

    private void setPoint(int corner, Point point) {
        if (corner == 1)
            left = point;
        else
            right = point;
    }

    void setReducer(String reducerName, int reducerPort) {
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

    public String getReducer() {

        return reducerName;
    }

}
