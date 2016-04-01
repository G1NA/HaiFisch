package com.haifisch.server.map;

import com.haifisch.server.NetworkTools.CheckInRequest;
import com.haifisch.server.datamanagement.DatabaseManager;
import com.haifisch.server.utils.CheckInMap;
import com.haifisch.server.utils.PointOfInterest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

class Mapper implements Runnable {

    private CheckInRequest request;
    private CheckInMap<String, PointOfInterest> counters;
    private CheckInMap<String, PointOfInterest> interm; //PART OF MULTICORE IMPLEMENTATION
    boolean shitHappened = false;

    Mapper(CheckInRequest request) {
        this.request = request;
    }

    @Override
    public void run() {

        DatabaseManager db = new DatabaseManager("jdbc:mysql:// 83.212.117.76:3306/ds_systems_2016?user=omada26&password=omada26db");

        db.connectToDatabase();

        //----> mporei na alla3ei an dn einai swsto i veltistopoiimeno
        String query = "SELECT * FROM checkins " //TODO
                + "WHERE longitude BETWEEN " + request.getLeftCorner().getLongtitude()
                + " AND " + request.getRightCorner().getLongtitude()
                + " AND latitude BETWEEN " + request.getRightCorner().getLatitude()
                + " AND " + request.getLeftCorner().getLatitude()
                + " AND time BETWEEN " + request.getFromTime()
                + " AND " + request.getToTime();

        ResultSet result = db.executeQuery(query);

        db.closeConnection();

        int cores = Runtime.getRuntime().availableProcessors();

        double interval =
                Math.abs((request.getLeftCorner().getLatitude() - request.getRightCorner().getLatitude()) / cores);

        ArrayList<ArrayList<CheckIn>> entries = new ArrayList<>();

        for (int c = 0; c < cores; c++)
            entries.add(new ArrayList<>());

        try {
            while (result.next()) {
                /* results of the form:
                 * 0:id 1:user 2:POI 3:POI_name 4:POI_category 5:POI_category_id 6:longitude 7:latitude 8:time 9:photos
            	 * */
                CheckIn e = new CheckIn(result.getString(2), result.getString(3), result.getString(4),
                        result.getInt(5), result.getDouble(6), result.getDouble(7), result.getString(9));
                double lat = result.getDouble(3);
                int list = (int) Math.floor((lat - request.getRightCorner().getLatitude()) / interval);
                entries.get(list).add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            shitHappened = true;
            return;
        }
        
        /*
        entries.stream().sorted((e1, e2) -> e1.get(0).compareTo(e2.get(0))); //---> einai swsto to compareTo edw?? (nmz nai)
        //TODO xwrise tn lista se kommatia analoga me tous epe3ergastes....an ginetai apeu8eias sto stream kalitera...meta efarmwse map.....
        counters = map(request.getRequestId(), entries);*/

        counters = map(request.getRequestId(), entries);

    }


    public CheckInMap<String, PointOfInterest> map(Object key, Object value) {

        ArrayList<ArrayList<CheckIn>> entries = (ArrayList<ArrayList<CheckIn>>) value;

        CheckInMap<String, PointOfInterest> intermediate = new CheckInMap<>();

        entries.parallelStream().map(this::countArea).forEach(intermediate::putAll);

        CheckInMap<String, PointOfInterest> counters =
                (CheckInMap<String, PointOfInterest>) intermediate.entrySet()
                        .stream()
                        .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                        .limit(this.request.getTopK())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue));
        //-------> apla euxomai na  kanei ontws auto pou 8elw


        return counters;

    }

    //PART OF MULTICORE IMPLEMENTATION
    public CheckInMap<String, PointOfInterest> mapMultiCore(Object key, ArrayList<ArrayList<CheckIn>> value) {


        final int topK = this.request.getTopK();
        value.parallelStream().forEach(e -> new Thread() {
            public void run() {
                CheckInMap<String, PointOfInterest> map = countArea(e);
                populate((CheckInMap<String, PointOfInterest>) map.entrySet()
                        .stream()
                        .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                        .limit(topK)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue)));
            }
        });

        return
                (CheckInMap<String, PointOfInterest>) interm.entrySet()
                        .stream()
                        .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                        .limit(topK)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private CheckInMap<String, PointOfInterest> countArea(ArrayList<CheckIn> entries) {

        CheckInMap<String, PointOfInterest> counters = new CheckInMap<>();

        entries.parallelStream().forEach(e ->
        {
            if (counters.containsKey(e.getPOI())) { //SIMEIWSI!!! edw epeidi i containsKey tsekarei ta hashCodes autos einai valid elegxos
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

    //TODO CHECK FOR DUPLICATES
    //PART OF MULTICORE IMPLEMENTATION
    private synchronized void populate(CheckInMap<String, PointOfInterest> map) {
        if (interm == null)
            interm = new CheckInMap<>();
        interm.putAll(map);
    }

    public CheckInMap<String, PointOfInterest> getResults() {
        return counters;
    }

}