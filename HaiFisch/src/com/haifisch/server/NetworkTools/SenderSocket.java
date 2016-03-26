package com.haifisch.server.NetworkTools;

import com.haifisch.server.NetworkTools.utils.Serialize;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SenderSocket implements Runnable {


    private String serverName;
    private int port;
    private NetworkPayload payload;
    private boolean sent = false;
    private String error;

    public SenderSocket(String serverName, int port, NetworkPayload payload) {
        this.payload = payload;
        this.serverName = serverName;
        this.port = port;
    }

    public void run() {
    	sent = false; //---> gia epanaxrisimopoiisi tou idiou SenderSocket opws ston RequestHandler tou Mapper
        try {
            Socket sender = new Socket(serverName, port);
            new DataOutputStream(sender.getOutputStream()).write(Serialize.serialize(payload));
            sender.close();
            sent = true;
        } catch (IOException e) {
            error = e.getMessage();
        }

    }

    public boolean isSent() {
        return sent;
    }

    public String getError() {
        return error;
    }
}
