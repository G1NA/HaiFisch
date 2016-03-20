package com.haifisch.server;

import java.util.Scanner;


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
		
		
		
		
		


	}


}