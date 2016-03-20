package com.haifisch.server.map;

import com.haifisch.server.CheckinMap;
import com.haifisch.server.NetworkTools.*;
import com.haifisch.server.datamanagement.*;

import java.sql.*;
import java.util.ArrayList;

public class Mapper implements Runnable {

    private CheckInRequest request;
    private CheckinMap<String, Integer> counters;
    public boolean shitHappened = false;

    public Mapper(CheckInRequest request) {
        this.request = request;
    }


    @Override
    public void run() {

        DatabaseManager db = new DatabaseManager("jdbc:mysql://localhost:3306/sys?user=p3130052&password=oyhya9lo");

        db.connectToDatabase();

        //----> mporei na alla3ei an dn einai swsto i veltistopoiimeno
        String query = "SELECT POI, photos FROM checkins "
                + "WHERE longitude BETWEEN " + request.getLeftCorner()[0]
                + " AND " + request.getRightCorner()[0]
                + " AND latitude BETWEEN " + request.getRightCorner()[1]
                + " AND " + request.getLeftCorner()[1]
                + " AND time BETWEEN " + request.getFromTime()
                + " AND " + request.getToTime();

        ResultSet result = db.executeQuery(query);

        db.closeConnection();

        ArrayList<ArrayList<String>> entries = new ArrayList<ArrayList<String>>();

        try {
            while (result.next()) {
                ArrayList<String> e = new ArrayList<String>();
                //adding POI
                e.add(result.getString(0));
                //adding photo
                e.add(result.getString(1));

                entries.add(e);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            shitHappened = true;
            return;
        }

        counters = map(request.getRequestId(), entries);

    }

    public CheckinMap<String, Integer> map(Object key, Object value) {

        ArrayList<ArrayList<String>> entries = (ArrayList<ArrayList<String>>) value;

        CheckinMap<String, Integer> counters = new CheckinMap<String, Integer>();

        entries.parallelStream().forEach(e -> count_area(counters, e));

        return counters;

    }

    private void count_area(CheckinMap<String, Integer> counters, ArrayList<String> entry) {

        if (counters.containsKey(entry.get(0))) {
            counters.replace(entry.get(0), counters.get(entry.get(0)) + 1);
            //---> dn eimai sigouri an kanei pragmati auto p 8elw...
        } else {
            counters.put(entry.get(0), 1);
        }
    }

    private CheckinMap<String, Integer> getResults() {
        return counters;
    }

}