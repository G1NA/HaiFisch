package com.haifisch.server.map;

import java.net.SocketAddress;

public interface onConnectionListener {

    public void onConnect(SocketAddress client_address);

    public void dataReceived(Object[] stream);
}
