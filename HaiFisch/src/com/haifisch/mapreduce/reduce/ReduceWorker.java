package com.haifisch.mapreduce.reduce;

import com.haifisch.mapreduce.*;

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
	public Map<Integer, Object> reduce(int key, Object value);
	
	/**
	 * Sends the results of the reducing process.
	 * @param map
	 */
	public void sendResults(Map<Integer, Object> map);
	
}
