package com.haifisch.server.utils;

import java.util.Scanner;

/**
 * Class responsible for the initialization of the final variables.
 */
public class Questionaire {

    public static Scanner scanner;

    //---> ta evala public alla mporoume na valoume kai getters
    public final String masterServerName;
    public final int masterServerPort;
    public final String reducerName;
    public final int reducerPort;
    public final double topLeftCoordinateLongitude;
    public final double topLeftCoordinateLatitude;
    public final double bottomRightCoordinateLongtitude;
    public final double bottomRightCoordinateLatitude;

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
     * Set the reducer 's name.
     *
     * @return name
     */
    private String setReducerName() {

        System.out.println("Insert reducer name:");
        return scanner.nextLine();
    }

    /**
     * Set the reducer 's port.
     *
     * @return port
     */
    private int setReducerPort() {

        System.out.println("Insert reducer port:");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    /**
     * Set the top left point 's longtitude.
     *
     * @return value
     */
    private Double setTopLeftLongtitude() {

        System.out.println("Enter top left point 's longtitude:");
        return Double.parseDouble(scanner.nextLine().trim());
    }

    /**
     * Set the top left point 's latitude.
     *
     * @return value
     */
    private Double setTopLeftLatitude() {

        System.out.println("Enter top left point 's latitude:");
        return Double.parseDouble(scanner.nextLine().trim());
    }


    /**
     * Set the bottom right point 's longtitude.
     *
     * @return value
     */
    private Double setBottomRightLongtitude() {

        System.out.println("Enter bottom right point 's longtitude:");
        return Double.parseDouble(scanner.nextLine().trim());
    }

    /**
     * Set the bottom right point 's latitude.
     *
     * @return value
     */
    private Double setBottomRightLatitude() {

        System.out.println("Enter bottom right point 's latitude:");
        return Double.parseDouble(scanner.nextLine().trim());
    }

    /**
     * Constructor.
     */
    public Questionaire() {

        scanner = new Scanner(System.in);

        masterServerName = setMasterServerName();
        masterServerPort = setMasterServerPort();
        reducerName = setReducerName();
        reducerPort = setReducerPort();
        topLeftCoordinateLongitude = setTopLeftLongtitude();

        topLeftCoordinateLatitude = setTopLeftLatitude();

        bottomRightCoordinateLongtitude = setBottomRightLongtitude();

        bottomRightCoordinateLatitude = setBottomRightLatitude();

    }
}