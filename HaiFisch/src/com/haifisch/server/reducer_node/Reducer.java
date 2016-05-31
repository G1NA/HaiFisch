package com.haifisch.server.reducer_node;

import commons.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Runnable class representing a reducer.
 * <p>
 * It performs a reducing process on the results of the mappers to determine the topK
 * points of interest based on the number of checkins and cleans up an duplicate photos.
 */

class Reducer implements Runnable {

    /**
     * Holds the results of the mappers.
     * Each key/poi has a list of values representing the results of the mappers
     * for that poi.
     * <p>
     * This mapper_node is to be reduced by summing up all the results in the lists in
     * a single object, creating a final list.
     */
    private HashMap<String, ArrayList<PointOfInterest>> map = new HashMap<>();

    /**
     * The list of the final results of the reducer.
     */
    private List<PointOfInterest> results;

    /**
     * The number of top points of interest to return.
     */
    private int topK;


    @Override
    public void run() {
        reduce();
    }

    /**
     * Adds one by one the results of each mapper into a single hashmap before reducing.
     *
     * @param addition the newly mapper_node result to be added.
     */
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

    /**
     * The reducing process.
     * <p>
     * It calculates the final topK results by reducing the mapper_node and
     * sorting it. Finally, it cleans up any duplicate photos.
     */
    private void reduce() {
        // the reducer should get top-K places from each mapper
        // calculate the final top-K ones
        // and discard duplicate photos

        List<PointOfInterest> reduced = new ArrayList<>();

        /*
         * Reduce each entry of the mapper_node into a list of points of interest,
         * by summing up the values in the bucket of each poi.
         */
        map.values().stream() //for each arraylist
                .forEach(l -> l
                        .stream()
                        .reduce(PointOfInterest::incrementObject)
                        .ifPresent(reduced::add)
                );
        
        /*
         * Sort the results and get the topK.
         * Also perform the duplicate photos cleansing. 
         */
        (results = reduced
                .parallelStream()
                .sorted()
                .limit(topK)
                .collect(Collectors.toList()))
                .stream()
                .forEach(PointOfInterest::cleanupDuplicatePhotos);
    }

    /**
     * @return the results of the reducing process in a mapper_node form.
     */
    public HashMap<String, PointOfInterest> getResults() {
        HashMap<String, PointOfInterest> map = new HashMap<>();
        for (PointOfInterest point : results)
            map.put(point.getID(), point);
        return map;
    }

    /**
     * topK setter
     *
     * @param topK
     */
    void setTopK(int topK) {
        this.topK = topK;
    }

    /**
     * topK getter
     *
     * @return topK
     */
    int getTopK() {
        return this.topK;
    }
}
