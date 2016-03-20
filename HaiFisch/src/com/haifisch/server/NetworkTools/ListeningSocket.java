package com.haifisch.server.NetworkTools;

import java.io.IOException;
import java.net.ServerSocket;

public class ListeningSocket implements Runnable {
    private ServerSocket socket;
    private onConnectionListener callback;

    //Initiate the "listening" socket on any port available
    public ListeningSocket(onConnectionListener callback) {
        socketInit(-1);
        this.callback = callback;

    }

    //Iniate the "listening" socket on a specific port
    public ListeningSocket(int port, onConnectionListener callback) throws IOException {
        if (!socketInit(port))
            if (socketInit(-1))
                throw new IOException();
        this.callback = callback;

    }

    public int getPort(){
        if(socket!=null)
            return socket.getLocalPort();
        else
            return -1;
    }

    public String getName(){
        if(socket!=null)
            return socket.getInetAddress().toString();
        else
            return null;
    }

    private boolean socketInit(int port) {
        int socket_port;
        if (port == -1)
            socket_port = 0;
        else
            socket_port = port;

        try {
            socket = new ServerSocket(socket_port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Start listening and for each connection create a new thread to handle it
    public void run() {
        while (true)
            try {
                new ServingSocket(socket.accept(), callback);
            } catch (IOException e) {
                e.printStackTrace();
                //Shit happened something will be called here to restart the damn thing
                break;
            }
    }
}
