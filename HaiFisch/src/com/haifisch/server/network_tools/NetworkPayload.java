package com.haifisch.server.network_tools;

import java.io.*;

/**
 * A serializable network payload utility class. It stores the data transmitted.
 */

public class NetworkPayload implements Serializable {

    private static final long serialVersionUID = 2212722724046031297L;

    public Serializable payload;
    public final boolean REQUIRE_RESPONSE;
    public final NetworkPayloadType PAYLOAD_TYPE;
    public final String SENDER_NAME;
    public final int SENDER_PORT;
    public final int STATUS;
    public final String MESSAGE;

    /**
     * @param type       0 for checkIn(s) request, 1 checkIn result, 2 new checkIn, 3 Connection Acknowledge, 4 status check
     * @param reqResp    require or not a response. Perhaps not needed
     * @param payload    the actual object
     * @param senderName The name of the sender
     * @param senderPort The port of the sender
     * @param status     The http status of the payload
     * @param message    An optional message for the receiver
     */
    public NetworkPayload(NetworkPayloadType type, boolean reqResp, Serializable payload, String senderName, int senderPort, int status, String message) {
        this.payload = payload;
        REQUIRE_RESPONSE = reqResp;
        PAYLOAD_TYPE = type;
        this.SENDER_NAME = senderName;
        this.SENDER_PORT = senderPort;
        STATUS = status;
        MESSAGE = message;
    }

}
