package com.haifisch.server.map;

import com.haifisch.server.map.utils.Serialize;

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
            //Read incoming data
            InputStream incoming = clientSocket.getInputStream();
            DataInputStream incoming_data = new DataInputStream(incoming);
            byte b = incoming_data.readByte();
            List<Byte> blist = new ArrayList<>();
            while (b != 0) {
                blist.add(b);
                b = incoming_data.readByte();
            }

            //close the connection
            clientSocket.close();
            System.out.println("Connection closed");

            byte[] array = new byte[blist.size()];
            for (int i = 0; i < blist.size(); i++) {
                array[i] = blist.get(i);
            }

            //Send it to the callback
            callback.onConnect((NetworkPayload) Serialize.deserialize(array));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
