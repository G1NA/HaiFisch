package com.haifisch.server.reduce;

import java.util.ArrayList;
import java.util.HashMap;

import com.haifisch.server.NetworkTools.onConnectionListener;
import com.haifisch.server.master.Client;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;
import com.haifisch.server.utils.Questionaire;

import com.haifisch.server.utils.*;

public class Reduce_Server {
	
	//---> dn ta ekana final giati dn ginetai na ta arxikopoiisei i main an einai final
	
	//----> arxikopoiountai apo to Listening Socket
	private static String name; 
	private static int port;
	private static Questionaire q;
	private static Configuration MasterConfig;

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
		
		//Create a new configuration object for the master server.
		MasterConfig = new Configuration(q.masterServerName, q.masterServerPort);
		
		
		//---> 8a mporouse na einai static 3r gw..
		//--> to xrisimopoioun oi handlers gia na ikanopoioun ta aitimata
		HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>> requests = new HashMap<String, ArrayList<CheckInMap<String, PointOfInterest>>>();
		
		//WAITING IMPLEMENTATION....
		
			//Create a new configuration object for the reducer.
			//ReducerConfiguration ReducerConfig = new ReducerConfiguration();
		
		//END WAITING IMPLEMENTATION.
		
		
		
		

	}
	

	public static String getReducerName(){ return name; }
	
	public static int getReducerPort(){ return port; }
	
	public static Configuration getMaster(){ return MasterConfig; }

}