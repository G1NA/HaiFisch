package com.haifisch.server.map;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.utils.Point;
import com.haifisch.server.utils.Questionaire;


public class Map_Server extends MainProgram implements onConnectionListener {

    public volatile static Map_Server server;
    private final MapperConfiguration configuration;

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
        MapperConfiguration mapperConfig = MapperConfiguration.getMapperConfiguration(q.masterServerName,
                q.masterServerPort, q.reducerName, q.reducerPort);
        server = new Map_Server(mapperConfig);
        server.toolsInit();

    }

    private Map_Server(MapperConfiguration configuration) {
        this.configuration = configuration;
        createListeningSocket(); //Create the listening socket.
        connectToMaster(configuration.masterServerName,configuration.masterServerPort);
    }

    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload)).start();
    }

    synchronized public MapperConfiguration getConfiguration() {
        return configuration;
    }
}
