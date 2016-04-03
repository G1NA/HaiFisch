package com.haifisch.server.reduce;

import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.NetworkPayloadType;
import com.haifisch.server.NetworkTools.SenderSocket;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.RandomString;

class RequestHandler implements Runnable {

    private final NetworkPayload request;

    RequestHandler(NetworkPayload payload) {
        this.request = payload;
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

            Reduce_Server.putData(res.getRequest_id(), res.getMap());
            Reduce_Server.setTopK(res.getRequest_id(), res.getTopK());


        } else if (request.PAYLOAD_TYPE == NetworkPayloadType.START_REDUCE) {
            //----> pros stigmin 8ewrw oti sto payload pedio tou NetworkPayload vrisketai to id tou request p prepei
            // na e3ipiretisw.... 8a mporousame i na to enswmatwsoume sto CheckInRes i se kati allo
            // alla logika ki etsi douleuei
            Reducer reduce = new Reducer();
            Thread r = new Thread(reduce, new RandomString(6).nextString());
            r.setPriority(Thread.MAX_PRIORITY);
            Reduce_Server.getData(request.MESSAGE).stream().forEach(reduce::addMap);
            reduce.setTopK(Reduce_Server.getTopK(request.MESSAGE));
            Reduce_Server.removeData(request.MESSAGE);
            // ----> add res in the reducer thread 
            r.start();
            try {
                r.join();
                CheckInRes results = new CheckInRes(request.MESSAGE, reduce.getResults(), reduce.getTopK());
                SenderSocket send = new SenderSocket(Reduce_Server.server.getConfiguration().masterServerName,
                        Reduce_Server.server.getConfiguration().masterServerPort,
                        new NetworkPayload(NetworkPayloadType.CHECK_IN_RESULTS, false, results,
                                Reduce_Server.server.getName(), Reduce_Server.server.getPort(), 200, "Results incoming"));
                send.run();
                if (!send.isSent())
                    errorResponse();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void errorResponse() {
        Configuration config = Reduce_Server.server.getConfiguration();
        //Demo data until the configuration dialog is operational
        SenderSocket send = new SenderSocket(config.masterServerName, config.masterServerPort,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, null, "sup", 5, 500,
                        "Something went terribly wrong"));
        send.run();
        if (!send.isSent())
            System.err.println("Failed to send error");
    }
}
