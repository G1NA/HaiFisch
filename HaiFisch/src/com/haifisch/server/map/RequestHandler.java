package com.haifisch.server.map;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.NetworkTools.NetworkPayload;
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
        if (request.payload instanceof ConnectionAcknowledge) {
            ConnectionAcknowledge connected = (ConnectionAcknowledge) request.payload;
            if(connected.TYPE == 3){
                //Store reducer name and port
            }

        } else if (request.payload instanceof CheckInRequest) {
            Mapper map = new Mapper((CheckInRequest) request.payload);
            Thread r = new Thread(map, new RandomString(6).nextString());
            r.start();
            try {
                r.join();
                if (map.shitHappened) {
                    //damn
                    System.out.println("Fuck");
                } else {
                    CheckInMap pois = map.getResults();

                    SenderSocket send = new SenderSocket("reducerName", 25,
                            new NetworkPayload(3, false, null,
                                    "mapperName", 4, 400, "Results incoming"));
                    send.run();
                    if (send.isSent())
                        System.out.println("Done");

                    send = new SenderSocket("masterName", 25,
                            new NetworkPayload(3, false, null,
                                    "mapperName", 4, 400, "Done with request"));
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
                new NetworkPayload(3, false, null,
                        "asdf", 4, 400, "Shit happened"));
        send.run();
        if (!send.isSent())
            Master.actionLog(send.getError());
    }
}
