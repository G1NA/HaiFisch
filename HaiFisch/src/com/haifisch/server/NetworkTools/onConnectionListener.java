package com.haifisch.server.NetworkTools;

public interface onConnectionListener {


    void onConnect(NetworkPayload payload);

    void onSent(boolean result);

}
