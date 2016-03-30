package com.haifisch.server.reduce;

import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

import java.util.Map;
import java.util.stream.Collectors;

public class Reducer implements /*ReduceWorker, */ Runnable {

    private CheckInMap<String, PointOfInterest> map = new CheckInMap<>();
    private int topK;

    public void initialize() {
        // TODO Auto-generated method stub

    }

    public void waitForTasksThread() {
        // TODO Auto-generated method stub

    }

    public void waitForMasterAck() {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        //

    }

    public void addMap(CheckInMap<String, PointOfInterest> addition) {
        for (Map.Entry<String, PointOfInterest> e : addition.entrySet()) {
            if (map.containsKey(e)) // then just add the found checkins
                map.get(e).incrementObject(e.getValue());
            else
                map.put(e.getKey(), e.getValue());
        }
    }

    public CheckInMap<String, PointOfInterest> reduce(/*Integer key, CheckInMap<String, PointOfInterest> value*/) {
        // the reducer should get top-K places from each mapper
        // calculate the final top-K ones
        // and discard duplicate photos

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
