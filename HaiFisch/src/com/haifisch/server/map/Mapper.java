package com.haifisch.server.map;

import com.haifisch.server.CheckInMap;
import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.PointOfInterest;
import com.haifisch.server.datamanagement.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mapper implements Runnable {

    private CheckInRequest request;
    private CheckInMap<String, PointOfInterest> counters;
    private int topK;
    public boolean shitHappened = false;

    public Mapper(CheckInRequest request) {
        this.request = request;
        this.topK = topK;
    }

    @Override
    public void run() {

        DatabaseManager db = new DatabaseManager("jdbc:mysql://localhost:3306/sys?user=p3130052&password=oyhya9lo");

        db.connectToDatabase();

        //----> mporei na alla3ei an dn einai swsto i veltistopoiimeno
        String query = "SELECT POI, POI_name, photos, latitude FROM checkins " //TODO
                + "WHERE longitude BETWEEN " + request.getLeftCorner().getLongtitude()
                + " AND " + request.getRightCorner().getLongtitude()
                + " AND latitude BETWEEN " + request.getRightCorner().getLatitude()
                + " AND " + request.getLeftCorner().getLatitude()
                + " AND time BETWEEN " + request.getFromTime()
                + " AND " + request.getToTime();

        ResultSet result = db.executeQuery(query);

        db.closeConnection();
        
        int cores = Runtime.getRuntime().availableProcessors();
        
        double interval =  
        		Math.abs((request.getLeftCorner().getLatitude() - request.getRightCorner().getLatitude())/cores);
        
        ArrayList<ArrayList<CheckIn>> entries = new ArrayList<ArrayList<CheckIn>>();
        
        for(int c = 0; c < cores; c++)
        	entries.add(new ArrayList<CheckIn>());

        try {
            while (result.next()) {
                CheckIn e = new CheckIn(result.getString(0), result.getString(1), result.getString(2));
                double lat = result.getDouble(3);
                int list = (int)Math.floor((lat - request.getRightCorner().getLatitude())/interval);
                entries.get(list).add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            shitHappened = true;
            return;
        }
        
        /*
        entries.stream().sorted((e1, e2) -> e1.get(0).compareTo(e2.get(0))); //---> einai swsto to compareTo edw?? (nmz nai)
        //TODO xwrise tn lista se kommatia analoga me tous epe3ergastes....an ginetai apeu8eias sto stream kalitera...meta efarmwse map.....
        counters = map(request.getRequestId(), entries);*/
        
        counters = map(request.getRequestId(), entries);

    }

    public CheckInMap<String, PointOfInterest> map(Object key, Object value) {

        ArrayList<ArrayList<CheckIn>> entries = (ArrayList<ArrayList<CheckIn>>) value;

        CheckInMap<String, PointOfInterest> intermediate = new CheckInMap<String, PointOfInterest>();
        
        entries.parallelStream().map(e -> countArea(e)).forEach(r-> intermediate.putAll(r));
        
        CheckInMap<String, PointOfInterest> counters = 
        		(CheckInMap<String, PointOfInterest>) intermediate.entrySet()
                .stream()
                .sorted((e1,e2)-> e1.getValue().compareTo(e2.getValue()))
                .limit(this.topK)
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()));
                //-------> apla euxomai na  kanei ontws auto pou 8elw
        
        
        return counters;

    }

    private CheckInMap<String, PointOfInterest> countArea(ArrayList<CheckIn> entries) {
    	
    	CheckInMap<String, PointOfInterest> counters = new CheckInMap<String, PointOfInterest>();
    	
    	/*-------> AN EXEI PERISSOTERO NOIMA ETSI AS TO ALLA3OUME
    	entries.parallelStream().forEach( e -> 
    	{ if(counters.containsKey(e.getPOI())){ //SIMEIWSI!!! edw epeidi i containsKey tsekarei ta hashCodes autos einai valid elegxos
            counters.get(e.getPOI()).addCheckIn(e.getLINK());
          } else {
        	PointOfInterest val = new PointOfInterest(e.getPOI(), e.getPOI_NAME()); 
        	val.addCheckIn(e.getLINK());
            counters.put(e.getPOI(), val );
          }
    	});
    	*/
    	
    	for(CheckIn e : entries){
    	if(counters.containsKey(e.getPOI())){ //SIMEIWSI!!! edw epeidi i containsKey tsekarei ta hashCodes autos einai valid elegxos
            counters.get(e.getPOI()).addCheckIn(e.getLINK());
          } else {
        	PointOfInterest val = new PointOfInterest(e.getPOI(), e.getPOI_NAME()); 
        	val.addCheckIn(e.getLINK());
            counters.put(e.getPOI(), val );
          }
    	}
    	
    	
    	return counters;
    }

    public CheckInMap<String, PointOfInterest> getResults() {
        return counters;
    }

}