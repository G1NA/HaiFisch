package com.haifisch.server.map;

import com.haifisch.server.utils.Questionaire;
import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;


public class Map_Server {
	
	//---> dn ta ekana final giati dn ginetai na ta arxikopoiisei i main an einai final
	
	//----> arxikopoiountai apo to Listening Socket
	private static String name; 
	private static int port;

	public static void main(String args[]) {

		//Create the object that will get all the input information from the user.
		Questionaire q = new Questionaire();

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


		//WAITING IMPLEMENTATION....

		//create a listening socket
		
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

}

