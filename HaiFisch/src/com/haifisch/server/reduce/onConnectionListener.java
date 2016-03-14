package com.haifisch.server.reduce;

public interface onConnectionListener {


    void onConnect(NetworkPayload payload);

    void onSent(boolean result);

}
