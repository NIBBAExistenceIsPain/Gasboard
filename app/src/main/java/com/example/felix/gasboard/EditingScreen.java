package com.example.felix.gasboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_screen);


        Bundle bundle = getIntent().getExtras();
        ArrayList<Byte> prices = (ArrayList<Byte>) bundle.getSerializable("prices");
        ArrayList<Byte> params = (ArrayList<Byte>) bundle.getSerializable("params");
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

    private byte[] getPrice(int i, ArrayList<Byte> prices)
    {
        byte[] b = new byte[6];
        b[0] = (byte) (prices.get(i) % 10);
        b[1] = (byte) (prices.get(i) / 10);
        b[2] = (byte) (prices.get(i+1) % 10);
        b[3] = (byte) (prices.get(i+1) / 10);
        b[4] = (byte) (prices.get(i+2) % 10);
        b[5] = (byte) (prices.get(i+2) / 10);

        return b;

    }

    private byte decimalToBCD(byte dec) {
        return 0;
    }

    private byte BCDtoDecimal(byte bcd) {
        return 0;
    }
}
