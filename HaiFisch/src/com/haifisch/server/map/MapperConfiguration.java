package com.haifisch.server.map;

import com.haifisch.server.utils.Configuration;
import com.haifisch.server.utils.Point;

public class MapperConfiguration extends Configuration {
	
	private static MapperConfiguration configInstance;
	
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	
	public String name; //mapper's name
	public int port; // mapper's port
    public String reducerName;
    public int reducerPort;
    public Point left;
    public Point right;
    
    public static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort,
            Point left, Point right){
    	if(configInstance == null){
    		configInstance = new MapperConfiguration(masterServerName, masterServerPort, reducerName, reducerPort,left, right);
    	}else{
    		configInstance.setReducer(reducerName, reducerPort);
    		configInstance.setPoint(LEFT, left);
    		configInstance.setPoint(RIGHT, right);
    	}
    	return configInstance;
    }
    
    public static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort){
    	if(configInstance == null){
    		configInstance = new MapperConfiguration(masterServerName, masterServerPort, reducerName, reducerPort);
    	}else{
    		configInstance.setReducer(reducerName, reducerPort);
    	}
    	return configInstance;
    }
    
    public static MapperConfiguration getMapperConfiguration(String masterServerName, int masterServerPort){
    	if(configInstance == null){
    		configInstance = new MapperConfiguration(masterServerName, masterServerPort);
    	}
    	//----> kanonika an iparxei to instance idi prepei na dinei error 
    	//alla tespa dn evala kati giati 8ewrw oti dn paizei na iparxei
    	return configInstance;
    }
    
    public static MapperConfiguration getMapperConfiguration(){
    	return configInstance;
    }
    

    private MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort,
                               Point left, Point right) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
        this.left = left;
        this.right = right;
    }

    private MapperConfiguration(String masterServerName, int masterServerPort, String reducerName, int reducerPort) {
        super(masterServerName, masterServerPort);
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

    private MapperConfiguration(String masterServerName, int masterServerPort) {
        super(masterServerName, masterServerPort);
    }
    
    public void setPoint(int corner, Point point) {
        if (corner == 1)
            left = point;
        else
            right = point;
    }

    public void setReducer(String reducerName, int reducerPort) {
        this.reducerName = reducerName;
        this.reducerPort = reducerPort;
    }

}
