package com.haifisch.server.reduce;

import com.haifisch.server.*;
import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

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
	public CheckInMap<Integer, PointOfInterest> reduce(int key, Object value);
	
	/**
	 * Sends the results of the reducing process.
	 * @param map
	 */
	public void sendResults(CheckInMap<Integer, PointOfInterest> map);
	
}
