package com.haifisch.server.reduce;

import com.haifisch.server.utils.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Reducer implements Runnable {

    private HashMap<String, ArrayList<PointOfInterest>> map = new HashMap<>();
    private List<PointOfInterest> results;
    private int topK;


    @Override
    public void run() {
        reduce();

    }


    void addMap(HashMap<String, PointOfInterest> addition) {
        for (Map.Entry<String, PointOfInterest> e : addition.entrySet()) {
            if (map.containsKey(e)) // then just add the found checkins
                map.get(e).add(e.getValue());
            else {
                map.put(e.getKey(), new ArrayList<>());
                map.get(e.getKey()).add(e.getValue());
            }
        }
    }

    private void reduce() {
        // the reducer should get top-K places from each mapper
        // calculate the final top-K ones
        // and discard duplicate photos

        final List<PointOfInterest> reduced = new ArrayList<>();

        map.values().stream() //for each arraylist
                .forEach(l -> l
                        .stream()
                        .reduce(PointOfInterest::incrementObject)
                        .ifPresent(reduced::add)
                );
        (results = reduced
                .parallelStream()
                .sorted()
                .limit(topK)
                .collect(Collectors.toList()))
                .stream()
                .forEach(PointOfInterest::cleanupDuplicatePhotos);

    }

    public HashMap<String, PointOfInterest> getResults() {
        HashMap<String, PointOfInterest> map = new HashMap<>();
        for (PointOfInterest point : results)
            map.put(point.getID(), point);
        return map;
    }

    void setTopK(int topK) {
        this.topK = topK;
    }

    int getTopK() {
        return this.topK;
    }
}
