package com.haifisch.client;

import android.app.Application;

import commons.NetworkPayload;
import commons.onConnectionListener;

public class Master extends Application implements onConnectionListener {
    public Communicator communicator;

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
