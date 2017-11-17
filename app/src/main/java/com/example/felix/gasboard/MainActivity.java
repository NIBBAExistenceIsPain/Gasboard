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

import java.util.ArrayList;

import Bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback
{
    Button b1;
    Bluetooth bt;
    TextView  tv;
    String text;
    boolean doubleBackToExitPressedOnce = false;
    boolean recieving = false;
    ArrayList<Integer> priceList;
    ArrayList<Integer> paramList;
    ArrayList<Integer> temp;

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

        priceList = new ArrayList<>();
        paramList = new ArrayList<>();
        temp = new ArrayList<Integer>();
        //int[] b = new int[]{ 0xEE, 0xC1,0x03 ,0x05 ,0x02 ,0x10 ,0x02 ,0x00,0x43, 0x05 ,0x00 ,0x76 ,0x08 ,0x00 ,0x09 ,0x01 ,0x00 ,0x32 ,0x04 ,0x00 , 0xE0, 0xE5};
        //for (int b2 : b) {
        //    priceList.add(b2);
        //}

        //create onClick event handler
        View.OnClickListener listen = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.getId() == R.id.button)
                {

                    Log.d("MSG","I connected to hc06" );
                }
            }
        };
        b1.setOnClickListener(listen);

        // Set up blutooth connection
        bt = new Bluetooth(this);
        bt.enableBluetooth();
        bt.setCommunicationCallback(this);
        bt.connectToName("hc06");




    }

    //Confirm for second click on back
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
    public void onConnect(BluetoothDevice device)
    {
        // Successful connect message goes here
        // Will enable the move button, which will be disabled.
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message)
    {
        Log.d("MSG", "good");
    }

    @Override
    public void onMessage(byte message) {
        //text = message;
        Log.d("MSG", String.valueOf(message));
        Log.d("MSG", "ss" + Integer.toHexString(message & 0xff));
        //if message = D1 success prompt
        if(message == -18 || recieving == true)
        {
            Log.d("MSG", String.valueOf(message));
            recieving = true;
            temp.add((int)message);

        }
        if(message == -27 && recieving == true)
        {
            Log.d("MSG", String.valueOf(message));
            recieving = false;
            if(temp.get(1) == -63)
                priceList = new ArrayList<>(temp);
            else
                paramList = new ArrayList<>(temp);
            temp.clear();
        }


    }

    @Override
    public void onError(String message) {
        //Handle exceptions here
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message)
    {
        Log.d("MSG", "bad");
    }

    public void goToList(View view){
        Intent intent = new Intent(this, EditingScreen.class);
        intent.putExtra("prices", priceList);
        intent.putExtra("params", paramList);
        startActivity(intent);

    }
}
