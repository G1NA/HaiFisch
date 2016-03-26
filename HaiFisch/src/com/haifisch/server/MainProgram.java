package com.haifisch.server;

import com.haifisch.server.map.MapperConfiguration;
import com.haifisch.server.utils.Point;
import com.haifisch.server.utils.Configuration;


public class MainProgram {

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
		
		//Create a new configuration object for the master server.
		Configuration MasterConfig = new Configuration(q.masterServerName, q.masterServerPort);
		
		//Create a new configuration object for all the mappers.
		MapperConfiguration MapperConfig = new 
		MapperConfiguration(q.masterServerName, q.masterServerPort, q.reducerName, q.reducerPort, 
		topLeftPoint, bottomRightPoint);
		
		
		//WAITING IMPLEMENTATION....
		
			//Create a new configuration object for the reducer.
			//ReducerConfiguration ReducerConfig = new ReducerConfiguration();
		
		//END WAITING IMPLEMENTATION.
		
		
		
		

	}

}