package com.example.felix.gasboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class EditingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_screen);


        Bundle bundle = getIntent().getExtras();
        ArrayList<Integer> prices = (ArrayList<Integer>) bundle.getSerializable("prices");
        ArrayList<Integer> params = (ArrayList<Integer>) bundle.getSerializable("params");
        int y = 5;
        final ArrayList<ListEntry> list = new ArrayList<ListEntry>();
        for(int i = 0; i < prices.get(3);i++)
        {
            list.add(new ListEntry(prices.get(2),getPrice(y,prices),"PRICE"));
            y += 3;
        }

        ListAdapter adapter = new ListAdapter(this, list);
        ListView listView = (ListView) findViewById(R.id.listLayout);
        listView.setAdapter(adapter);
    }

    private int[] getPrice(int i, ArrayList<Integer> prices)
    {

        String decs = ("00" + Integer.toHexString(prices.get(i))).substring(Integer.toHexString(prices.get(i)).length());
        String ones = ("00" + Integer.toHexString(prices.get(i+1))).substring(Integer.toHexString(prices.get(i+1)).length());
        String hundreds = ("00" + Integer.toHexString(prices.get(i+2))).substring(Integer.toHexString(prices.get(i+2)).length());
        Log.d("string1", decs);
        Log.d("string1", ones);
        Log.d("string1", hundreds);
        int[] b = new int[6];
        b[0] = Character.getNumericValue(decs.charAt(1)); // returns asci code
        b[1] = Character.getNumericValue(decs.charAt(0));
        b[2] = Character.getNumericValue(ones.charAt(1));
        b[3] = Character.getNumericValue(ones.charAt(0));
        b[4] = Character.getNumericValue(hundreds.charAt(1));
        b[5] = Character.getNumericValue(hundreds.charAt(0));

        return b;

    }

    private byte decimalToBCD(byte dec) {
        return 0;
    }

    private byte BCDtoDecimal(byte bcd) {
        return 0;
    }
}
