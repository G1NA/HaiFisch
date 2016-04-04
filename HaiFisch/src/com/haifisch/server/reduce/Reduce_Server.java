package com.haifisch.server.reduce;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.CheckInRes;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.PointOfInterest;
import com.haifisch.server.utils.Questionaire;

import java.util.ArrayList;
import java.util.HashMap;


public class Reduce_Server extends MainProgram {


    private final Configuration configuration;
    public volatile static Reduce_Server server;
    private static volatile HashMap<String, Results> requests = new HashMap<>();

    /**
     * The reduce server node
     * Retrieve the master details from the admin initiate butler and console.
     *
     * @param args
     */
    public static void main(String args[]) {


        //Create the object that will get all the input information from the user.
        Questionaire q = new Questionaire();

        //Create a new configuration object for all the mappers.
        Configuration config = new Configuration(q.masterServerName, q.masterServerPort);
        server = new Reduce_Server(config);
        server.initiateButler();
        server.initiateConsole();
    }

    /**
     * Set the configuration for the server, create a listener and
     * inform the master node for its existence
     *
     * @param configuration The node configuration
     */
    private Reduce_Server(Configuration configuration) {
        this.configuration = configuration;
        createListeningSocket(); //Create the listening socket.
        connectToMaster(configuration.masterServerName, configuration.masterServerPort, 2);
    }


    /**
     * On connection listener
     *
     * @param payload the data transmitted
     */
    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload)).start();
    }

    /**
     * Get the server configuration
     *
     * @return The configuration
     */
    synchronized Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Get the stored data for a request
     *
     * @param id The request id
     * @return The data stored
     */
    synchronized static ArrayList<HashMap<String, PointOfInterest>> getData(String id) {
        return requests.get(id).getResults();
    }

    /**
     * Store new data for a request
     *
     * @param id  The request id
     * @param map The data to be stored
     */
    synchronized static void putData(String id, HashMap<String, PointOfInterest> map) {
        if (!requests.containsKey(id))
            requests.put(id, new Results());
        requests.get(id).add(map);
    }

    /**
     * Set miscellaneous information about the request
     *
     * @param id  The request id
     * @param res he request packet containing the information
     */
    synchronized static void setMisc(String id, CheckInRes res) {
        Results results = requests.get(id);
        results.setTopK(res.getTopK());
        results.setMappers(res.getMapperCount());
    }

    /**
     * Increment the completed mappers for a request
     *
     * @param id The request id that the mapper was handling
     */
    synchronized static void mapperDone(String id) {
        requests.get(id).mapperCompleted();
    }

    /**
     * Check if a request is done
     *
     * @param id The request id to check
     * @return true if done
     */
    synchronized static boolean isDone(String id) {
        return requests.get(id).isDone();
    }

    /**
     * Get the topK variable for a request
     *
     * @param id The request id
     * @return return the variable
     */
    synchronized static int getTopK(String id) {
        return requests.get(id).getTopK();
    }

    /**
     * Remove the data since the request has been served
     *
     * @param id The request id
     */
    synchronized static void removeData(String id) {
        requests.remove(id);
    }
}

