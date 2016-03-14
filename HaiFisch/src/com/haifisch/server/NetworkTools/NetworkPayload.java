package com.haifisch.server.NetworkTools;

import java.io.*;


public class NetworkPayload implements Serializable {


    protected Serializable payload;
    protected final boolean REQUIRE_RESPONSE;
    protected final int PAYLOAD_TYPE;


    /**
     * @param type    0 for chekin(s) request 1 chekin result 2 new chekin
     * @param reqResp require or not a response. Perhaps not needed
     * @param payload the actual object
     */
    public NetworkPayload(int type, boolean reqResp, Serializable payload) {
        this.payload = payload;
        REQUIRE_RESPONSE = reqResp;
        PAYLOAD_TYPE = type;
    }

}
