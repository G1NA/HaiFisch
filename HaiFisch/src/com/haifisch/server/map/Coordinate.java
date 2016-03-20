package com.haifisch.server.map;

/** Class that represents a point on a map. */
public class Coordinate {

	public int longtitude;
	public int latitude;
	
	/**
	 * Constructor.
	 * @param longtitude
	 * @param latitude
	 */
	public Coordinate(int longtitude, int latitude) {
	
		this.longtitude = longtitude;
		this.latitude = latitude;
	}
	
	/**
	 * Sets the longtitude of a map coordinate.
	 * @param longtitude
	 */
	public void setLongtitude(int longtitude) {

		this.longtitude = longtitude;
	}
	
	/**
	 * Sets the latitude of a map coordinate.
	 * @param latitude
	 */
	public void setLatitude(int latitude) {

		this.latitude = latitude;
	}
	
	/**
	 * Gets the longtitude of a map coordinate.
	 * @return longtitude
	 */
	public int getLongtitude() {

		return longtitude;
	}
	
	/**
	 * Gets the latitude of a map coordinate.
	 * @return latitude
	 */
	public int getLatitude() {

		return latitude;
	}
}