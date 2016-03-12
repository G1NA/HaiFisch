package com.haifisch.server;

/**
 * An interface for the Worker classes.
 */

public interface Worker {
	
	/**
	 * Initializes the Worker
	 */
	public void initialize();
	
	/**
	 * 
	 */
	public void waitForTasksThread();
	
}
