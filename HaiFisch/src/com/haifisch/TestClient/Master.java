package com.haifisch.TestClient;

import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.utils.Point;
import com.haifisch.server.utils.RandomString;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

//Master server class, will be used for testing during the 1st phase
public class Master implements onConnectionListener {

    private static String server;
    private static int serverPort;
    private static int threadCounter;
    private static HashMap<ConnectionAcknowledge, Point[]> mappers = new HashMap<>();
    private static ConnectionAcknowledge reducer;
    static volatile Master masterThread;

    public static void main(String[] args) {
        //retrieve from config the data
        try {
            mappers = Config.getInstance().getMapperData();
            reducer = Config.getInstance().getReducerData();
        } catch (IOException e) {
            System.out.println("Well....shit");
            e.printStackTrace();
            return;
        }

        //TODO will be changed
        ListeningSocket server = new ListeningSocket(null);
        new Thread(server, "listener").start();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Type -1 to exit or \n " +
                    "write the latitude of the top left corner of the area you want to search");
            Double cord = scan.nextDouble();
            if (cord == -1)
                break;
            else {
                System.out.println("Type the longitude of the top left corner");
                Double cord2 = scan.nextDouble();
                Point left = new Point(cord, cord2);
                System.out.println("Type the latitude of the bottom right corner");
                cord = scan.nextDouble();
                System.out.println("Type the longitude of the bottom right corner");
                cord2 = scan.nextDouble();
                Point right = new Point(cord, cord2);
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
                ConnectionAcknowledge[] mapper = (ConnectionAcknowledge[]) mappers.keySet().toArray();
                int length = mapper.length;
                double partSize = (right.longtitude - left.longtitude) / length;
                for (int i = 0; i < length; i++) {
                    Point trueLeft = new Point(left.longtitude + partSize * i, left.latitude);
                    Point trueRight = new Point(left.longtitude + partSize * (i + 1), right.latitude);
                    req = new CheckInRequest("None", trueLeft, trueRight, stampFrom,
                            stampTo, new RandomString(5).nextString());
                    SenderSocket socket = new SenderSocket(mapper[i].serverName,
                            mapper[i].port,
                            new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                                    req, server.getName(), server.getPort(), 200, "show me the money"));
                    socket.run();
                    if (!socket.isSent())
                        System.out.println("Failed to send request to: " + mapper[i].serverName);

                }

            }
        }


    }


    synchronized static String getServerName() {
        return server;
    }

    synchronized public static int getPort() {
        return serverPort;
    }

    @Override
    public void onConnect(NetworkPayload payload) {
        if (payload.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS && payload.payload == null)
            System.out.println("Mapper at: "
                    + payload.SENDER_NAME + ":" + payload.SENDER_PORT + " finished processing request");
        else if (payload.PAYLOAD_TYPE == NetworkPayloadType.CHECK_IN_RESULTS) {
            System.out.println("Found " + ((CheckInRes) payload.payload).getRequest_id() + " check ins at given area");
        }
    }
}
