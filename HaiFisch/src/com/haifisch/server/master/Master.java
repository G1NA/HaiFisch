package com.haifisch.server.master;

import com.haifisch.server.NetworkTools.ConnectionAcknowledge;
import com.haifisch.server.NetworkTools.ListeningSocket;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//Master server class, will be used for testing during the 1st phase
public class Master implements onConnectionListener {

    private static String server;
    private static int serverPort;
    private static volatile int threadCounter;
    static volatile ArrayList<ConnectionAcknowledge> mappers = new ArrayList<>();
    static volatile ConnectionAcknowledge reducer;
    static volatile HashMap<String, Client> servingClients = new HashMap<>();
    static volatile Master masterThread;

    public static void main(String[] args) {

        EventQueue.invokeLater(
                () -> masterThread = new Master()
        );
    }

    public Master() {
        mainSequence();
    }

    private void mainSequence() {
        ListeningSocket server = new ListeningSocket(this);
        new Thread(server, "listener").start();
        System.out.println("Server running on:" + server.getPort());
        Master.server = server.getName();
        Master.serverPort = server.getPort();
    }

    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        new Thread(new RequestHandler(payload), "serving thread no" + threadCounter++).start();
    }

    @Override
    synchronized public void onSent(boolean result) {

    }

    synchronized public static void actionLog(String output) {

        System.out.println(output);
    }

    synchronized static String getServerName() {
        return server;
    }

    synchronized public static int getPort() {
        return serverPort;
    }
}
