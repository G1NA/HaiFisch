package com.haifisch.server.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * Adds a new photo by its link.
     *
     * @param link the photo link
     */
    private void addPhoto(String link) {
        if (!link.equalsIgnoreCase("Not exists"))
            photos.add(link);
    }

    /**
     * Increments the checkins by 1.
     */
    private void incrementCheckIns() {
        ++this.numOfCheckIns;
    }

    /**
     * Increments the checkins by a value
     *
     * @param value The value to increment
     */
    private void incrementCheckInsBy(int value) {
        this.numOfCheckIns += value;
    }

    /**
     * Adds a list of new photos
     *
     * @param photos the list of photos
     */
    private void addPhotos(List<String> photos) {
        photos.stream().forEach(this::addPhoto);
    }

    /**
     * Adds a new checkin and adds the photo
     *
     * @param photoLink the photo link
     */
    public void addCheckIn(String photoLink) {
        this.incrementCheckIns();
        this.addPhoto(photoLink);
    }

    /**
     * 'Increments' the current instance by a new point of interest.
     * <p>
     * This is done by summing up the number of checkins and the list of photos
     *
     * @param poi the Point of Interest to add
     * @return the updated instance
     */
    public PointOfInterest incrementObject(PointOfInterest poi) {
        // we've checked the equality of the poi's -> its about the same place
        this.incrementCheckInsBy(poi.getNumberOfCheckIns());
        this.addPhotos(poi.getPhotos());
        return this;
    }

    /**
     * Cleans up the duplicate photos of the list.
     */
    public void cleanupDuplicatePhotos() {
        this.photos = this.photos.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Compares the current instance to a PointOfInterest
     */
    @Override
    public int compareTo(PointOfInterest p) {
        return Integer.signum(p.getNumberOfCheckIns() - this.getNumberOfCheckIns());
    }

}
