package com.haifisch.client;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, OnFragmentInteractionListener {

    private GoogleMap mMap;
    ProgressDialog dialog;
    ImageView active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportFragmentManager().beginTransaction().replace(R.id.map, new SupportMapFragment()).commit();
        getSupportFragmentManager().executePendingTransactions();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView  view = (ImageView) findViewById(R.id.map_view);
        view.setOnClickListener(this);
        setActive(view);

        //view.invalidate();
        findViewById(R.id.top_set).setOnClickListener(this);
        findViewById(R.id.settings_view).setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading map..");
        dialog.setCancelable(false);
        dialog.show();

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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_view:
                setPassive(active);
                setActive((ImageView) v);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                getSupportFragmentManager().beginTransaction().replace(R.id.map, new SupportMapFragment()).commit();
                getSupportFragmentManager().executePendingTransactions();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                break;
            case R.id.top_set:
                setPassive(active);
                setActive((ImageView) v);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map,TopkChoice.newInstance()).commit();
                break;
            case R.id.settings_view:
                setPassive(active);
                setActive((ImageView) v);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map,SettingsFragment.newInstance()).commit();
                break;
            default:
                break;
        }
    }
    public void setActive(ImageView v){
        active = v;
        v.setColorFilter(getResources().getColor(R.color.colorPrimary));
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int)getResources().getDimension(R.dimen.bottom_but_active);
        v.setLayoutParams(params);
    }

    public void setPassive(ImageView v) {
        v.setColorFilter(getResources().getColor(R.color.button_material_dark));
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = (int)getResources().getDimension(R.dimen.bottom_but_passive);
        v.setLayoutParams(params);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
