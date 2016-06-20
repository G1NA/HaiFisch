package com.haifisch.server.master_node;

import commons.*;
import com.haifisch.server.MainProgram;
import com.haifisch.server.utils.*;
import commons.Point;

import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

//Master server class, will be used for testing during the 1st phase
public class Master extends MainProgram{

    private static volatile int threadCounter;
    static volatile ArrayList<ConnectionAcknowledge> mappers = new ArrayList<>();
    static volatile ConnectionAcknowledge reducer;
    static volatile HashMap<String, Client> servingClients = new HashMap<>();
    static volatile Master masterThread;

    /**
     * The master_node server main
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(
                () -> masterThread = new Master()
        );
    }

    /**
     * Master server constructor
     */
    private Master() {
        mainSequence();
    }

    /**
     * The main sequence which initiates the listening socket,
     * the listening socket "butler" and the console listener
     */
    private void mainSequence() {
        createListeningSocket();
        initiateButler();
        initiateConsole();
    }

    /**
     * Initiate the console listener
     * Currently different from the one on the MainProgram parent
     */
    @Override
    protected void initiateConsole() {
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
                        break;
                    } else if (command.equals("debug")) {
                        debug();
                    }
                }
            }
        };
        console.start();
    }

    /**
     * On connect listener
     *
     * @param payload the data transmitted
     */
    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME + ":" + payload.SENDER_PORT);
        new Thread(new RequestHandler(payload), "serving thread no" + threadCounter++).start();
    }

    /**
     * Check if the mappers identified as well as the reducer are still online
     * This check happens before every new checkin request
     *
     * @return true if there is at least one mapper and a reducer
     */
    private boolean checkAlive() {
        for (ConnectionAcknowledge mapper : mappers) {
            mapper.status = 2;
            new Thread(
                    new SenderSocket(mapper.serverName, mapper.port,
                            new NetworkPayload(NetworkPayloadType.STATUS_CHECK, false, null, getName(), getPort(), 200, "Status check"))
            ).start();
        }
        reducer.status = 2;
        new Thread(
                new SenderSocket(reducer.serverName, reducer.port,
                        new NetworkPayload(NetworkPayloadType.STATUS_CHECK, false, null, getName(), getPort(), 200, "Status check"))
        ).start();

        try {
            waitForAck();
            return reducer != null && mappers.size() != 0;
        } catch (InterruptedException e) {
            System.err.println("Check status operation interrupted!");
            return false;
        }
    }

    /**
     * Wait 1s to receive answer from the nodes. If no answer is received they are removed
     * The time may be changed later on, it is currently adequate
     *
     * @throws InterruptedException
     */
    synchronized private void waitForAck() throws InterruptedException {
        wait(1000);
        mappers = (ArrayList<ConnectionAcknowledge>) mappers.stream()
                .filter(e -> e.status == 1).collect(Collectors.toList());
        if (reducer.status != 1)
            reducer = null;
    }

    /**
     * The debug sequence for the 1st part testing
     * The commented out parts can be used for dynamic query input
     */
    private void debug() {
        if (mappers.size() == 0 || reducer == null) {
            System.err.println("No mappers or reducer present in the network!");
        } else {
            System.out.println("Write the latitude of the top left corner of the area you want to search");
            //Double cord = Double.parseDouble(scan.nextLine().trim());
            Double cord = Double.parseDouble("40");

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
            Point right = new Point(cord2, cord);

            System.out.println("From when? Time format as dd/MM/yyyy HH:mm");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date date;
            try {
                // date = format.parse(scan.nextLine().trim());
                date = format.parse("01/04/2012 20:58");

            } catch (ParseException e) {
                System.out.println("Wrong date format!");
                return;
            }
            assert date != null;
            Timestamp stampFrom = new Timestamp(date.getTime());
            System.out.println("To when? Time format as dd/MM/yyyy HH:mm");
            format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                //date = format.parse(scan.nextLine().trim());
                date = format.parse("01/05/2012 21:00");

            } catch (ParseException e) {
                System.out.println("Wrong date format!");
                return;
            }

            assert date != null;
            Timestamp stampTo = new Timestamp(date.getTime());

            //status check
            if (!checkAlive()) {
                System.err.println("No mappers or reducer present in the network!");
                return;
            }

            CheckInRequest req;
            String client_id = new RandomString(10).nextString();
            //Will be changed later on
            servingClients.put(client_id, new Client("void", 0, client_id,
                    mappers.size(), new HashMap<>()));
            Client serving = servingClients.get(client_id);
            int length = mappers.size();
            double partSize = (right.longtitude - left.longtitude) / length;
            Point trueLeft;
            Point trueRight;
            SenderSocket socket;
            for (int i = 0; i < length; i++) {
                trueLeft = new Point(left.longtitude + partSize * i, left.latitude);
                trueRight = new Point(left.longtitude + partSize * (i + 1), right.latitude);
                req = new CheckInRequest(client_id, length, trueLeft, trueRight, stampFrom,
                        stampTo);
                req.setTopK(100);
                socket = new SenderSocket(mappers.get(i).serverName,
                        mappers.get(i).port,
                        new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                                req, masterThread.getName(), getPort(), 200, "Incoming request"));
                socket.run();
                if (!socket.isSent()) {
                    System.err.println("Failed to send request to: " + mappers.get(i).serverName);
                    break;
                } else
                    serving.addAssignment(req, mappers.get(i).serverName + ":" + mappers.get(i).port);

            }

        }
    }

}
