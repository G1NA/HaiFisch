package com.haifisch.server.utils;

import java.util.ArrayList;
import java.util.Collection;

public class PointOfInterest implements Comparable<PointOfInterest> {
	
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
		
		if(!link.equalsIgnoreCase("Not exists"))
			photos.add(link);
		
		++this.numOfPhotos;
	}
	
	public void incrementCheckIns(){
		++this.numOfCheckIns;
	}
	
	public void incrementCheckInsBy(int value){
		this.numOfCheckIns += value;
	}
	
	protected void addPhotos(Collection<? extends String> photos){
		photos.stream().forEach(p -> addPhoto(p));
	}
	
	public void addCheckIn(String link){
		this.incrementCheckIns();
		this.addPhoto(link);
	}
	
	public void incrementObject(PointOfInterest poi){
		// we've checked the equality of the poi's -> its about the same place
		this.incrementCheckInsBy(poi.getNumberOfCheckIns());
		this.addPhotos(poi.getPhotos());
	}

	public int compareTo(PointOfInterest p) {
		return Integer.signum(this.getNumberOfCheckIns()- p.getNumberOfCheckIns());
	}
	
}
