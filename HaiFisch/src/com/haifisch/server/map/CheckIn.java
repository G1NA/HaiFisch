package com.haifisch.server.map;

import java.sql.Timestamp;

import com.haifisch.server.utils.Point;

public class CheckIn {
	
	private String poi;
	private String poi_name; //---> isws xreiazetai gia na mporoume na t kanoume display stn efarmogi
	private String poi_category;
	private int poi_category_id;
	private String link;
	private Point coords;
	
	
	//---> aaan xreiastoun ta vazoume
	//private Timestamp time;
	//private int id;
	//private int user;
	
	
	public CheckIn(String poi, String poi_name,String poi_category,int poi_category_id, double longitude, double latitude, String link){
		this.poi = poi;
		this.poi_name = poi_name;
		this.poi_category = poi_category;
		this.poi_category_id = poi_category_id;
		this.link = link;
		this.coords = new Point(longitude,  latitude);
		
	}
	
	public String getPOI(){return this.poi;}
	
	public String getPOI_NAME(){return this.poi_name;}
	
	public String getLINK(){return this.link;}
	
	public String getPOI_CATEGORY(){ return this.poi_category; }
	
	public int getPOI_CATEGORY_ID(){ return this.poi_category_id; }
	
	public Point getCOORDINATES(){ return this.coords; }
	
	public double getLONGITUDE(){ return coords.getLongtitude(); }
	
	public double getLATITUDE(){ return coords.getLatitude(); }

}
