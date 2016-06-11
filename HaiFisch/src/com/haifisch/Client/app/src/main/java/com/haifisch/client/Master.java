package com.haifisch.client;

import android.app.Application;

import java.util.List;

import commons.NetworkPayload;
import commons.PointOfInterest;
import commons.onConnectionListener;

public class Master extends Application implements onConnectionListener {
    public Communicator communicator;
    public static volatile List<PointOfInterest> visiblePois;
    public static String masterIP;
    public static int masterPort;


    @Override
    public void onCreate() {
        super.onCreate();
        communicator = new Communicator(this);
        communicator.execute(new Object());

    }

    @Override
    public void onConnect(NetworkPayload networkPayload) {
        //We'll see
    }
}
