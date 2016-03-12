package com.haifisch.server.reduce;

import java.net.SocketAddress;

public interface onConnectionListener {

    public void onConnect(SocketAddress client_address);

    public void dataReceived(Object[] stream);
}
