package com.haifisch.server.NetworkTools;

import java.io.ObjectInputStream;
import java.net.Socket;

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
            ObjectInputStream incoming_data = new ObjectInputStream(clientSocket.getInputStream());

            NetworkPayload payload = (NetworkPayload) incoming_data.readObject();

            //close the connection
            clientSocket.close();
            System.out.println("Connection closed");

            if (payload.PAYLOAD_TYPE == NetworkPayloadType.STATUS_CHECK) {
                SenderSocket reply = new SenderSocket(payload.SENDER_NAME, payload.SENDER_PORT,
                        new NetworkPayload(NetworkPayloadType.STATUS_REPLY, false, null, clientSocket.getLocalSocketAddress().toString(),
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
