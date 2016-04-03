package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.PointOfInterest;
import com.haifisch.server.utils.RandomString;

import java.util.HashMap;
import java.util.stream.Collectors;

import static com.haifisch.server.master.Master.*;

class RequestHandler implements Runnable {
    private final NetworkPayload request;

    RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
        if (request.PAYLOAD_TYPE == NetworkPayloadType.CONNECTION_ACK) {
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            if (request.STATUS == 500) {
                System.err.println("Request sent to: " + connected.serverName + ":" + connected.port + " failed with error: "
                        + request.MESSAGE);
                return;
            }
            if (connected.TYPE == 1) {
                mappers.add(connected);
                if (reducer != null)
                    inform(connected);
            } else {
                reducer = connected;
                if (mappers.size() != 0)
                    informBulk();
            }
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.STATUS_REPLY) {
            mappers.forEach(e -> {
                if ((e.serverName).equals(request.SENDER_NAME) && e.port == request.SENDER_PORT)
                    e.status = 1;
            });
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_REQUEST) {
            //If there are no mappers or reducer the request should return an error
            if (mappers.size() == 0 || reducer == null)
                errorResponse();
            else {

                String client_id = new RandomString(10).nextString();
                //Will be changed later on
                servingClients.put(client_id, new Client(request.SENDER_NAME, request.SENDER_PORT, client_id,
                        mappers.size(), new HashMap<>()));
                //Do what the client asked

            }
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS) {

            //Received mapper operation end
            if (request.payload == null) {
                Client cl = Master.servingClients.get(request.MESSAGE);
                cl.markDone(request.SENDER_NAME + ":" + request.SENDER_PORT);
                if (cl.isDone()) {
                    SenderSocket socket = new SenderSocket(Master.reducer.serverName, Master.reducer.port,
                            new NetworkPayload(NetworkPayloadType.START_REDUCE, false, null,
                                    Master.masterThread.getName(), Master.masterThread.getPort(), 200, request.MESSAGE));
                    socket.run();
                    if (socket.isSent())
                        Master.servingClients.remove(request.MESSAGE);
                    else
                        System.err.println("Error when sending reduce for request: " + request.MESSAGE + " to reducer: "
                                + Master.reducer.serverName + ":" + Master.reducer.port);
                }

            } else {
                String client_id = ((CheckInRes) request.payload).getRequest_id();
                //Return the result to the client that requested it
                Client client = Master.servingClients.get(client_id);
                /*
                SenderSocket socket = new SenderSocket(client.getClientAddress(), client.getClientPort(),
                        new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, request.payload,
                                Master.masterThread.getName(), Master.masterThread.getPort(), 200, "Done"));
                socket.run();
                if (!socket.isSent())
                    System.out.println(socket.getError());*/
                System.out.println("Request for: " + client_id + " finished with success and produced the following: ");
                for (PointOfInterest res : ((CheckInRes) request.payload).getMap().values().stream().sorted(PointOfInterest::compareTo).collect(Collectors.toList())) {
                    System.out.println("Place name: " + res.getName() +
                            " checkins: " + res.getNumberOfCheckIns()
                            + " photos: " + res.getNumberOfPhotos());
                }
                servingClients.remove(client_id);
            }
        } else if (request.payload instanceof CheckInAdd) {
            //add the new checkin and return a positive response
        }
    }

    //Inform the mappers about the reducer
    private void informBulk() {
        mappers.stream().forEach(s -> new SenderSocket(s.serverName, s.port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(3,
                        reducer.serverName, reducer.port),
                        Master.masterThread.getName(), Master.masterThread.getPort(), 200, "Done")).run());
    }

    //Get the last one added
    private void inform(ConnectionAcknowledge added) {
        new SenderSocket(added.serverName, added.port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(3,
                        reducer.serverName, reducer.port),
                        Master.masterThread.getName(), Master.masterThread.getPort(), 200, "Done")).run();
    }

    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null,
                        Master.masterThread.getName(), Master.masterThread.getPort(),
                        400, "No mappers or reducer present in the network"));
        send.run();
        if (!send.isSent())
            System.out.println(send.getError());
    }
}
