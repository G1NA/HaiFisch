package com.haifisch.server.NetworkTools;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A runnable sender socket. Sends network payload data through an output stream.
 */
public class SenderSocket implements Runnable {

    private String serverName;
    private int port;
    private NetworkPayload payload;
    private boolean sent = false;
    private String error;

    /**
     * Constructor
     *
     * @param serverName The destination name
     * @param port       The destination port
     * @param payload    The object to be sent
     */
    public SenderSocket(String serverName, int port, NetworkPayload payload) {
        this.payload = payload;
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket sender = new Socket(serverName, port);
            ObjectOutputStream stream = new ObjectOutputStream(sender.getOutputStream());
            stream.writeObject(payload);
            stream.close();
            sender.close();
            sent = true;
        } catch (IOException e) {
            error = e.getMessage();
        }
    }

    /**
     * @return if message was sent from sender socket through the stream.
     */
    public boolean isSent() {
        return sent;
    }

    /**
     * @return any error thrown.
     */
    public String getError() {
        return error;
    }
}
