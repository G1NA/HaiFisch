package com.haifisch.server.reduce;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.utils.*;

import java.util.ArrayList;
import java.util.HashMap;


public class Reduce_Server extends MainProgram implements onConnectionListener {


    private final Configuration configuration;
    public static Reduce_Server server;
    private static volatile HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>> requests = new HashMap<>();

    public static void main(String args[]) {


        //Create the object that will get all the input information from the user.
        Questionaire q = new Questionaire();

        //Create a new configuration object for all the mappers.
        Configuration config = new Configuration(q.masterServerName, q.masterServerPort);
        server = new Reduce_Server(config);
        server.toolsInit();
    }

    private Reduce_Server(Configuration configuration) {
        this.configuration = configuration;
        createListeningSocket(); //Create the listening socket.
        connectToMaster(configuration.masterServerName, configuration.masterServerPort);
    }


    @Override
    public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload)).start();
    }

    @Override
    public void onSent(boolean result) {

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static ArrayList<CheckInMap<String, PointOfInterest>> getData(String id) {
        return requests.get(id);
    }

    public static void putData(String id, CheckInMap<String, PointOfInterest> map) {

        if (!requests.containsKey(id))
            requests.put(id, new ArrayList<>());
        requests.get(id).add(map);
    }

    public static void removeDate(String id) {
        requests.remove(id);
    }
}

