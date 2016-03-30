package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.RandomString;

import java.util.HashMap;

import static com.haifisch.server.master.Master.mappers;
import static com.haifisch.server.master.Master.reducer;
import static com.haifisch.server.master.Master.servingClients;

class RequestHandler implements Runnable {
    private final NetworkPayload request;

    public RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
        if (request.payload instanceof ConnectionAcknowledge) {
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            if (connected.TYPE == 1) {
                mappers.add(connected);
                if (reducer != null)
                    inform(connected);
            } else {
                reducer = connected;
                if (mappers.size() != 0)
                    informBulk();
            }
        } else if (request.payload instanceof CheckInRequest) {
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
        } else if (request.payload instanceof CheckInRes) {
            String client_id = ((CheckInRes) request.payload).getRequest_id();

            //Return the result to the client that requested it
            Client client = Master.servingClients.get(client_id);
            SenderSocket socket = new SenderSocket(client.getClientAddress(), client.getClientPort(),
                    new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, request.payload, Master.getServerName(), Master.getPort(), 200, "Done"));
            socket.run();
            if (!socket.isSent())
                Master.actionLog(socket.getError());
            servingClients.remove(client_id);

        } else if (request.payload instanceof CheckInAdd) {
            //add the new checkin and return a positive response
        }
    }

    //Inform the mappers about the reducer
    private void informBulk() {
        mappers.stream().forEach(s -> new SenderSocket(s.serverName, s.port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(3, reducer.serverName, reducer.port),
                        Master.getServerName(), Master.getPort(), 200, "Done")).run());
    }

    //Get the last one added
    private void inform(ConnectionAcknowledge added) {
        new SenderSocket(added.serverName, added.port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(3, reducer.serverName, reducer.port),
                        Master.getServerName(), Master.getPort(), 200, "Done")).run();
    }

    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null,
                        Master.getServerName(), Master.getPort(), 400, "No mappers or reducer present in the network"));
        send.run();
        if (!send.isSent())
            Master.actionLog(send.getError());
    }
}
