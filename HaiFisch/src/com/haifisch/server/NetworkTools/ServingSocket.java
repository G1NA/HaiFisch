package com.haifisch.server.NetworkTools;

import com.haifisch.server.NetworkTools.utils.Serialize;

import java.io.DataInputStream;
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
            DataInputStream incoming_data = new DataInputStream(clientSocket.getInputStream());
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

            NetworkPayload payload = (NetworkPayload) Serialize.deserialize(array);
            if (payload.PAYLOAD_TYPE == 4) {
                SenderSocket reply = new SenderSocket(payload.SENDER_NAME, payload.SENDER_PORT,
                        new NetworkPayload(4, false, null, clientSocket.getLocalSocketAddress().toString(),
                                clientSocket.getLocalPort(), 200, "Alive and kicking"));
                reply.run();
                return;
            }

            //Send it to the callback
            callback.onConnect(payload);

        } catch (Exception e) {
            e.printStackTrace();
            //Shit happened something will be called here to restart the damn thing
        }

    }
}
