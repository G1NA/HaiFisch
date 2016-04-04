package com.haifisch.server.reduce;

import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.NetworkPayloadType;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.RandomString;

class RequestHandler implements Runnable {

    private final NetworkPayload request;

    /**
     * constructor
     * @param payload the data sent
     */
    RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the reducer data from the Master Server
        if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS) {
            CheckInRes res = (CheckInRes) request.payload;
            String request_id = res.getRequest_id();
            Reduce_Server.putData(request_id, res.getMap());
            Reduce_Server.setMisc(request_id, res);
            Reduce_Server.mapperDone(request_id);
            if (Reduce_Server.isDone(request_id)) {
                Reducer reduce = new Reducer();
                Thread r = new Thread(reduce, new RandomString(6).nextString());
                r.setPriority(Thread.MAX_PRIORITY);
                Reduce_Server.getData(request_id).stream().forEach(reduce::addMap);
                reduce.setTopK(Reduce_Server.getTopK(request_id));
                Reduce_Server.removeData(request_id);
                // ----> add res in the reducer thread
                r.start();
                try {
                    r.join();
                    CheckInRes results = new CheckInRes(request_id, res.getMapperCount(),
                            reduce.getResults(), reduce.getTopK());

                    SenderSocket send = new SenderSocket(Reduce_Server.server.getConfiguration().masterServerName,
                            Reduce_Server.server.getConfiguration().masterServerPort,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, results,
                                    Reduce_Server.server.getName(), Reduce_Server.server.getPort(), 200, "Results incoming"));
                    send.run();
                    if (!send.isSent())
                        errorResponse();

                } catch (InterruptedException e) {
                    System.err.println("Reduce operation for request: " + request_id + " interrupted");

                }
            }
        }
    }

    /**
     * Sends an error message to the Master if an error occurs
     */
    private void errorResponse() {
        Configuration config = Reduce_Server.server.getConfiguration();
        SenderSocket send = new SenderSocket(config.masterServerName, config.masterServerPort,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null, "sup", 5, 500,
                        "Something went terribly wrong"));
        send.run();
        if (!send.isSent())
            System.err.println("Failed to send error");
    }
}
