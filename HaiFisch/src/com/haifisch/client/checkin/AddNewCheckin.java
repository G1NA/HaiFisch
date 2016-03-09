package com.haifisch.client.checkin;

/**
 * 
 */

public interface AddNewCheckin {
	
	/**
	 * Adds a photo for the check-in.
	 */
	public void addPhoto();
	
	/**
	 * Gets the check-in location.
	 */
	public void getLocation();
	
	/**
	 * Sends the check-in to the server.
	 */
	public void sendCheckinToServer();
	
}
