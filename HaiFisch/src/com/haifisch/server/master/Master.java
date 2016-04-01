package com.haifisch.server.master;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.RandomString;

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
                            Double cord = Double.parseDouble(scan.nextLine().trim());
                            if (cord == -1)
                                break;
                            else {
                                System.out.println("Type the longitude of the top left corner");
                                Double cord2 = Double.parseDouble(scan.nextLine().trim());
                                com.haifisch.server.utils.Point left = new com.haifisch.server.utils.Point(cord, cord2);
                                System.out.println("Type the latitude of the bottom right corner");
                                cord = Double.parseDouble(scan.nextLine().trim());
                                System.out.println("Type the longitude of the bottom right corner");
                                cord2 = Double.parseDouble(scan.nextLine().trim());
                                com.haifisch.server.utils.Point right = new com.haifisch.server.utils.Point(cord, cord2);
                                System.out.println("From when? Time format as dd/MM/yyyy HH:mm");
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                                Date date = null;
                                try {
                                    date = format.parse(scan.nextLine().trim());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Timestamp stampFrom = new Timestamp(date.getTime());
                                System.out.println("To when? Time format as dd/MM/yyyy HH:mm");
                                format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                                date = null;
                                try {
                                    date = format.parse(scan.nextLine().trim());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                scan.close();
                                Timestamp stampTo = new Timestamp(date.getTime());

                                CheckInRequest req;
                                int length = mappers.size();
                                double partSize = (right.longtitude - left.longtitude) / length;
                                for (int i = 0; i < length; i++) {
                                    com.haifisch.server.utils.Point trueLeft = new com.haifisch.server.utils.Point(left.longtitude + partSize * i, left.latitude);
                                    com.haifisch.server.utils.Point trueRight = new com.haifisch.server.utils.Point(left.longtitude + partSize * (i + 1), right.latitude);
                                    req = new CheckInRequest("None", trueLeft, trueRight, stampFrom,
                                            stampTo, new RandomString(5).nextString());
                                    SenderSocket socket = new SenderSocket(mappers.get(i).serverName,
                                            mappers.get(i).port,
                                            new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                                                    req, getName(), getPort(), 200, "show me the money"));
                                    socket.run();
                                    if (!socket.isSent())
                                        System.out.println("Failed to send request to: " + mappers.get(i).serverName);

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

}
