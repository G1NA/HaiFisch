package com.haifisch.server.network_tools;

import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.Socket;

/**
 * A runnable serving socket.
 * <p>
 * It receives data (network payload) from a client socket through an input stream, initiates a sender socket to reply
 * if it is about a status reply, and sends the data to a callback.
 */
public class ServingSocket implements Runnable {

    private Socket clientSocket;
    private onConnectionListener callback;

    /**
     * Constructor
     *
     * @param clientSocket The client connection
     * @param callback     The callback which will be used to handle the request
     */
    public ServingSocket(Socket clientSocket, onConnectionListener callback) {
        this.clientSocket = clientSocket;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            //Read incoming data
            ObjectInputStream incomingData = new ObjectInputStream(clientSocket.getInputStream());

            NetworkPayload payload = (NetworkPayload) incomingData.readObject();

            //close the connection
            clientSocket.close();

            if (payload.PAYLOAD_TYPE == NetworkPayloadType.STATUS_CHECK) {
                SenderSocket reply = new SenderSocket(payload.SENDER_NAME, payload.SENDER_PORT,
                        new NetworkPayload(NetworkPayloadType.STATUS_REPLY, false, null,
                                Inet4Address.getLocalHost().getHostAddress(), clientSocket.getLocalPort(), 200, "Alive and kicking"));
                reply.run();
                return;
            }

            //Send it to the callback
            callback.onConnect(payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
