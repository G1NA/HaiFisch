package com.haifisch.client.checkin;

/**
 * 
 */

public interface AddNewCheckinService {
	
	/**
	 * Initializes the check-in service for adding a new one.
	 */
	public void initialize();
	
	/**
	 * 
	 */
	public void waitForNewCheckinsThread();
	
	/**
	 * 
	 */
	public void insertCheckinToDatabase(Checkin checkin);
	
	/**
	 * 
	 */
	public void ackToClient();
	
}
