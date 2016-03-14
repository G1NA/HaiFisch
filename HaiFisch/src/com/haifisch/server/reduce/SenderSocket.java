package com.haifisch.server.reduce;

import com.haifisch.server.map.utils.Serialize;

import java.io.DataOutputStream;
import java.net.Socket;

public class SenderSocket extends Thread {


    private onConnectionListener callback;
    private String serverName;
    private int port;
    private NetworkPayload payload;

    public SenderSocket(String serverName, int port, NetworkPayload payload, onConnectionListener callback) {
        this.callback = callback;
        this.payload = payload;
        this.serverName = serverName;
        this.port = port;
    }

    public void run() {
        try {
            Socket sender = new Socket(serverName, port);
            new DataOutputStream(sender.getOutputStream()).write(Serialize.serialize(payload));
            sender.close();
            callback.onSent(true);
        } catch (Exception e) {
            callback.onSent(false);
        }

    }
}
