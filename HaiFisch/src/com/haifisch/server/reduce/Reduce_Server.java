package com.haifisch.server.reduce;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.PointOfInterest;
import com.haifisch.server.utils.Questionaire;

import java.util.ArrayList;
import java.util.HashMap;


public class Reduce_Server extends MainProgram implements onConnectionListener {


    private final Configuration configuration;
    public volatile static Reduce_Server server;
    private static volatile HashMap<String, Results> requests = new HashMap<>();

    public static void main(String args[]) {


        //Create the object that will get all the input information from the user.
        Questionaire q = new Questionaire();

        //Create a new configuration object for all the mappers.
        Configuration config = new Configuration(q.masterServerName, q.masterServerPort);
        server = new Reduce_Server(config);
        server.initiateButler();
        server.initiateConsole();
    }

    private Reduce_Server(Configuration configuration) {
        this.configuration = configuration;
        createListeningSocket(); //Create the listening socket.
        connectToMaster(configuration.masterServerName, configuration.masterServerPort, 2);
    }


    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload)).start();
    }

    synchronized Configuration getConfiguration() {
        return configuration;
    }

    synchronized static ArrayList<HashMap<String, PointOfInterest>> getData(String id) {
            return requests.get(id).getResults();
    }

    synchronized static void putData(String id, HashMap<String, PointOfInterest> map) {
        if (!requests.containsKey(id))
            requests.put(id, new Results());
        requests.get(id).add(map);
    }

    synchronized static void setMisc(String id, CheckInRes res)
    {
        Results results = requests.get(id);
        results.setTopK(res.getTopK());
        results.setMappers(res.getMapperCount());
    }

    synchronized static void mapperDone(String id)
    {
        requests.get(id).mapperCompleted();
    }
    synchronized static boolean isDone(String id)
    {
        return  requests.get(id).isDone();
    }
    synchronized static int getTopK(String id) {
        return requests.get(id).getTopK();
    }

    synchronized static void removeData(String id) {
        requests.remove(id);
    }
}

