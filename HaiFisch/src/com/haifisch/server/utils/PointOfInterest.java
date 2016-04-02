package com.haifisch.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PointOfInterest implements Comparable<PointOfInterest> {
	
	private String id;
	private String name;
	private String category;
	private int category_id;
	private Point coords;
	private List<String> photos;
	private int numOfCheckIns;
	private int numOfPhotos; //---> pou xreiazetai?
	
	public PointOfInterest(String id, String name, String category, int category_id, Point coords){
		this.id = id;
		this.name = name;
		this.category = category;
		this.category_id = category_id;
		this.coords = coords;
		this.photos = new ArrayList<String>();
		this.numOfCheckIns = 0;
		this.numOfPhotos = 0;
	}
	
	public PointOfInterest(String id, String name, String category, int category_id, double longitude, double latitude){
		this.id = id;
		this.name = name;
		this.category = category;
		this.category_id = category_id;
		this.coords = new Point(longitude, latitude);
		this.photos = new ArrayList<String>();
		this.numOfCheckIns = 0;
		this.numOfPhotos = 0;
	}
	
	
	public String getID(){ return this.id; }
	
	public String getName(){ return this.name; }
	
	public String getCategory(){ return this.category; }
	
	public int getCategoryId(){ return this.category_id; }
	
	public Point getCoordinates(){ return this.coords; }
	
	public List<String> getPhotos(){ return this.photos; }
	
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
	
	public PointOfInterest incrementObject(PointOfInterest poi){
		// we've checked the equality of the poi's -> its about the same place
		this.incrementCheckInsBy(poi.getNumberOfCheckIns());
		this.addPhotos(poi.getPhotos());
		return this;
	}
	
	public void cleanupDuplicatePhotos(){
		this.photos = this.photos.stream().distinct().collect(Collectors.toList());
	}

	public int compareTo(PointOfInterest p) {
		return Integer.signum(this.getNumberOfCheckIns()- p.getNumberOfCheckIns());
	}
	
}
