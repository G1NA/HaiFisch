package com.haifisch.server.map;

public interface onConnectionListener {


    void onConnect(NetworkPayload payload);

    void onSent(boolean result);

}
