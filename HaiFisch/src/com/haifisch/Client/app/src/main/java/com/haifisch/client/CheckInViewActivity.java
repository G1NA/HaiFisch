package com.haifisch.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

    public void checkIn(View v){



    }

}
