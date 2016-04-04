package com.haifisch.server.NetworkTools;

public interface onConnectionListener {

	/**
	 * it's a callback
	 * @param payload the data transmitted
	 */
    void onConnect(NetworkPayload payload);

}
