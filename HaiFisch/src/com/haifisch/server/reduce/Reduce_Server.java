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

    public static void main(String args[]) {


        //Create the object that will get all the input information from the user.
        Questionaire q = new Questionaire();

        //Create the points on the map represented by the given coordinates.
        Point topLeftPoint = new Point(q.topLeftCoordinateLongitude, q.topLeftCoordinateLatitude);
        Point bottomRightPoint = new Point(q.bottomRightCoordinateLongtitude, q.bottomRightCoordinateLatitude);

        //Calculate the other two points of the map.
        Point bottomLeftPoint = new Point(topLeftPoint.longtitude, bottomRightPoint.latitude);
        Point topRightPoint = new Point(bottomRightPoint.longtitude, topLeftPoint.latitude);

        topRightPoint.print(); //DEBUG
        bottomLeftPoint.print(); //DEBUG

        //Create a new configuration object for all the mappers.
        Configuration config = new Configuration(q.masterServerName, q.masterServerPort);
        server = new Reduce_Server(config);
        server.toolsInit();

        HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>> requests = new HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>>();
    }

    private Reduce_Server(Configuration configuration) {
        this.configuration = configuration;
        super.createListeningSocket(); //Create the listening socket.
    }


    @Override
    public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new com.haifisch.server.map.RequestHandler(payload)).start();
    }

    @Override
    public void onSent(boolean result) {

    }

    public Configuration getConfiguration() {
        return configuration;
    }
}

