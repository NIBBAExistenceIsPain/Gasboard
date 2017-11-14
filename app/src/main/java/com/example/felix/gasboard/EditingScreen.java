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



        final ArrayList<ListEntry> list = new ArrayList<ListEntry>();
        list.add(new ListEntry(4, new int[] {4, 5, 4, 9}, "ZeShower"));
        list.add(new ListEntry(5, new int[] {4, 5, 4, 9, 8}, "ZeSmasher"));
        list.add(new ListEntry(6, new int[] {4, 5, 4, 9, 8, 7}, "ZeCashew"));
        list.add(new ListEntry(5, new int[] {8, 0, 0, 8, 5}, "ZeBoobs"));

        ListAdapter adapter = new ListAdapter(this, list);
        ListView listView = (ListView) findViewById(R.id.listLayout);
        listView.setAdapter(adapter);
    }
}
