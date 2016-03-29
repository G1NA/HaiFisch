package com.haifisch.server;

import com.haifisch.server.NetworkTools.ListeningSocket;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.utils.Questionaire;

import java.util.Scanner;

public class MainProgram implements onConnectionListener {


    private String name;
    private int port;
    private volatile static Thread listening_thread;
    private volatile static Thread butler;
    private volatile static Thread console;

    protected void toolsInit() {
        //Add a thread to watch over the listening socket
        butler = new Thread("butler") {
            public void run() {
                while (true) {
                    while (listening_thread.isAlive() && !listening_thread.isInterrupted())
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    createListeningSocket();
                }
            }
        };
        butler.start();

        console = new Thread("console") {
            public void run() {
                Scanner scan = Questionaire.scanner;
                while (true) {
                    System.out.print("type \'exit\' to close the mapper:\n");
                    String command = scan.nextLine();
                    command = command.trim();
                    if (command.equals("exit")) {
                        scan.close();
                        close();
                    }

                }
            }
        };
        console.start();
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    protected void createListeningSocket() {

        //if the listening thread crashed
        if (listening_thread != null) {

        }
        //Create the threads needed.
        ListeningSocket listener = new ListeningSocket(this);

        listening_thread = new Thread(listener);
        listening_thread.start();
        name = listener.getName();
        port = listener.getPort();
        System.out.println("The server is running on: " + name + ":" + port);
    }

    public static void close() {
        butler.interrupt();
        listening_thread.interrupt();
        console.interrupt();
        System.exit(1);
    }


    @Override
    public void onConnect(NetworkPayload payload) {

    }

    @Override
    public void onSent(boolean result) {

    }
}
