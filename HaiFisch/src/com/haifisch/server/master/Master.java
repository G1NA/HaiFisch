package com.haifisch.server.master;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.*;
import com.haifisch.server.utils.Point;

import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

//Master server class, will be used for testing during the 1st phase
public class Master extends MainProgram implements onConnectionListener {

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
        createListeningSocket();
        toolsInit();
    }

    @Override

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
                Scanner scan = new Scanner(System.in);
                while (true) {
                    System.out.print("type \'exit\' to close the server " +
                            "or debug to begin debug sequence: \n");
                    String command = scan.nextLine();
                    command = command.trim();
                    if (command.equals("exit")) {
                        scan.close();
                        close();
                    } else if (command.equals("debug")) {
                        if (mappers.size() == 0 || reducer == null) {
                            System.err.println("No mappers or reducer present in the network!");
                        } else {
                            System.out.println("Write the latitude of the top left corner of the area you want to search");
                            //Double cord = Double.parseDouble(scan.nextLine().trim());
                            Double cord = Double.parseDouble("40");
                            if (cord == -1)
                                break;
                            else {
                                System.out.println("Type the longitude of the top left corner");
                                //Double cord2 = Double.parseDouble(scan.nextLine().trim());
                                Double cord2 = Double.parseDouble("-74.25");

                                Point left = new Point(cord2, cord);

                                System.out.println("Type the latitude of the bottom right corner");
                                // cord = Double.parseDouble(scan.nextLine().trim());
                                cord = Double.parseDouble("41");

                                System.out.println("Type the longitude of the bottom right corner");
                                // cord2 = Double.parseDouble(scan.nextLine().trim());
                                cord2 = Double.parseDouble("-73.7");
                                Point right = new com.haifisch.server.utils.Point(cord2, cord);

                                System.out.println("From when? Time format as dd/MM/yyyy HH:mm");
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                Date date = null;
                                try {
                                    // date = format.parse(scan.nextLine().trim());
                                    date = format.parse("01/04/2012 20:58");

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Timestamp stampFrom = new Timestamp(date.getTime());
                                System.out.println("To when? Time format as dd/MM/yyyy HH:mm");
                                format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                date = null;
                                try {
                                    //date = format.parse(scan.nextLine().trim());
                                    date = format.parse("01/05/2012 21:00");

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Timestamp stampTo = new Timestamp(date.getTime());

                                CheckInRequest req;
                                for (ConnectionAcknowledge mapper : mappers) {
                                    mapper.status = 2;
                                    checkAlive(mapper);
                                }

                                try {
                                    waitForAck();
                                } catch (InterruptedException e) {

                                }
                                String client_id = new RandomString(10).nextString();
                                //Will be changed later on

                                servingClients.put(client_id, new Client("void", 0, client_id,
                                        mappers.size(), new HashMap<>()));
                                Client serving = servingClients.get(client_id);
                                int length = mappers.size();
                                double partSize = (right.longtitude - left.longtitude) / length;
                                for (int i = 0; i < length; i++) {
                                    Point trueLeft = new Point(left.longtitude + partSize * i, left.latitude);
                                    Point trueRight = new Point(left.longtitude + partSize * (i + 1), right.latitude);
                                    req = new CheckInRequest(client_id, trueLeft, trueRight, stampFrom,
                                            stampTo);
                                    req.setTopK(100);
                                    SenderSocket socket = new SenderSocket(mappers.get(i).serverName,
                                            mappers.get(i).port,
                                            new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                                                    req, masterThread.getName(), getPort(), 200, "show me the money"));
                                    socket.run();
                                    if (!socket.isSent()) {
                                        System.err.println("Failed to send request to: " + mappers.get(i).serverName);
                                        break;
                                    } else {
                                        serving.addAssignment(mappers.get(i).serverName + ":" + mappers.get(i).port, req);
                                    }
                                }


                            }
                        }
                    }
                }
            }
        };
        console.start();
    }

    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload), "serving thread no" + threadCounter++).start();
    }

    private void checkAlive(ConnectionAcknowledge ack) {
        new Thread(
                new SenderSocket(ack.serverName, ack.port,
                        new NetworkPayload(NetworkPayloadType.STATUS_CHECK, false, null, getName(), getPort(), 200, "Dude u there?"))
        ).start();

    }

    synchronized private void waitForAck() throws InterruptedException {
        wait(1000);
        ArrayList<ConnectionAcknowledge> new_mappers = new ArrayList<>();
        mappers.forEach(e -> {
            if (e.status == 1)
                new_mappers.add(e);
        });
        mappers.clear();
        mappers = null;
        mappers = new_mappers;
    }
}
