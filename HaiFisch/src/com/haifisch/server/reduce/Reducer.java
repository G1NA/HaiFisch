package com.haifisch.server.reduce;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reducer implements Runnable {

    private HashMap<String, ArrayList<PointOfInterest>> map = new HashMap<>();
    private int topK;

    @Override
    public void run() {
        //TODO

    }

    public void addMap(CheckInMap<String, PointOfInterest> addition) {
        for (Map.Entry<String, PointOfInterest> e : addition.entrySet()) {
            if (map.containsKey(e)) // then just add the found checkins
                map.get(e).add(e.getValue());
            else {
                map.put(e.getKey(), new ArrayList<>());
                map.get(e.getKey()).add(e.getValue());
            }
        }
    }

    public List<PointOfInterest> reduce() {
        // the reducer should get top-K places from each mapper
        // calculate the final top-K ones
        // and discard duplicate photos
    	
    	final List<PointOfInterest> reduced = new ArrayList<>(); //--> me anagkazei na valw final epeidh kanw thn parakatw entolh lel
    	
    	map.values().parallelStream() //for each arraylist 
	    	.forEach(l -> 
	    		reduced.add(l.stream().reduce((sum, v) -> sum.incrementObject(v)).get())
	    	);
    	
    	List<PointOfInterest> limited = reduced.parallelStream().sorted().limit(topK).collect(Collectors.toList());
    	limited.stream().forEach(PointOfInterest::cleanupDuplicatePhotos);
    	return limited;
    			
    }
    public void sendResults(CheckInMap<Integer, PointOfInterest> map) {
        // TODO Auto-generated method stub

    }

    public List<PointOfInterest> getResults() { //---> genika apo8ikeuse kapou ta apotelesmata sou kai 8a ta pairnei o RequestHandler me tn me8odo auti
        // TODO Auto-generated method stub
        return null;
    }

}
