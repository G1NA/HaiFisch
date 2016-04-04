package com.haifisch.server.map;

import com.haifisch.server.MainProgram;
import com.haifisch.server.NetworkTools.NetworkPayload;
import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.utils.Questionaire;

/** Basic class responsible for the actions executed by the server node. */
public class Map_Server extends MainProgram implements onConnectionListener {

    public volatile static Map_Server server;
    private final MapperConfiguration configuration;

   /** 
     * The main method of the server node class. 
     * Responsible for the initialization of the server configuration.
    */
    public static void main(String args[]) {

        //Create the object that will get all the input information from the user.
        Questionaire q = new Questionaire();

        //Create a new configuration object for all the mappers.
        MapperConfiguration mapperConfig = MapperConfiguration.getMapperConfiguration(q.masterServerName,
                q.masterServerPort);
        server = new Map_Server(mapperConfig);
        server.toolsInit();

    }
    
   /** 
	 * Constructor.
	 * Creates a listening socket and connects to the master server.
	 * @param configuration	The configuration of the mapper asking to connect with the server.
	 */
    private Map_Server(MapperConfiguration configuration) {
        this.configuration = configuration;
        createListeningSocket(); //Create the listening socket.
        connectToMaster(configuration.masterServerName, configuration.masterServerPort, 1);
    }

   /** 
     * Creates a new thread for the request handler.
     * @param payload A payload that contains the basic characteristics of the connection. 
     */
    @Override
    synchronized public void onConnect(NetworkPayload payload) {
        System.out.println("Serving new request from: " + payload.SENDER_NAME);
        new Thread(new RequestHandler(payload)).start();
    }
    
    /** Getter method. Returns the configuration of the mapper. */
    synchronized public MapperConfiguration getConfiguration() {
        return configuration;
    }
}
