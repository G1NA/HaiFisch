package com.haifisch.server.reduce;

import com.haifisch.server.CheckinMap;

public class Reducer implements ReduceWorker {

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
	public CheckinMap<Integer, Object> reduce(int key, Object value) {
		CheckinMap<Integer, Object> map = new CheckinMap<>();
		
		return null;
	}

	@Override
	public void sendResults(CheckinMap<Integer, Object> map) {
		// TODO Auto-generated method stub
		
	}

}
