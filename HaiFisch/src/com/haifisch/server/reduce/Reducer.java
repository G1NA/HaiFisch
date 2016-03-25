package com.haifisch.server.reduce;

import com.haifisch.server.CheckInMap;

public class Reducer implements ReduceWorker {
	
	CheckInMap<Integer, Object> map = new CheckInMap<>();

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForMasterAck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CheckInMap<Integer, Object> reduce(int key, Object value) {
		//TODO when get home
		
		return null;
	}

	@Override
	public void sendResults(CheckInMap<Integer, Object> map) {
		// TODO Auto-generated method stub
		
	}

}
