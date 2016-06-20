package com.haifisch.client;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import commons.CheckInRequest;
import commons.CheckInRes;
import commons.NetworkPayload;
import commons.NetworkPayloadType;
import commons.Point;
import commons.PointOfInterest;
import commons.onConnectionListener;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        OnFragmentInteractionListener, ResultFragment.OnListFragmentInteractionListener, onConnectionListener {

    private GoogleMap mMap;
    ProgressDialog dialog;
    ImageView active;
    Date selectedFrom;
    Date selectedTo;
    int selecting = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportFragmentManager().beginTransaction().replace(R.id.map, new SupportMapFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView view = (ImageView) findViewById(R.id.map_view);
        view.setOnClickListener(this);
        setActive(view);

        //view.invalidate();
        findViewById(R.id.top_set).setOnClickListener(this);
        findViewById(R.id.settings_view).setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading map..");
        dialog.setCancelable(false);
        dialog.show();
        Communicator.listener = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        dialog.dismiss();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(40.7, -74);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (Master.visiblePois != null && Master.visiblePois.size() != 0) {
            for (PointOfInterest poi : Master.visiblePois)
                mMap.addMarker(new MarkerOptions()
                        .visible(true)
                        .position(new LatLng(poi.getCoordinates().getLongtitude(),
                                poi.getCoordinates().getLatitude())));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //Here send the user to the appropriate view
                    Point p;
                    for (PointOfInterest poi : Master.visiblePois) {
                        p = poi.getCoordinates();
                        if (p.getLatitude() == marker.getPosition().latitude
                                && p.getLongtitude() == marker.getPosition().longitude) {
                            onListFragmentInteraction(poi);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_view:
                setPassive(active);
                setActive((ImageView) v);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                getSupportFragmentManager().beginTransaction().replace(R.id.map, new SupportMapFragment()).commit();
                getSupportFragmentManager().executePendingTransactions();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                findViewById(R.id.view).setVisibility(View.VISIBLE);
                break;
            case R.id.top_set:
                setPassive(active);
                setActive((ImageView) v);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, ResultFragment.newInstance()).commit();
                findViewById(R.id.view).setVisibility(View.GONE);
                break;
            case R.id.settings_view:
                setPassive(active);
                setActive((ImageView) v);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, SettingsFragment.newInstance()).commit();
                findViewById(R.id.view).setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void setActive(ImageView v) {
        active = v;
        v.setColorFilter(getResources().getColor(R.color.colorPrimary));
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.bottom_but_active);
        v.setLayoutParams(params);
    }

    public void setPassive(ImageView v) {
        v.setColorFilter(getResources().getColor(R.color.button_material_dark));
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int) getResources().getDimension(R.dimen.bottom_but_passive);
        v.setLayoutParams(params);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public LatLng[] getPos() {
        return new LatLng[]{mMap.getProjection().getVisibleRegion().nearLeft,
                mMap.getProjection().getVisibleRegion().farRight};
    }

    public void search(View view) {
        /*
        PointOfInterest item = new PointOfInterest("mppla", "mpla", ",p[la", 3, new Point(32.0, 23.0));
        Intent n = new Intent(this, CheckInViewActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("item", item);
        n.putExtra("item", extra);
        startActivity(n);
        */

        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (selecting == 0)
                    selectedFrom = c.getTime();
                else {
                    selectedTo = c.getTime();
                }

                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        if (selecting == 0) {
                            selectedFrom = c.getTime();
                            selecting++;
                            search(view);
                        } else if (selecting == 1) {
                            selectedTo = c.getTime();
                            sendRequest();
                        }
                    }
                }, 21, 0, true).show();

            }
        }, 2012, 4, 1).show();


    }

    public void sendRequest() {
        LatLng[] pos = getPos();
        SenderSocket sock = new SenderSocket(Master.masterIP, Master.masterPort,
                new NetworkPayload(NetworkPayloadType.CHECK_IN_REQUEST, true,
                        new CheckInRequest("", 0, new Point(pos[0].longitude, pos[0].latitude),
                                new Point(pos[1].longitude, pos[1].latitude),
                                new Timestamp(selectedFrom.getTime()), new Timestamp(selectedTo.getTime())),
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
        Toast.makeText(this, "Sent request, awaiting results...", Toast.LENGTH_SHORT).show();
        selecting = 0;
        selectedFrom = null;
        selectedTo = null;
    }

    @Override
    public void onListFragmentInteraction(PointOfInterest item) {
        //If the id is empty it's a dummy item
        if (item.getID().length() == 0) {
            return;
        } else {
            Intent n = new Intent(this, CheckInViewActivity.class);
            Bundle extra = new Bundle();
            extra.putSerializable("item", item);
            n.putExtra("item", extra);
            startActivity(n);
        }
    }


    //Populate the map and the result adapter with the received data
    @Override
    public void onConnect(NetworkPayload networkPayload) {
        try {
            if (networkPayload.payload instanceof CheckInRes) {

                final CheckInRes res = (CheckInRes) networkPayload.payload;
                Master.visiblePois = new ArrayList<>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (PointOfInterest poi : res.getMap().values()) {
                            Master.visiblePois.add(poi);
                            mMap.addMarker(new MarkerOptions()
                                    .visible(true)
                                    .position(new LatLng(poi.getCoordinates().getLongtitude(),
                                            poi.getCoordinates().getLatitude()))
                            );
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
