package com.haifisch.server.NetworkTools;

import com.haifisch.server.NetworkTools.utils.Serialize;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServingSocket implements Runnable {

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
            byte b;
            List<Byte> blist = new ArrayList<>();
            while ((b = incoming_data.readByte()) > 0)
                blist.add(b);

            //close the connection
            clientSocket.close();
            System.out.println("Connection closed");

            byte[] array = new byte[blist.size()];
            for (int i = 0; i < blist.size(); i++)
                array[i] = blist.get(i);

            //Send it to the callback
            callback.onConnect((NetworkPayload) Serialize.deserialize(array));

        } catch (Exception e) {
            e.printStackTrace();
            //Shit happened something will be called here to restart the damn thing
        }

    }
}
