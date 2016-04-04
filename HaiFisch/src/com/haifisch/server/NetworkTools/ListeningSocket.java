package com.haifisch.server.NetworkTools;

import com.haifisch.server.utils.RandomString;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class ListeningSocket implements Runnable {
	
    private ServerSocket socket;
    private onConnectionListener callback;

    /**
     * Primary constructor with random port
     * @param callback The callback which will be used for request handling
     */
    public ListeningSocket(onConnectionListener callback) {
        socketInit(-1);
        this.callback = callback;

    }

    /**
     * Secondary constructor with set port
     * @param port The port required
     * @param callback The callback which will be used for request handling
     * @throws IOException Exception for port failure
     */
    public ListeningSocket(int port, onConnectionListener callback) throws IOException {
        if (!socketInit(port))
            if (socketInit(-1))
                throw new IOException();
        this.callback = callback;

    }

    /** GETTERS and SETTERS */
    public int getPort(){
        if(socket!=null)
            return socket.getLocalPort();
        else
            return -1;
    }

    public String getName(){
        if(socket!=null)
            try {
                return Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        else
            return null;
    }

    /**
     * Initiate the socket
     * @param port The port on which to be initiated
     * @return True for success
     */
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

    /**
     * Wait for connections and assign them to serving sockets
     */
    public void run() {
        while (true)
            try {
                new Thread(new ServingSocket(socket.accept(), callback), new RandomString(5).nextString()).start();
            } catch (IOException e) {
                e.printStackTrace();
                //Shit happened something will be called here to restart the damn thing
                break;
            }
    }
}
