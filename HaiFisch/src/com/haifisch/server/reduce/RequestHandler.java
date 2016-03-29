package com.haifisch.server.reduce;

import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.NetworkPayloadType;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.master.Master;
import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;
import com.haifisch.server.utils.RandomString;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestHandler implements Runnable {
    private final NetworkPayload request;
    private HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>> requests;

    public RequestHandler(NetworkPayload payload, HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>> requests) {
        this.request = payload;
        this.requests = requests;
    }

    @Override
    public void run() {
        //Get the reducer data from the Master Server

        //Will only be used if we want to know which mappers will communicate with us
        if (request.PAYLOAD_TYPE == NetworkPayloadType.CONNECTION_ACK) {
            // ---> edw dn 3erw t paizei akrivws
            //Receive the mapper results and run the reduce function
        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS) {
            CheckInRes res = (CheckInRes) request.payload;

            if (requests.containsKey(res.getRequest_id())) {
                requests.get(res.getRequest_id()).add(res.getMap());
            } else {
                ArrayList<CheckInMap<String, PointOfInterest>> list = new ArrayList<CheckInMap<String, PointOfInterest>>();
                list.add(res.getMap());
                requests.put(res.getRequest_id(), list);
            }


        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.START_REDUCE) {
            //----> pros stigmin 8ewrw oti sto payload pedio tou NetworkPayload vrisketai to id tou request p prepei
            // na e3ipiretisw.... 8a mporousame i na to enswmatwsoume sto CheckInRes i se kati allo
            // alla logika ki etsi douleuei
            Reducer reduce = new Reducer();
            Thread r = new Thread(reduce, new RandomString(6).nextString());
            r.setPriority(Thread.MAX_PRIORITY);
            ArrayList<CheckInMap<String, PointOfInterest>> res = requests.remove(request.payload);
            // ----> add res in the reducer thread 
            r.start();
            try {
                r.join();

                CheckInRes results = new CheckInRes((String) request.payload, reduce.getResults());
                SenderSocket send = new SenderSocket(Reduce_Server.server.getConfiguration().masterServerName,
                        Reduce_Server.server.getConfiguration().masterServerPort,
                        new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, results,
                                Reduce_Server.server.getName(), Reduce_Server.server.getPort(), 200, "Results incoming"));
                send.run();
                if (send.isSent())
                    System.out.println("Done");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
