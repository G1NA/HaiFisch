package com.haifisch.server.reduce;

import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.master.Master;

public class RequestHandler implements Runnable {
    private final NetworkPayload request;

    public RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
        if (request.payload instanceof ConnectionAcknowledge) {

        } else if (request.payload instanceof CheckInRes) {

        }
    }

    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(3, false, null,
                        "Sup", 5, 400, "No mappers or reducer present in the network"));
        send.run();
        if (!send.isSent())
            Master.actionLog(send.getError());
    }
}
