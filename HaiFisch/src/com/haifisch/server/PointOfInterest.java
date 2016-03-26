package com.haifisch.server;

import java.util.ArrayList;

public class PointOfInterest implements Comparable<PointOfInterest>{
	
	private String id;
	private String name;
	private ArrayList<String> photos;
	private int numOfCheckIns;
	private int numOfPhotos;
	
	public PointOfInterest(String id, String name){
		this.id = id;
		this.name = name;
		this.photos = new ArrayList<String>();
		this.numOfCheckIns = 0;
		this.numOfPhotos = 0;
	}
	
	public String getID(){ return this.id; }
	
	public String getName(){ return this.name; }
	
	public ArrayList<String> getPhotos(){ return this.photos; }
	
	public int getNumberOfCheckIns(){ return this.numOfCheckIns; }
	
	public int getNumberOfPhotos(){ return this.numOfPhotos; }
	
	public void addPhoto(String link){
		
		if(!link.equals("Not exists"))
			photos.add(link);
	}
	
	public void incrementCheckIns(){
		this.numOfCheckIns++;
	}
	
	public void addCheckIn(String link){
		this.incrementCheckIns();
		this.addPhoto(link);
	}

	public int compareTo(PointOfInterest p) {
		//return Integer.signum(this.getNumberOfCheckIns()- p.getNumberOfCheckIns());
		return Integer.signum(this.getNumberOfPhotos() - p.getNumberOfPhotos());
	}
	
}
