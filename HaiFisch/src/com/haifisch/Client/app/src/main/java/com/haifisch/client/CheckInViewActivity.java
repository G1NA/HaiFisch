package com.haifisch.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import akiniyalocts.imgurapiexample.activities.SantaHelper;
import akiniyalocts.imgurapiexample.utils.ResCallback;
import commons.NetworkPayload;
import commons.NetworkPayloadType;
import commons.PointOfInterest;

public class CheckInViewActivity extends AppCompatActivity implements OnImageInteractionListener, ResCallback {

    private PointOfInterest poi;
    private PointOfInterest pointOfInterest;
    private Uri photoURI;


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

        pointOfInterest = new PointOfInterest(poi.getID(), poi.getName(),
                poi.getCategory(), poi.getCategoryId(), poi.getCoordinates());

        new AlertDialog.Builder(this)
                .setTitle("New CheckIn")
                .setMessage("Do you want to add a photo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        } else {
                            dispatchTakePictureIntent();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckInViewActivity.this.pointOfInterest.addCheckIn("not exists");
                        sendCheckin();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            SantaHelper help = new SantaHelper(this);
            help.pic(photoURI);

        }
    }


    private void sendCheckin() {

        SenderSocket sock = new SenderSocket(Master.masterIP, Master.masterPort,
                new NetworkPayload(NetworkPayloadType.CHECK_IN, true, pointOfInterest,
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                dispatchTakePictureIntent();
            } else {
                CheckInViewActivity.this.pointOfInterest.addCheckIn("not exists");
                sendCheckin();
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                this.photoURI = FileProvider.getUriForFile(this,
                        "com.haifisch.client.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void resCall(String taf) {
        if (taf.length() != 0)
            pointOfInterest.addCheckIn("taf");
        else {
            pointOfInterest.addCheckIn("Not Exists");
        }
        sendCheckin();
    }
}
