package com.haifisch.server.map;

import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.RandomString;

class RequestHandler implements Runnable {
    private final NetworkPayload request;

    RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {

        //Get the reducer data from the Master Server
        if (request.PAYLOAD_TYPE == NetworkPayloadType.CONNECTION_ACK) {
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            if (connected.TYPE == 3) {
                MapperConfiguration.getMapperConfiguration().setReducer(connected.serverName, connected.port);
            }
            //Get a check in request and process it then return the results
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_REQUEST) {
            Mapper map = new Mapper((CheckInRequest) request.payload);
            Thread r = new Thread(map, new RandomString(6).nextString());
            r.setPriority(Thread.MAX_PRIORITY);
            r.start();
            try {
                r.join();
                if (map.shitHappened) {
                    errorResponse();
                } else {
                    //Add the results to the packet
                    CheckInRes results = new CheckInRes(((CheckInRequest) request.payload).getRequestId(),
                            map.getResults());

                    SenderSocket send = new SenderSocket(MapperConfiguration.getMapperConfiguration().reducerName,
                            MapperConfiguration.getMapperConfiguration().reducerPort,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, results,
                                    Map_Server.server.getName(), Map_Server.server.getPort(), 200, "Results incoming"));
                    send.run();
                    if (send.isSent())
                        System.out.println("Done");

                    send = new SenderSocket(MapperConfiguration.getMapperConfiguration().masterServerName,
                            MapperConfiguration.getMapperConfiguration().masterServerPort,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, null,
                                    Map_Server.server.getName(), Map_Server.server.getPort(), 200, "Done with request"));
                    send.run();
                    if (send.isSent())
                        System.out.println("Done");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Respond with an error
    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null,
                        Map_Server.server.getName(), Map_Server.server.getPort(), 400, "Something went wrong!"));
        send.run();
        if (!send.isSent())
            System.err.println("Failed to send error!");
    }
}
