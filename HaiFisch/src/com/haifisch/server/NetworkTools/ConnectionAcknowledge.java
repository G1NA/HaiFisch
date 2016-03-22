package com.haifisch.server.NetworkTools;

import java.io.Serializable;

public class ConnectionAcknowledge implements Serializable{

	private static final long serialVersionUID = 7342155718917655917L;
	
	public final int TYPE;
    public final String serverName;
    public final int port;

    /**
     * Will be used to inform the master node of its Existence
     * @param type 1 for mapper 2 for reducer type 3 inform for reducer existence
     */
    public ConnectionAcknowledge(int type,String serverName, int port){
        this.TYPE = type;
        this.serverName = serverName;
        this.port = port;
    }
}
