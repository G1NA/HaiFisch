package com.haifisch.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import commons.ConnectionAcknowledge;
import commons.NetworkPayload;
import commons.NetworkPayloadType;
import commons.onConnectionListener;

public class InitialActivity extends AppCompatActivity implements onConnectionListener {

    public static Communicator communicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        communicator = new Communicator(this);
        communicator.execute(new Object());
        if (!communicator.created)
            Toast.makeText(this, "Failed to initialize socket", Toast.LENGTH_SHORT).show();
    }

    public void click(View view) {
        String ip = ((EditText) findViewById(R.id.ip_address)).getText().toString();
        int port = Integer.parseInt(((EditText) findViewById(R.id.port_no)).getText().toString());
        NetworkPayload payload = new NetworkPayload(NetworkPayloadType.CONNECTION_ACK,
                false, new ConnectionAcknowledge(1, Communicator.address,
                Communicator.port), Communicator.address, Communicator.port, 200, "OK");
        new Thread(new SenderSocket(ip, port, payload)).start();
    }

    @Override
    public void onConnect(NetworkPayload networkPayload) {
        if (networkPayload.PAYLOAD_TYPE == NetworkPayloadType.CONNECTION_ACK && networkPayload.STATUS == 200) {
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}
