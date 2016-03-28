package com.haifisch.server.map;

import com.haifisch.server.utils.Questionaire;

import java.io.IOException;
import java.awt.EventQueue;

import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;
import com.haifisch.server.NetworkTools.ListeningSocket;
import com.haifisch.server.NetworkTools.onConnectionListener;


public class Map_Server {
	
	//---> dn ta ekana final giati dn ginetai na ta arxikopoiisei i main an einai final
	
	//----> arxikopoiountai apo to Listening Socket
	private static String name; 
	private static int port;
	private static Questionaire q;

	private static onConnectionListener callback;
	
	public static void main(String args[]) {

		//Create the object that will get all the input information from the user.
		q = new Questionaire();

		//Create the points on the map represented by the given coordinates.
		Point topLeftPoint = new Point (q.topLeftCoordinateLongitude, q.topLeftCoordinateLatitude);
		Point bottomRightPoint = new Point (q.bottomRightCoordinateLongtitude, q.bottomRightCoordinateLatitude);

		//Calculate the other two points of the map.
		Point bottomLeftPoint = new Point(topLeftPoint.longtitude, bottomRightPoint.latitude);
		Point topRightPoint = new Point(bottomRightPoint.longtitude, topLeftPoint.latitude);

		topRightPoint.print(); //DEBUG
		bottomLeftPoint.print(); //DEBUG

		//----> gia svisimo
		//Create a new configuration object for the master server.
		//Configuration MasterConfig = new Configuration(q.masterServerName, q.masterServerPort);

		//Create a new configuration object for all the mappers.
		MapperConfiguration MapperConfig = MapperConfiguration.getMapperConfiguration(q.masterServerName, q.masterServerPort, q.reducerName, q.reducerPort,
				topLeftPoint, bottomRightPoint);

		
		CreateListeningSocket(); //Create the listening socket.
		
		//WAITING IMPLEMENTATION....

		//create a listening socket --> Done.
		
		//serving socket results
		
		//manager (Request_Manager) thread activated 

		//END WAITING IMPLEMENTATION.

		//---> gia svisimo
		//Create a new configuration object for the reducer.
		//ReducerConfiguration ReducerConfig = new ReducerConfiguration();


	}
	
	//---> gia na mn mporei na ta "peira3ei" kapoios, afou dn ginetai na einai final, ta ekana private kai eftia3a getters.
	
	public static String getMapperName(){ return name; }
	
	public static int getMapperPort(){ return port; }
	
	private static void CreateListeningSocket() {
		
		try{
			
			//Create the threads needed.
			EventQueue.invokeLater(new ListeningSocket(q.masterServerPort, callback));
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		
	}
	

	

}

