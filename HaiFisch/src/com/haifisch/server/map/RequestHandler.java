package com.haifisch.server.map;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;
import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.NetworkPayloadType;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.master.Master;
import com.haifisch.server.utils.RandomString;

public class RequestHandler implements Runnable {
    private final NetworkPayload request;

    public RequestHandler(NetworkPayload payload) {
        this.request = payload;
    }

    @Override
    public void run() {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
    	
    	//---> mporoume sta ifs na koitame to PAYLOAD_TYPE
        if (request.payload instanceof ConnectionAcknowledge) {
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            if(connected.TYPE == 3){ 
            	//--->8ewrw oti exei arxikopoii8ei idi apo t Map_Server
            	MapperConfiguration.getMapperConfiguration().setReducer(connected.serverName, connected.port);
            }

        } else if (request.payload instanceof CheckInRequest) {
            Mapper map = new Mapper((CheckInRequest) request.payload);
            Thread r = new Thread(map, new RandomString(6).nextString());
            r.start();
            try {
                r.join();
                if (map.shitHappened) {
                    //damn //----> isws edw na stelnoume apeu8eias errorResponse()
                    System.out.println("Fuck");
                } else {
                    CheckInMap<String, PointOfInterest> pois = map.getResults();

                    SenderSocket send = new SenderSocket(MapperConfiguration.getMapperConfiguration().reducerName, MapperConfiguration.getMapperConfiguration().reducerPort,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, pois,
                                    Map_Server.getMapperName(), Map_Server.getMapperPort(), 400, "Results incoming"));
                    send.run();
                    if (send.isSent())
                        System.out.println("Done");

                    send = new SenderSocket(MapperConfiguration.getMapperConfiguration().masterServerName, MapperConfiguration.getMapperConfiguration().masterServerPort,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, null, //--> edw dn stelnei tpt e? mono to minima teleiwsa
                            		Map_Server.getMapperName(), Map_Server.getMapperPort(), 400, "Done with request"));
                    send.run();
                    if (send.isSent())
                        System.out.println("Done");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void errorResponse() {
        SenderSocket send = new SenderSocket(request.SENDER_NAME, request.SENDER_PORT,
                new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, null,
                		Map_Server.getMapperName(), Map_Server.getMapperPort(), 400, "Shit happened"));
        send.run();
        if (!send.isSent())
            Master.actionLog(send.getError());
    }
}
