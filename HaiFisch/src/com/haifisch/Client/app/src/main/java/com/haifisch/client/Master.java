package com.haifisch.client;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.List;

import commons.NetworkPayload;
import commons.PointOfInterest;
import commons.onConnectionListener;

public class Master extends Application implements onConnectionListener {
    public Communicator communicator;
    public static volatile List<PointOfInterest> visiblePois;
    public static String masterIP;
    public static int masterPort;
    public static String ownIp;
    public static int topK = 100;

    @Override
    public void onCreate() {
        super.onCreate();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ownIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        communicator = new Communicator(this);
        communicator.execute(new Object());

    }

    @Override
    public void onConnect(NetworkPayload networkPayload) {
        //We'll see
    }
}
