package com.haifisch.server.NetworkTools;

import com.haifisch.server.NetworkTools.utils.Serialize;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SenderSocket implements Runnable {


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
        } catch (IOException e) {
            callback.onSent(false);
        }

    }
}
