package com.haifisch.server.map;

import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.datamanagement.DatabaseManager;
import com.haifisch.server.utils.PointOfInterest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class Mapper implements Runnable {

    private CheckInRequest request;
    private HashMap<String, PointOfInterest> counters;
    private String error;
    boolean shitHappened = false;

    Mapper(CheckInRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        DatabaseManager db = new DatabaseManager("jdbc:mysql://83.212.117.76:3306/ds_systems_2016?user=omada26&password=omada26db");

        db.connectToDatabase();

        String query = "SELECT * FROM checkins "
                + "WHERE longitude BETWEEN " + request.getLeftCorner().getLongtitude()
                + " AND " + request.getRightCorner().getLongtitude()
                + " AND latitude BETWEEN " + request.getLeftCorner().getLatitude()
                + " AND " + request.getRightCorner().getLatitude()
                + " AND time BETWEEN \'" + request.getFromTime()
                + "\' AND \'" + request.getToTime() + "\'";

        ResultSet result = db.executeQuery(query);


        int cores = Runtime.getRuntime().availableProcessors();

        double interval =
                Math.abs((request.getLeftCorner().getLatitude() - request.getRightCorner().getLatitude()) / cores);

        ArrayList<ArrayList<CheckIn>> entries = new ArrayList<>();

        for (int c = 0; c < cores; c++)
            entries.add(new ArrayList<>());

        try {
            while (result.next()) {
                /* results of the form:
                 * 1:id 2:user 3:POI 4:POI_name 5:POI_category 6:POI_category_id 7:longitude 8:latitude 9:time 10:photos
            	 * */
                CheckIn e = new CheckIn(result.getString(3), result.getString(4), result.getString(5),
                        result.getInt(6), result.getDouble(7), result.getDouble(8), result.getString(10));
                double lat = result.getDouble(7);
                int list = (int) Math.floor((request.getRightCorner().getLatitude() - lat) / interval);
                entries.get(list).add(e);
            }
            db.closeConnection();
        } catch (SQLException e) {
            error = e.getMessage();
            shitHappened = true;
            return;
        }

        counters = map(request.getRequestId(), entries);

    }


    public HashMap<String, PointOfInterest> map(Object key, Object value) {

        HashMap<String, PointOfInterest> intermediate = new HashMap<>();

        ((ArrayList<ArrayList<CheckIn>>) value)
                .parallelStream()
                .map(this::countArea)
                .forEach(intermediate::putAll);

        return (HashMap<String, PointOfInterest>) intermediate.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(this.request.getTopK())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

    }


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

    public HashMap<String, PointOfInterest> getResults() {
        return counters;
    }

    public String getError() {
        return error;
    }

}