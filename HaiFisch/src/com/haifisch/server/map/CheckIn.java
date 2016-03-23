package com.haifisch.server.map;

public class CheckIn {
	
	private String poi;
	private String poi_name; //---> isws xreiazetai gia na mporoume na t kanoume display stn efarmogi
	private String link;
	
	public CheckIn(String poi, String poi_name, String link){
		this.poi = poi;
		this.poi_name = poi_name;
		this.link = link;
	}
	
	public String getPOI(){return this.poi;}
	
	public String getPOI_NAME(){return this.poi_name;}
	
	public String getLINK(){return this.link;}

}
