package com.haifisch.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Point of Interest.
 */

public class PointOfInterest implements Comparable<PointOfInterest>, Serializable {

    private static final long serialVersionUID = 26754458721582L;

    private String id;
    private String name;
    private String category;
    private int category_id;
    private Point coords;
    private List<String> photos;
    private int numOfCheckIns;

    /**
     * Basic constructor
     *
     * @param id          The point of interest unique id
     * @param name        The point of interest name
     * @param category    The point of interest category
     * @param category_id The point of interest category id
     * @param coords      The point of interest coordinates
     */
    public PointOfInterest(String id, String name, String category, int category_id, Point coords) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.category_id = category_id;
        this.coords = coords;
        this.photos = new ArrayList<>();
        this.numOfCheckIns = 0;
    }

    /**
     * Secondary constructor
     *
     * @param id          The point of interest unique id
     * @param name        The point of interest name
     * @param category    The point of interest category
     * @param category_id The point of interest category id
     * @param longitude   The point of interest longitude
     * @param latitude    The point of interest latitude
     */
    public PointOfInterest(String id, String name, String category, int category_id, double longitude, double latitude) {
        this(id, name, category, category_id, new Point(longitude, latitude));
    }

    /**
     * GETTERS & SETTERS
     */

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public int getCategoryId() {
        return this.category_id;
    }

    public Point getCoordinates() {
        return this.coords;
    }

    public List<String> getPhotos() {
        return this.photos;
    }

    public int getNumberOfCheckIns() {
        return this.numOfCheckIns;
    }

    public int getNumberOfPhotos() {
        return this.photos.size();
    }
    /**
     * Compares the current instance to a PointOfInterest
     */
    @Override
    public int compareTo(PointOfInterest p) {
        return Integer.signum(p.getNumberOfCheckIns() - this.getNumberOfCheckIns());
    }

}
