package com.haifisch.server;

import commons.*;
import com.haifisch.server.network_tools.*;
import com.haifisch.server.utils.Questionaire;

import java.util.Scanner;

public class MainProgram implements onConnectionListener {


    private String name;
    private int port;
    private volatile Thread listening_thread;
    private volatile ListeningSocket listener;
    private volatile Thread butler;
    protected volatile Thread console;

    protected void initiateButler() {
        if (butler != null && (butler.isAlive() || !butler.isInterrupted()))
            return;
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
                    listener.cleanUp();
                    createListeningSocket();
                }
            }
        };
        butler.start();
    }

    protected void initiateConsole() {
        if (console != null && (console.isAlive() || !console.isInterrupted()))
            return;
        console = new Thread("console") {
            public void run() {
                Scanner scan = Questionaire.scanner;
                while (true) {
                    System.out.print("type \'exit\' to close the server:\n");
                    String command = scan.nextLine();
                    command = command.trim();
                    if (command.equals("exit")) {
                        scan.close();
                        close();
                        break;
                    }

                }
            }
        };
        console.start();
    }

    synchronized public String getName() {
        return name;
    }

    synchronized public int getPort() {
        return port;
    }

    protected void createListeningSocket() {

        //Create the threads needed.
        listener = new ListeningSocket(this);

        listening_thread = new Thread(listener);
        listening_thread.start();
        name = listener.getName();
        port = listener.getPort();
        System.out.println("The server is running on: " + name + ":" + port);
    }

    protected void connectToMaster(String name, int port, int type) {
        SenderSocket socket_ack = new SenderSocket(name, port,
                new NetworkPayload(NetworkPayloadType.CONNECTION_ACK, false, new ConnectionAcknowledge(type, getName(),
                        getPort()), getName(), getPort(), 200, "connected"));
        socket_ack.run();
        if (!socket_ack.isSent()) {
            System.err.println("Failed to send Connection Acknowledge to master_node server!\nClosing mapper!");
            close();
        }
    }

    public void close() {
        if (butler != null)
            butler.interrupt();
        if (listening_thread != null) {
            listening_thread.interrupt();
            listener.cleanUp();
        }
        if (console != null)
            console.interrupt();
        System.exit(1);
    }


    @Override
    public void onConnect(NetworkPayload payload) {

    }
}
