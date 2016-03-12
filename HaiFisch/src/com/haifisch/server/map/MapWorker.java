package com.haifisch.server.map;

import com.haifisch.server.*;

/**
 * A Worker interface for the map process.
 */

public interface MapWorker extends Worker {
	
	/**
	 * Central map function.
	 * @param key
	 * @param value
	 * @return
	 */
	public Map<Integer, Object> map(Object key, Object value);
	
	/**
	 * 
	 */
	public void notifyMaster();
	
	/**
	 * Sends the map for reducing to the respective workers.
	 * @param map to be reduced
	 */
	public void sendToReducers(Map<Integer, Object> map);
	
}
