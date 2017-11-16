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
    CharSequence text;
    boolean doubleBackToExitPressedOnce = false;
    boolean recieving = false;
    ArrayList<Byte> priceList;
    ArrayList<Byte> paramList;
    ArrayList<Byte> temp;

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
        temp = new ArrayList<>();
        byte[] b = new byte[]{(byte) 0xEE, (byte) 0xC1,0x03 ,0x05 ,0x02 ,0x10 ,0x02 ,0x00,0x43, 0x05 ,0x00 ,0x76 ,0x08 ,0x00 ,0x09 ,0x01 ,0x00 ,0x32 ,0x04 ,0x00 , (byte) 0xE0, (byte) 0xE5};
        for (byte b2 : b) {
            priceList.add(b2);
        }

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
                    Log.d("MSG","I connected to hc06" );
                }
            }
        };
        b1.setOnClickListener(listen);

        // Set up blutooth connection
        bt = new Bluetooth(this);
        bt.setCommunicationCallback(this);





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
        // Disconnect happens here
        // Will return to main menu and disable the move button.
    }

    @Override
    public void onMessage(byte message) {

        //if message = D1 success prompt
        if(message == 238 || recieving == true)
        {
            recieving = true;
            temp.add(message);
        }
        if(message == 229 && recieving == true)
        {
            recieving = false;
            if(temp.get(1) == 193)
                priceList = new ArrayList<>(temp);
            else
                paramList = new ArrayList<>(temp);
            temp.clear();
        }

        Log.d("MSG", (String) text);
    }

    @Override
    public void onError(String message) {
        //Handle exceptions here
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message)
    {
        //Error: Unable to connect here
    }

    public void goToList(View view){
        Intent intent = new Intent(this, EditingScreen.class);
        intent.putExtra("prices", priceList);
        intent.putExtra("params", paramList);
        startActivity(intent);

    }
}
