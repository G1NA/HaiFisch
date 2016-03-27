package com.haifisch.server.reduce;

import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.NetworkPayloadType;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.master.Master;

public class RequestHandler implements Runnable {
    private final NetworkPayload request;

    public RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Will only be used if we want to know which mappers will communicate with us
        if (request.payload instanceof ConnectionAcknowledge) {

            //Receive the mapper results and run the reduce function
        } else if (request.payload instanceof CheckInRes) {

        }
    }

    private void errorResponse() {
        //Demo data until the configuration dialog is operational
        SenderSocket send = new SenderSocket("masterServerName", 10,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null,
                        "Sup", 5, 500, "Something went terribly wrong"));
        send.run();
        if (!send.isSent())
            Master.actionLog(send.getError());
    }
}
