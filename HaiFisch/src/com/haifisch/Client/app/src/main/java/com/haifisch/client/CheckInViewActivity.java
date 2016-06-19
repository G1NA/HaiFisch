package com.haifisch.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import commons.CheckInRes;
import commons.NetworkPayload;
import commons.NetworkPayloadType;
import commons.PointOfInterest;

public class CheckInViewActivity extends AppCompatActivity implements OnImageInteractionListener {

    private PointOfInterest poi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_view);
        Bundle extra = getIntent().getBundleExtra("item");
        poi = (PointOfInterest) extra.get("item");
        RecyclerView view = (RecyclerView) findViewById(R.id.imageView);
        assert view != null;
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(new ImageAdapter(poi.getPhotos(), this));
        ((TextView) findViewById(R.id.place_name)).setText(poi.getName());
        ((TextView) findViewById(R.id.checkin_num)).setText(String.valueOf(poi.getNumberOfCheckIns()));
        ((TextView) findViewById(R.id.category)).setText(poi.getCategory());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void OnImageInteraction(String item) {

    }

    //Add a new checkin here
    public void checkIn(View v) {
        HashMap<String, PointOfInterest> tmp = new HashMap<>();
        PointOfInterest pointOfInterest = new PointOfInterest(poi.getID(), poi.getName(), poi.getCategory(), poi.getCategoryId(), poi.getCoordinates());
        //TODO take photo and upload it somewhere


        //add link here
        pointOfInterest.addCheckIn("not exists");
        tmp.put("new", poi);
        SenderSocket sock = new SenderSocket(Master.masterIP, Master.masterPort,
                new NetworkPayload(NetworkPayloadType.CHECK_IN, true,
                        new CheckInRes("", 0, tmp, 0),
                        Communicator.address, Communicator.port, 200, "OK"));
        Thread t = new Thread(sock);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show();
        }

        if (!sock.isSent())
            Toast.makeText(this, "Failed to send request", Toast.LENGTH_SHORT).show();

    }

}
