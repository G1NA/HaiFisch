package com.haifisch.server.reduce;

import com.haifisch.server.utils.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;

public class Results {

    private ArrayList<HashMap<String, PointOfInterest>> results = new ArrayList<>();
    private int topK;
    private int mappers = -1;
    private int mappersDone = 0;

    public ArrayList<HashMap<String, PointOfInterest>> getResults() {
        return results;
    }

    public void setResults(ArrayList<HashMap<String, PointOfInterest>> results) {
        this.results = results;
    }

    public void add(HashMap<String, PointOfInterest> results) {
        this.results.add(results);
    }

    int getTopK() {
        return topK;
    }

    void setTopK(int topK) {
        this.topK = topK;
    }

    void setMappers(int mappers) {
        this.mappers = mappers;
    }

    void mapperCompleted() {
        mappersDone++;
    }

    boolean isDone() {
        return mappersDone == mappers;
    }
}
