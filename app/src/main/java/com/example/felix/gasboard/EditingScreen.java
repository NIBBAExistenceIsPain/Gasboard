package com.example.felix.gasboard;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Bluetooth.Bluetooth;

public class EditingScreen extends AppCompatActivity implements Bluetooth.CommunicationCallback
{   //Declarations
    Bluetooth bt;
    boolean recieving = false;
    ArrayList<Integer> priceList;
    ArrayList<Integer> paramList;
    ArrayList<Integer> temp;
    boolean doubleBackToExitPressedOnce = false;
    ListAdapter adapter;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {   //Set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_screen);

        // Set up bluetooth connection
        bt = new Bluetooth();
        bt.setCommunicationCallback(this);
        bt.enableBluetooth();
        bt.connectToName("hc06");

        while(!(bt.isConnected()))
        {
            //wait to connect!
        }

        // Instantiate arryas
        priceList = new ArrayList<>();
        paramList = new ArrayList<>();
        temp = new ArrayList<Integer>();

        int[] SEND_REQUEST = new int[]{0xEE, 0xB4, 0xB4, 0xE5}; // Request prices
        for(int i = 0; i<SEND_REQUEST.length; i++)
        {
            bt.send((byte) SEND_REQUEST[i]);
        }
        while(priceList.isEmpty())
        {
            //wait to receive prices
        }

        SEND_REQUEST = new int[]{0xEE, 0xB5, 0xB6, 0xE5}; // Request params
        for(int i = 0; i<SEND_REQUEST.length; i++)
        {
            bt.send((byte) SEND_REQUEST[i]);
        }
        while(paramList.isEmpty())
        {
            //wait to receive params
        }

        //Add prices to list
        int y = 5;
        final ArrayList<ListEntry> list = new ArrayList<ListEntry>();
        size = priceList.get(3);
        for(int i = 0; i < priceList.get(3);i++)
        {
            list.add(new ListEntry(priceList.get(2), priceBCDToDec(y,priceList),"PRICE"));
            y += 3;
        }
        //if param.get(3) display clock

        //Set Up ListView
        adapter = new ListAdapter(this, list);
        ListView listView = (ListView) findViewById(R.id.listLayout);
        listView.setAdapter(adapter);

    }

    @Override
    //Shows confirmation message and enables connect button.
    public void onConnect(BluetoothDevice device)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(EditingScreen.this, "Bluetooth connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    //Shows error message and returns to main screen
    public void onDisconnect(BluetoothDevice device, String message)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(EditingScreen.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
        //returns to main activity on disconnect and clears activity stack
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onMessage(byte message)
    {

        Log.d("MSG", String.valueOf(message));
        Log.d("MSG", "" + Integer.toHexString(message & 0xff));

        if(message == (byte) 0xD1)
        {
            runOnUiThread(new Runnable() {
                public void run()
                {
                    //Toast.makeText(MainActivity.this, "Sent Successfully.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(message == (byte)0xEE || recieving)
        {
            Log.d("MSG", String.valueOf(message));
            recieving = true;
            temp.add((int)message);

        }
        if(message == (byte) 0xE5 && recieving) {
            if (checkSum(temp) == temp.get(temp.size() - 2)) {
                Log.d("true", "good chacksum");
                recieving = false;
                if (temp.get(1) == (byte) 0xC1)
                {
                    Log.d("true", "price");
                    priceList = new ArrayList<>(temp);
                }
                else if(temp.get(1) == (byte) 0xC2)
                    Log.d("true", "param");
                    paramList = new ArrayList<>(temp);
                temp.clear();
            }
            else
            {
                recieving = false;
                Log.d("false", "bad chacksum");
            }
        }
    }

    @Override
    public void onError(final String message)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(EditingScreen.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnectError(BluetoothDevice device, final String message)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(EditingScreen.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int[] getPrice(int i, ArrayList<Integer> prices)
    {
        String decs = ("00" + Integer.toHexString(prices.get(i))).substring(Integer.toHexString(prices.get(i)).length());
        String ones = ("00" + Integer.toHexString(prices.get(i+1))).substring(Integer.toHexString(prices.get(i+1)).length());
        String hundreds = ("00" + Integer.toHexString(prices.get(i+2))).substring(Integer.toHexString(prices.get(i+2)).length());
        int[] b = new int[6];
        b[0] = Character.getNumericValue(decs.charAt(1)); // returns asci code
        b[1] = Character.getNumericValue(decs.charAt(0));
        b[2] = Character.getNumericValue(ones.charAt(1));
        b[3] = Character.getNumericValue(ones.charAt(0));
        b[4] = Character.getNumericValue(hundreds.charAt(1));
        b[5] = Character.getNumericValue(hundreds.charAt(0));

        return b;

    }
    
    @Override
    //Confirm for second click on back
    public void onBackPressed()
    {
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

    private int[] priceBCDToDec(int i, ArrayList<Integer> prices)
    {
        String decs = ("00" + Integer.toHexString(prices.get(i))).substring(Integer.toHexString(prices.get(i)).length());
        String ones = ("00" + Integer.toHexString(prices.get(i+1))).substring(Integer.toHexString(prices.get(i+1)).length());
        String hundreds = ("00" + Integer.toHexString(prices.get(i+2))).substring(Integer.toHexString(prices.get(i+2)).length());
        int[] b = new int[6];
        b[0] = Character.getNumericValue(decs.charAt(1)); // returns asci code
        b[1] = Character.getNumericValue(decs.charAt(0));
        b[2] = Character.getNumericValue(ones.charAt(1));
        b[3] = Character.getNumericValue(ones.charAt(0));
        b[4] = Character.getNumericValue(hundreds.charAt(1));
        b[5] = Character.getNumericValue(hundreds.charAt(0));

        return b;
    }

    private static int checkSum(ArrayList<Integer> list)
    {
        int temp = list.get(0);
        for(int i = 1 ; i < list.size() - 2 ;i++)
        {
            if((temp^ list.get(i)) < 0)
            {
                temp = (byte) (((byte)(temp^ list.get(i)) << 1)+1);
            }
            else
            {
                temp = (byte) ((byte)(temp^ list.get(i)) << 1);
            }
        }

        if(temp == (byte)0xEE || temp == (byte)0xE5)
            temp -= 0x0E;

        return temp;
    }

    public void priceDecToBCD(View view)
    {
        ArrayList<Integer> bcd = new ArrayList<>();
        bcd.add(0xEE);
        bcd.add(0xB1);
        bcd.add(0x02);
        for(int y = 0; y < size; y++)
        {
            int[] a = adapter.getItem(y).getNumbers();
            bcd.add(a[0] + 16 * a[1]);
            bcd.add(a[2] + 16 * a[3]);
            bcd.add(a[4] + 16 * a[5]);
        }
        bcd.add(1);
        bcd.add(0xEE);
        System.out.println(Integer.toHexString(bcd.get(bcd.size()-2)));
        bcd.set(bcd.size()-2,checkSum(bcd));

        for(int y = 0; y<bcd.size(); y++) {
            int b = bcd.get(y);
            bt.send((byte) b);
        }

    }

}
