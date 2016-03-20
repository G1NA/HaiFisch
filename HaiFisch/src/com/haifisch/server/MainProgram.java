package com.haifisch.server;

import java.util.Scanner;

import com.haifisch.server.map.Point;
import com.haifisch.server.master //For the master server.
import com.haifisch.server.map //For the mappers.
import com.haifisch.server.reduce //For the reducer.
import com.haifisch.server.datamanagement //For the database manager.


/** This class is the main program class that is responsible for the flow of the program. */

public class MainProgram {

	
	/** The main method of the program. */
	public static void main (String[] args) {
		
		// To read the input from the user.
		Scanner scanner = new Scanner(System.in);
		
		// Initialize the name of the master server.
		System.out.println("Insert master server name: \n");
		final String masterServerName = scanner.nextLine();
		System.out.println();
		
		// Initialize the port of the master server.
		System.out.println("Insert master server port: \n");
		final short masterServerPort = scanner.nextShort();
		scanner.nextLine(); //Without this, bugs occur... 
		System.out.println();
		
		// Initialize the name of the reducer.
		System.out.println("Insert reducer name: \n");
		final String reducerName = scanner.nextLine();
		System.out.println();
		
		// Initialize the port of the reducer.
		System.out.println("Insert reducer port: \n");
		final short reducerPort = scanner.nextShort();
		scanner.nextLine();
		System.out.println();
		
		// How many mappers?
		System.out.println("Insert mappers' number: \n");
		final short numberOfMappers = scanner.nextShort();
		scanner.nextLine();
		System.out.println();
		
		//Consider that we have only one chekin request.
		System.out.println("Insert the checkin request. Enter area name:\n");
		final String areaName = scanner.nextLine();
		System.out.println();
		
		System.out.println("Enter top left point 's coordinates:\n");
		final int topLeftCoordinateLongitude = scanner.nextInt();
		scanner.nextLine();
		final int topLeftCoordinateLatitude = scanner.nextInt();
		scanner.nextLine();
		System.out.println();
		
		System.out.println("Enter bottom right point 's coordinates:\n");
		final int bottomRightCoordinateLongtitude = scanner.nextInt();
		scanner.nextLine();
		final int bottomRightCoordinateLatitude = scanner.nextInt();
		scanner.nextLine();
		System.out.println();
		
		//Create the points on the map represented by the given coordinates.
		Point topLeftPoint = new Point (topLeftCoordinateLongitude, topLeftCoordinateLatitude);
		Point bottomRightPoint = new Point (bottomRightCoordinateLongtitude, bottomRightCoordinateLatitude);
		
		//Calculate the other two points of the map.
		Point bottomLeftPoint = new Point(topLeftPoint.longtitude, bottomRightPoint.latitude);
		Point topRightPoint = new Point(bottomRightPoint.longtitude, topLeftPoint.latitude);
		
		topRightPoint.print(); //DEBUG
		bottomLeftPoint.print(); //DEBUG
		
		
		//Coming soon --> Time to start initializing the slaves!
		
		


	}


}