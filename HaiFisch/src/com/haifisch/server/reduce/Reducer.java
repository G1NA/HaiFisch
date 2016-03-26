package com.haifisch.server.reduce;

import java.util.ArrayList;

import com.haifisch.server.CheckInMap;
import com.haifisch.server.PointOfInterest;
import com.haifisch.server.map.CheckIn;

public class Reducer implements /*ReduceWorker, */ Runnable {
	
	//CheckInMap<Integer, Object> map = new CheckInMap<>();

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
	}

	public void waitForMasterAck() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() {
		//
		
	}

	public CheckInMap<Integer, PointOfInterest> reduce(Integer key, Object value) {
		// the reducer should get top-K places from each mapper
		// calculate the final top-K ones
		// and discard duplicate photos
		 ArrayList<ArrayList<PointOfInterest>> places = (ArrayList<ArrayList<PointOfInterest>>) value;
		 
		 //places.parallelStream().reduce(); //TODO today
		
		
		
		return null;
	}

	public void sendResults(CheckInMap<Integer, PointOfInterest> map) {
		// TODO Auto-generated method stub
		
	}

}
