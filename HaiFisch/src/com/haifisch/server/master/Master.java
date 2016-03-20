package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.ConAcknowledge;
import com.haifisch.server.NetworkTools.ListeningSocket;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;

import java.awt.*;
import java.util.ArrayList;

//Master server class, will be used for testing during the 1st phase
public class Master implements onConnectionListener {

    private static String server;
    private static int serverPort;
    public static ArrayList<ConAcknowledge> mappers = new ArrayList();
    public static ConAcknowledge reducer;

    public static void main(String[] args) {



        EventQueue.invokeLater(
                Master::new
        );
    }

    public Master() {
        mainSequence();
    }

    public void mainSequence() {
        ListeningSocket server = new ListeningSocket(null);
        server.run();
        System.out.println("Server running on:" + server.getPort());
    }

    @Override
    public void onConnect(NetworkPayload payload) {
        //Get the connections coming from mappers and reducers that add themselves to the mapper pool
        if (payload.payload instanceof ConAcknowledge) {
            ConAcknowledge connected = (ConAcknowledge) payload.payload;
            if (connected.TYPE == 1)
                mappers.add(connected);
            else
                reducer = connected;
        }
        else{
            //Do other stuff

            //If there are no mappers or reducer the request should return an error
            if(mappers.size() == 0 || reducer == null){

            }
        }
    }

    @Override
    public void onSent(boolean result) {

    }
}
