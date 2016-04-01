package com.haifisch.server.reduce;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Reducer implements Runnable {

    private HashMap<String, ArrayList<PointOfInterest>> map = new HashMap<>();
    private int topK;

    @Override
    public void run() {
        //

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

    public CheckInMap<String, PointOfInterest> reduce(/*Integer key, CheckInMap<String, PointOfInterest> value*/) {
        // the reducer should get top-K places from each mapper
        // calculate the final top-K ones
        // and discard duplicate photos
    	
    	map.values().parallelStream().forEach(v -> v.stream().reduce(new PointOfInterest(), (sum, p) -> p.getNumberOfCheckIns() + sum.getNumberOfCheckIns()));

        CheckInMap<String, PointOfInterest> reducedMap =
                (CheckInMap<String, PointOfInterest>)
                        map.values().parallelStream()
                                .sorted()
                                .limit(this.topK)
                                .collect(Collectors.toMap(
                                        v -> ((PointOfInterest) v).getID(),
                                        v -> (PointOfInterest) v
                                ));

        return reducedMap;
    }

    protected void cleanupDuplicatePhotos() {
        this.map.entrySet().parallelStream().forEach(p -> {

        });
    }

    public void sendResults(CheckInMap<Integer, PointOfInterest> map) {
        // TODO Auto-generated method stub

    }

    public CheckInMap getResults() { //---> genika apo8ikeuse kapou ta apotelesmata sou kai 8a ta pairnei o RequestHandler me tn me8odo auti
        // TODO Auto-generated method stub
        return null;
    }

}
