package com.haifisch.server.master_node;

import com.haifisch.server.utils.RandomString;
import commons.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

import static com.haifisch.server.master_node.Master.*;

class RequestHandler implements Runnable {

    private final NetworkPayload request;

    /**
     * Constructor
     *
     * @param payload The request to handle
     */
    RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the connections coming from mappers and reducers and add them to the node pool
        if (request.PAYLOAD_TYPE == NetworkPayloadType.CONNECTION_ACK) {
            if (request.STATUS == 500) {
                if (request.payload instanceof CheckInRequest) {
                    System.err.println("Request " + ((CheckInRequest) request.payload).getRequestId()
                            + "sent to: " + request.SENDER_NAME + ":" + request.SENDER_PORT + " failed with error: "
                            + request.MESSAGE);
                    Client cl = Master.servingClients.get(((CheckInRequest) request.payload).getRequestId());
                    cl.addFail((CheckInRequest) request.payload);

                    //The maximum tries for the server have been reached assign to another one
                    if (cl.thresholdReached((CheckInRequest) request.payload)) {
                    	if(cl.failedTwice((CheckInRequest) request.payload)){
                    		//TODO send error to the client
                    	}else{
                    		assignToAnotherOne(request.SENDER_NAME, request.SENDER_PORT, (CheckInRequest)request.payload, cl);
                    	}
                    }

                } else if (request.payload instanceof CheckInRes) {
                    System.err.println("Request " + ((CheckInRes) request.payload).getRequestId()
                            + "sent to: " + request.SENDER_NAME + ":" + request.SENDER_PORT + " failed with error: "
                            + request.MESSAGE);
                }
                return;
            }
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            //If the sender is a client
            if (connected.TYPE == ConnectionAcknowledgeType.CLIENT) {
                //Do nothing now
            } else if (connected.TYPE == ConnectionAcknowledgeType.MAPPER) {
                mappers.add(connected);
                if (reducer != null)
                    inform(connected);
            } else {
                reducer = connected;
                if (mappers.size() != 0)
                    informBulk();
            }
            //If the request is an alive status reply
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.STATUS_REPLY) {
            mappers.forEach(e -> {
                if ((e.serverName).equals(request.SENDER_NAME) && e.port == request.SENDER_PORT)
                    e.status = 1;
            });
            if (reducer.serverName.equals(request.SENDER_NAME) && reducer.port == request.SENDER_PORT)
                reducer.status = 1;
            //If the request is a nce check in
            //TODO not currently used will be implemented for the 2nd part
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_REQUEST) {
            //If there are no mappers or reducer the request should return an error
            if (mappers.size() == 0 || reducer == null)
                errorResponse();
            else {

                //This does NOT identify the client, but the request of the client
                String client_id = new RandomString(10).nextString();
                while (servingClients.get(client_id) != null)
                    client_id = new RandomString(10).nextString();
                //Will be changed later on
                servingClients.put(client_id, new Client(request.SENDER_NAME, request.SENDER_PORT, client_id,
                        mappers.size(), new HashMap<>()));
                Client cl = servingClients.get(client_id);
                //Do what the client asked
                CheckInRequest req = (CheckInRequest) request.payload;
                Point left = req.getLeftCorner();
                Point right = req.getRightCorner();
                Timestamp stampFrom = req.getFromTime();
                Timestamp stampTo = req.getToTime();
                int length = mappers.size();
                double partSize = (right.longtitude - left.longtitude) / length;
                Point trueLeft;
                Point trueRight;
                SenderSocket socket;
                for (int i = 0; i < length; i++) {
                    trueLeft = new Point(left.longtitude + partSize * i, left.latitude);
                    trueRight = new Point(left.longtitude + partSize * (i + 1), right.latitude);
                    req = new CheckInRequest(client_id+i, length, trueLeft, trueRight, stampFrom,
                            stampTo);
                    req.setTopK(100);
                    socket = new SenderSocket(mappers.get(i).serverName,
                            mappers.get(i).port,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                                    req, masterThread.getName(), Master.masterThread.getPort(), 200, "Incoming request"));
                    socket.run();
                    if (!socket.isSent()) {
                        System.err.println("Failed to send request to: " + mappers.get(i).serverName);
                        break;
                    } else
                        cl.addAssignment(req,mappers.get(i).serverName + ":" + mappers.get(i).port);
                }
            }
            // Received a check in result packet from a mapper or a reducer
        }else if(request.PAYLOAD_TYPE == NetworkPayloadType.MAPPER_FINISHED){
        	
            Client cl = servingClients.get(request.MESSAGE);
            cl.markDone((CheckInRequest)request.payload);

            if (cl.isDone()) {
                System.out.println("Mappers completed request: "
                        + request.MESSAGE + " waiting for reducer results");
        
    	} else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS) {

            //Received mapper operation end
            //TODO use only if the master_node must inform the reducer, currently it simply monitors the request state
            if (request.payload == null) {

                    /*
                    SenderSocket socket = new SenderSocket(Master.reducer.serverName, Master.reducer.port,
                            new NetworkPayload(NetworkPayloadType.START_REDUCE, false, null,
                                    Master.masterThread.getName(), Master.masterThread.getPort(), 200, request.MESSAGE));
                    socket.run();
                    if (socket.isSent())
                        Master.servingClients.remove(request.MESSAGE);
                    else
                        System.err.println("Error when sending reducer_node for request: " + request.MESSAGE + " to reducer: "
                                + Master.reducer.serverName + ":" + Master.reducer.port);
                                */
                }
                //IF the results came from a reducer
            } else {
                String client_id = ((CheckInRes) request.payload).getRequestId();
                Client client = Master.servingClients.get(client_id);

                //TODO for the 2nd part where we respond to the client
                SenderSocket socket = new SenderSocket(client.getClientAddress(), client.getClientPort(),
                        new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, request.payload,
                                Master.masterThread.getName(), Master.masterThread.getPort(), 200, "Done"));
                socket.run();
                if (!socket.isSent())
                    System.out.println(socket.getError());


                //Print the results received
                //TODO WILL BE REMOVED ON THE 2ND PART ONLY FOR DEBUG
                System.out.println("Request for: " + client_id + " finished with success and produced the following: ");
                for (PointOfInterest res : ((CheckInRes) request.payload)
                        .getMap().values()
                        .stream().sorted(PointOfInterest::compareTo).collect(Collectors.toList())) {

                    System.out.println("Place name: " + res.getName() +
                            " checkins: " + res.getNumberOfCheckIns()
                            + " photos: " + res.getNumberOfPhotos());
                }
                servingClients.remove(client_id);
            }
            //TODO will be added on the 2nd part
        } else if (request.payload instanceof CheckInAdd) {

        }
    }

    private void assignToAnotherOne(String mapper_name, int mapper_port, CheckInRequest request, Client cl) {
		Random r = new Random();
		int mapper = r.nextInt(mappers.size()); //gets a random number to start from
		for(int i=0; i < mappers.size(); i++){
			if(mappers.get(mapper).serverName.equals(mapper_name)){
				mapper = (mapper+1) % mappers.size(); //TODO mipws dn einai swsto?? mipws 8elei % (size-1)
			}else{
				SenderSocket socket = new SenderSocket(mappers.get(mapper).serverName,
		                 mappers.get(mapper).port,
		                 new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
		                         request, masterThread.getName(), Master.masterThread.getPort(), 200, "Incoming request"));
		        socket.run();
		        if (!socket.isSent()){
		        	mapper = (mapper+1) % mappers.size();
		        	continue;
		        }else{
		        	cl.reassign(request, mappers.get(mapper).serverName+":"+mappers.get(mapper).port);
		        }
			}
			
		}
		
		 
	}

	/**
     * Inform all the mappers about the reducer that was added in the network
     */
    private void informBulk() {
        mappers.forEach(this::inform);
    }

    /**
     * inform a single mapper about the new reducer
     *
     * @param added The mapper to be informed
     */
    private void inform(ConnectionAcknowledge added) {
        new SenderSocket(added.serverName, added.port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(ConnectionAcknowledgeType.REDUCER_EXISTS,
                        reducer.serverName, reducer.port),
                        Master.masterThread.getName(), Master.masterThread.getPort(), 200, "Done")).run();
    }

    /**
     * Send an error message to the master_node server
     */
    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null,
                        Master.masterThread.getName(), Master.masterThread.getPort(),
                        500, "No mappers or reducer present in the network"));
        send.run();
        if (!send.isSent())
            System.out.println(send.getError());
    }
}
