package com.haifisch.server.NetworkTools;

import java.io.*;


public class NetworkPayload implements Serializable {


    public Serializable payload;
    public final boolean REQUIRE_RESPONSE;
    public final int PAYLOAD_TYPE;


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
