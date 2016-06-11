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

public class ConnectionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        if (!((Master) getApplication()).communicator.created)
            Toast.makeText(this, "Failed to initialize socket", Toast.LENGTH_SHORT).show();
    }

    public void click(View view) {
        String ip = ((EditText) findViewById(R.id.ip_address)).getText().toString();
        int port = Integer.parseInt(((EditText) findViewById(R.id.port_no)).getText().toString());
        NetworkPayload payload = new NetworkPayload(NetworkPayloadType.CONNECTION_ACK,
                false, new ConnectionAcknowledge(0, Communicator.address,
                Communicator.port), Communicator.address, Communicator.port, 200, "OK");
        SenderSocket sock = new SenderSocket(ip, port, payload);
        Thread t = new Thread(sock);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect to master", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sock.isSent()) {
            Master.masterIP = ip;
            Master.masterPort = port;
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Failed to connect to master", Toast.LENGTH_SHORT).show();
        }


    }

}
