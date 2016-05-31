package com.haifisch.server.mapper_node;

import com.haifisch.server.datamanagement.DatabaseManager;
import commons.CheckInRequest;
import commons.PointOfInterest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class Mapper implements Runnable {

    private CheckInRequest request; // the request the mapper serves
    private HashMap<String, PointOfInterest> counters; // the result of the mapping
    private String error; // a message in case an error occurs
    boolean errorFound = false; // true if a message occurs

    /**
     * Constructor
     *
     * @param request the request to be mapped
     */
    Mapper(CheckInRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        DatabaseManager db = new DatabaseManager("jdbc:mysql://83.212.117.76:3306/ds_systems_2016?user=omada26&password=omada26db");

        db.connectToDatabase();
        
        //basic query
        String query = "SELECT * FROM checkins "
                + "WHERE longitude BETWEEN " + request.getLeftCorner().getLongtitude()
                + " AND " + request.getRightCorner().getLongtitude()
                + " AND latitude BETWEEN " + request.getLeftCorner().getLatitude()
                + " AND " + request.getRightCorner().getLatitude()
                + " AND time BETWEEN \'" + request.getFromTime()
                + "\' AND \'" + request.getToTime() + "\'"
                + "ORDER BY latitude";

        ResultSet result = db.executeQuery(query);

        
        int cores = Runtime.getRuntime().availableProcessors();

        double interval =
                Math.abs((request.getLeftCorner().getLatitude() - request.getRightCorner().getLatitude()) / cores);

        ArrayList<ArrayList<CheckIn>> entries = new ArrayList<>();

        for (int c = 0; c < cores; c++)
            entries.add(new ArrayList<>());

        try {
        	int list = 0;
            while (result.next()) {
                /* results of the form:
                 * 1:id 2:user 3:POI 4:POI_name 5:POI_category 6:POI_category_id 7:longitude 8:latitude 9:time 10:photos
            	 **/
                CheckIn e = new CheckIn(result.getString(3), result.getString(4), result.getString(5),
                        result.getInt(6), result.getDouble(7), result.getDouble(8), result.getString(10));
                if(e.getLATITUDE() >= (list+1)*interval)
                	list++;

            	entries.get(list).add(e);
            }
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            errorFound = true;
            return;
        }
        
        /*
        !TEST IMPLEMENTATION IGNORE!

        ArrayList<CheckIn> ent = new ArrayList<CheckIn>();
	    ArrayList<ArrayList<CheckIn>> entries = new ArrayList<>();
        try {
			while(result.next()){
				CheckIn e = new CheckIn(result.getString(3), result.getString(4), result.getString(5),
			        result.getInt(6), result.getDouble(7), result.getDouble(8), result.getString(10));
				ent.add(e);
			}
			
			List<CheckIn> entList = ent.stream()
			   .sorted((e1,e2)-> e1.getCOORDINATES().getLatitude().compareTo(e2.getCOORDINATES().getLatitude()))
			   .collect(Collectors.toList());
			
			int arraysize = entList.size()/cores;
		    int start = 0;
		    int end = arraysize;
		    for (int c = 0; c < cores; c++){
		    	entries.add(new ArrayList<>());
		        entries.get(c).addAll(entList.subList(start, (end > entList.size())? entList.size() : end));
		        start += arraysize;
		        end += arraysize;
		        while(entList.get(start-1).getPOI().equals(entList.get(start).getPOI())){
		        	entries.get(c).add(entList.get(start));
		        	start++;
		        	end++;
		        	if(start > entList.size()) break;
		        }
		        if(start > entList.size()) break;
		    }
			
			   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

        counters = map(request.getRequestId(), entries);

    }


    /**
     * Maps checkins, arraylists to a single HashMap of the topK points of interest
     * based on how many checkins exist at each point.
     *
     * @param key   the request served
     * @param value an arraylist of arraylists, one to be served by each core
     * @return topK points of interest
     */
    public HashMap<String, PointOfInterest> map(Object key, ArrayList<ArrayList<CheckIn>> value) {

        HashMap<String, PointOfInterest> intermediate = new HashMap<>();

        value.parallelStream()
                .map(this::countArea)
                .forEach(intermediate::putAll);

        return (HashMap<String, PointOfInterest>) intermediate.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .limit(this.request.getTopK())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)
                );
    }


    /**
     * Counts how many checkins each distinct point of interest has, and adds the photos taken there for every
     * point of interest in the entries list.
     *
     * @param entries a list of checkins for various points of interest
     * @return a HashMap of all the points of interest
     */
    synchronized private HashMap<String, PointOfInterest> countArea(ArrayList<CheckIn> entries) {

        HashMap<String, PointOfInterest> counters = new HashMap<>();

        entries.stream()
                .forEach(e -> {
                    if (counters.containsKey(e.getPOI())) {
                        counters.get(e.getPOI()).addCheckIn(e.getLINK());
                    } else {
                        PointOfInterest val = new PointOfInterest(e.getPOI(), e.getPOI_NAME(),
                                e.getPOI_CATEGORY(), e.getPOI_CATEGORY_ID(), e.getCOORDINATES());
                        val.addCheckIn(e.getLINK());
                        counters.put(e.getPOI(), val);
                    }
                });

        return counters;
    }

    /**
     * Get the results of the mapper_node process
     *
     * @return a HashMap with all the points of interest found by the
     * mapper_node function
     */
    public HashMap<String, PointOfInterest> getResults() {
        return counters;
    }

    /**
     * @return the error that occurred
     */
    String getError() {
        return error;
    }

}