package com.haifisch.server.utils;

import java.util.Scanner;

/**
 * Class responsible for the initialization of the final variables.
 */
public class Questionaire {

    public static Scanner scanner;

    public final String masterServerName;
    public final int masterServerPort;

    /**
     * Set the master server 's name.
     *
     * @return name
     */
    private String setMasterServerName() {

        System.out.println("Insert master server name:");
        return scanner.nextLine();
    }

    /**
     * Set the master server 's port.
     *
     * @return port
     */
    private int setMasterServerPort() {

        System.out.println("Insert master server port: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    /**
     * Constructor.
     */
    public Questionaire() {

        scanner = new Scanner(System.in);

        masterServerName = setMasterServerName();
        masterServerPort = setMasterServerPort();

    }
}