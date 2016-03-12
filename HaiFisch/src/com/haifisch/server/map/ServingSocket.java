package com.haifisch.server.map;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServingSocket extends Thread {

    private Socket clientSocket;
    private onConnectionListener callback;

    public ServingSocket(Socket clientSocket, onConnectionListener callback) {
        this.clientSocket = clientSocket;
        this.callback = callback;
    }

    public void run() {
        try {
            InputStream incoming = clientSocket.getInputStream();
            DataInputStream incoming_data = new DataInputStream(incoming);
            byte b = incoming_data.readByte();
            List<Byte> blist = new ArrayList<>();
            while (b != 0) {
                blist.add(b);
                b = incoming_data.readByte();
            }
            callback.onConnect(blist.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
