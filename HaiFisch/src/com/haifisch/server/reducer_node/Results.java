package com.haifisch.server.reducer_node;

import commons.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;

class Results {

    private ArrayList<HashMap<String, PointOfInterest>> results = new ArrayList<>();
    private int topK;
    private int mappers = -1;
    private int mappersDone = 0;

    /**
     * Get the stored results
     *
     * @return The results
     */
    public ArrayList<HashMap<String, PointOfInterest>> getResults() {
        return results;
    }

    /**
     * Set the results equal the ones given
     *
     * @param results The results to be stored
     */
    public void setResults(ArrayList<HashMap<String, PointOfInterest>> results) {
        this.results = results;
    }

    /**
     * Add results to the storage
     *
     * @param results The results to be stored
     */
    public void add(HashMap<String, PointOfInterest> results) {
        this.results.add(results);
    }

    /**
     * Get the topK variable for the request
     *
     * @return The topK variable
     */
    int getTopK() {
        return topK;
    }

    /**
     * Set the topK variable
     *
     * @param topK The value to be set
     */
    void setTopK(int topK) {
        this.topK = topK;
    }

    /**
     * Set the number of mappers that must add to the results
     *
     * @param mappers The number of mappers
     */
    void setMappers(int mappers) {
        this.mappers = mappers;
    }

    /**
     * Increment the amount of mappers that completed their job
     */
    void mapperCompleted() {
        mappersDone++;
    }

    /**
     * If all the mappers are done
     *
     * @return true if they are
     */
    boolean isDone() {
        return mappersDone == mappers;
    }
}
