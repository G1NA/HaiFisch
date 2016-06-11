package com.haifisch.client;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import commons.NetworkPayload;
import commons.onConnectionListener;


public class Communicator extends AsyncTask {

    private ServerSocket serverSocket;
    public boolean created = false;
    public static volatile onConnectionListener listener;
    public static String address;
    public static int port;

    public Communicator(onConnectionListener listener) {
        Communicator.listener = listener;

    }

    synchronized public void setConnectionListener(onConnectionListener listener) {
        Communicator.listener = listener;
    }

    public String getName() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    protected Void doInBackground(Object... params) {
        try {
            serverSocket = new ServerSocket(0);
            created = true;
            address = getName();
            port = getPort();

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                listener.onConnect(
                        (NetworkPayload) new ObjectInputStream(socket.getInputStream()).readObject());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
