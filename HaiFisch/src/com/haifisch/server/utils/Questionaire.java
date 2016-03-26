package com.haifisch.server.utils;

import java.util.Scanner;

/** Class responsible for the initialization of the final variables. */
public class Questionaire {
	
	static Scanner scanner;
	
	final String masterServerName;
	final int masterServerPort;
	final String reducerName;
	final int reducerPort;
	final int topLeftCoordinateLongitude;
	final int topLeftCoordinateLatitude;
	final int bottomRightCoordinateLongtitude;
	final int bottomRightCoordinateLatitude;
	
	/**  
	*Set the master server 's name.
	*@return name
	*/
	private String setMasterServerName() {
		
		System.out.println("Insert master server name: \n");
		return scanner.nextLine();
	}
	
	/**  
	*Set the master server 's port.
	*@return port
	*/
	private int setMasterServerPort() {
	
		System.out.println("Insert master server port: \n");
		return scanner.nextInt();
	}
	
	/**  
	*Set the reducer 's name.
	*@return name
	*/
	private String setReducerName() {
		
		System.out.println("Insert reducer name: \n");
		return scanner.nextLine();
	}
	
	/**  
	*Set the reducer 's port.
	*@return port
	*/
	private int setReducerPort() {
	
		System.out.println("Insert reducer port: \n");
		return scanner.nextInt();
	}
	
	/**  
	*Set the top left point 's longtitude.
	*@return value
	*/
	private int setTopLeftLongtitude() {
		
		System.out.println("Enter top left point 's longtitude:\n");
		return scanner.nextInt();
	}
	
	/**  
	*Set the top left point 's latitude.
	*@return value
	*/
	private int setTopLeftLatitude() {
		
		System.out.println("Enter top left point 's latitude:\n");
		return scanner.nextInt();
	}
	
	/**  
	*Set the bottom right point 's longtitude.
	*@return value
	*/
	private int setBottomRightLongtitude() {
		
		System.out.println("Enter bottom right point 's longtitude:\n");
		return scanner.nextInt();
	}
	
	/**  
	*Set the bottom right point 's latitude.
	*@return value
	*/
	private int setBottomRightLatitude() {
		
		System.out.println("Enter bottom right point 's latitude:\n");
		return scanner.nextInt();
	}
	
	/** Constructor. */
	public Questionaire() {

		scanner = new Scanner(System.in);
		
		masterServerName = setMasterServerName();
		
		masterServerPort = setMasterServerPort();
		scanner.nextLine();
		
		reducerName = setReducerName();
		
		reducerPort = setReducerPort();
		scanner.nextLine();
		
		topLeftCoordinateLongitude = setTopLeftLongtitude();
		scanner.nextLine();
		
		topLeftCoordinateLatitude = setTopLeftLatitude();
		scanner.nextLine();
		
		bottomRightCoordinateLongtitude = setBottomRightLongtitude();
		scanner.nextLine();
		
		bottomRightCoordinateLatitude = setBottomRightLatitude();
		scanner.nextLine();
		
		scanner.close();
	}
}