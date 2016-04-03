package com.haifisch.server.reduce;

import com.haifisch.server.utils.PointOfInterest;

import java.util.ArrayList;
import java.util.HashMap;

public class Results {

    ArrayList<HashMap<String, PointOfInterest>> results = new ArrayList<>();
    int topK;

    public ArrayList<HashMap<String, PointOfInterest>> getResults() {
        return results;
    }

    public void setResults(ArrayList<HashMap<String, PointOfInterest>> results) {
        this.results = results;
    }
    public void add(HashMap<String, PointOfInterest> results) {
        this.results.add(results);
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }


}
