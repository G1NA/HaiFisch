package com.haifisch.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import commons.PointOfInterest;

public class CheckInViewActivity extends AppCompatActivity implements OnImageInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_view);
        Bundle extra = getIntent().getBundleExtra("item");
        PointOfInterest obj = (PointOfInterest) extra.get("item");
        RecyclerView view = (RecyclerView) findViewById(R.id.imageView);
        assert view != null;
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(new ImageAdapter(obj.getPhotos(), this));
    }

    @Override
    public void OnImageInteraction(String item) {

    }

}
