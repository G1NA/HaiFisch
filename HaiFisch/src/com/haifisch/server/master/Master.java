package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//Master server class, will be used for testing during the 1st phase
public class Master implements onConnectionListener {

    private static String server;
    private static int serverPort;
    public static ArrayList<ConAcknowledge> mappers = new ArrayList();
    public static ConAcknowledge reducer;
    public static HashMap<String, String> servingClients = new HashMap<>();

    public static void main(String[] args) {


        EventQueue.invokeLater(
                Master::new
        );
    }

    public Master() {
        mainSequence();
    }

    public void mainSequence() {
        ListeningSocket server = new ListeningSocket(this);
        new Thread(server, "listener").start();
        System.out.println("Server running on:" + server.getPort());
    }

    @Override
    public void onConnect(NetworkPayload payload) {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
        if (payload.payload == null) {
            //handle empty payload
        } else if (payload.payload instanceof ConAcknowledge) {
            ConAcknowledge connected = (ConAcknowledge) payload.payload;
            if (connected.TYPE == 1)
                mappers.add(connected);
            else
                reducer = connected;
        } else if (payload.payload instanceof CheckInRequest) {
            //If there are no mappers or reducer the request should return an error
            if (mappers.size() == 0 || reducer == null) {
                SenderSocket send = new SenderSocket(payload.SENDER_NAME, payload.SENDER_PORT,
                        new NetworkPayload(3, false, null,
                                server, serverPort, 400, "No mappers or reducer present in the network")
                        , this);
                new Thread(send, "sender").start();
            } else {
                new Thread("serving") {
                    // break down the request to send it to the mappers
                }.start();

            }
        } else if (payload.payload instanceof CheckInRes) {
            //Return the result to the client that requested it

        }
    }

    @Override
    public void onSent(boolean result) {

    }
}
