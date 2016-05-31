package com.haifisch.server.mapper_node;

import commons.Point;

class CheckIn {

    private String poi;
    private String poi_name;
    private String poi_category;
    private int poi_category_id;
    private String link;
    private Point coords;

    /**
     * CheckIn constructor
     *
     * @param poi             Point of interest unique id
     * @param poi_name        The name
     * @param poi_category    The category name
     * @param poi_category_id The category id
     * @param longitude       The longitude
     * @param latitude        The latitude
     * @param link            The photo link
     */
    CheckIn(String poi, String poi_name, String poi_category, int poi_category_id, double longitude, double latitude, String link) {
        this.poi = poi;
        this.poi_name = poi_name;
        this.poi_category = poi_category;
        this.poi_category_id = poi_category_id;
        this.link = link;
        this.coords = new Point(longitude, latitude);

    }

    String getPOI() {
        return this.poi;
    }

    String getPOI_NAME() {
        return this.poi_name;
    }

    String getLINK() {
        return this.link;
    }

    String getPOI_CATEGORY() {
        return this.poi_category;
    }

    int getPOI_CATEGORY_ID() {
        return this.poi_category_id;
    }

    Point getCOORDINATES() {
        return this.coords;
    }

    public double getLONGITUDE() {
        return coords.getLongtitude();
    }

    public double getLATITUDE() {
        return coords.getLatitude();
    }

}
