package com.haifisch.server.utils;

import java.io.Serializable;

/**
 * Class that represents a point on a mapper_node.
 */
public class Point implements Serializable {

    private static final long serialVersionUID = 7342155718917685177L;

    public Double longtitude; //X IMPORTANT
    public Double latitude;// Y IMPORTANT

    /**
     * Constructor.
     *
     * @param longtitude
     * @param latitude
     */
    public Point(Double longtitude, Double latitude) {

        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    /**
     * Sets the longtitude of a mapper_node coordinate.
     *
     * @param longtitude
     */
    public void setLongtitude(Double longtitude) {

        this.longtitude = longtitude;
    }

    /**
     * Sets the latitude of a mapper_node coordinate.
     *
     * @param latitude
     */
    public void setLatitude(Double latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longtitude of a mapper_node coordinate.
     *
     * @return longtitude
     */
    public Double getLongtitude() {

        return longtitude;
    }

    /**
     * Gets the latitude of a mapper_node coordinate.
     *
     * @return latitude
     */
    public Double getLatitude() {

        return latitude;
    }

    /**
     * Prints the coordinates of the point.
     */
    public void print() {

        System.out.println("Longtitude: " + this.getLongtitude() + " latitude: " + this.getLatitude());
    }
}