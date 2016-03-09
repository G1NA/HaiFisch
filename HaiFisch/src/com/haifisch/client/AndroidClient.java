package com.haifisch.client;

/**
 * A client interface for the Android
 */

public interface AndroidClient {
	
	/**
	 * 
	 */
	public void distributeToMappers();
	
	/**
	 * 
	 */
	public void waitForMappers();
	
	/**
	 * 
	 */
	public void ackToReducers();
	
	/**
	 * 
	 */
	public void collectDataFromReducers();
	
}
