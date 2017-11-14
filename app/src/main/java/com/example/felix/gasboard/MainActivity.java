package com.example.felix.gasboard;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback
{
    Button b1;
    Bluetooth bt;
    TextView  tv;
    CharSequence text = "";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //initialising activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //instantiating views
        b1 = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);

        //set up debug toast. Change text before displaying message
        Context context = getApplicationContext();
        final Toast toast = Toast.makeText(context, text,Toast.LENGTH_SHORT);

        //create onClick event handler
        View.OnClickListener listen = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.getId() == R.id.button)
                {
                    bt.enableBluetooth();
                    bt.connectToName("hc06");
                    bt.send((byte)05);
                    Log.d("MSG","I send somethin" );
                }
            }
        };
        b1.setOnClickListener(listen);

        // Set up blutooth connection
        bt = new Bluetooth(this);
        bt.setCommunicationCallback(this);





    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    @Override
    public void onConnect(BluetoothDevice device) {

    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {

    }

    @Override
    public void onMessage(int message) {
        text = Integer.toString(message);
        Log.d("MSG", (String) text);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

    }

    public void goToList(View view){
        Intent intent = new Intent(this, EditingScreen.class);
        startActivity(intent);
    }
}
