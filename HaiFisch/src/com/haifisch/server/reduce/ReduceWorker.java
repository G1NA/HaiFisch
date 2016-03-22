package com.haifisch.server.reduce;

import com.haifisch.server.*;

/**
 * A Worker interface for the reduce process.
 */

public interface ReduceWorker extends Worker {
	
	public void waitForMasterAck();
	
	/**
	 * Central reducing function.
	 * @param key
	 * @param value
	 * @return
	 */
	public CheckInMap<Integer, Object> reduce(int key, Object value);
	
	/**
	 * Sends the results of the reducing process.
	 * @param map
	 */
	public void sendResults(CheckInMap<Integer, Object> map);
	
}
