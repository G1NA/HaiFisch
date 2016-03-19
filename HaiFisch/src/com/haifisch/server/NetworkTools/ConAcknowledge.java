package com.haifisch.server.NetworkTools;

public class ConAcknowledge {

    public final int TYPE;
    public final String serverName;
    public final int port;

    /**
     * Will be used to inform the master node of its Existence
     * @param type 1 for mapper 2 for reducer
     */
    public ConAcknowledge(int type,String serverName, int port){
        this.TYPE = type;
        this.serverName = serverName;
        this.port = port;
    }
}
