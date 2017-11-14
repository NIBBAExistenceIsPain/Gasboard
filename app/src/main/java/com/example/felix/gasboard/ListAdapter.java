package com.example.felix.gasboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Stacra on 13/11/2017.
 */

public class ListAdapter extends ArrayAdapter<ListEntry> {

    public ListAdapter(Context context, ArrayList<ListEntry> resource) {
        super(context, 0, resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.preset_1, parent, false);
        }

        ListEntry x = getItem(position);

        ((TextView) listItemView.findViewById(R.id.nameField)).setText(x.getName());

        switch(x.getWidth()){
            case 4:
                ((TextView) listItemView.findViewById(R.id.ten)).setVisibility(View.VISIBLE);
                Log.d("List", "Passed");
                //Log.d("Item", String.valueOf(x.getNumbers().length));

                ((TextView) listItemView.findViewById(R.id.hundreds)).setText(String.valueOf(x.getNumbers()[0]));
                ((TextView) listItemView.findViewById(R.id.decimals)).setText(String.valueOf(x.getNumbers()[1]));
                ((TextView) listItemView.findViewById(R.id.single)).setText(String.valueOf(x.getNumbers()[2]));
                ((TextView) listItemView.findViewById(R.id.ten)).setText(String.valueOf(x.getNumbers()[3]));
                break;
            case 5:
                ((TextView) listItemView.findViewById(R.id.ten)).setVisibility(View.VISIBLE);
                ((TextView) listItemView.findViewById(R.id.hundred)).setVisibility(View.VISIBLE);

                ((TextView) listItemView.findViewById(R.id.hundreds)).setText(String.valueOf(x.getNumbers()[0]));
                ((TextView) listItemView.findViewById(R.id.decimals)).setText(String.valueOf(x.getNumbers()[1]));
                ((TextView) listItemView.findViewById(R.id.single)).setText(String.valueOf(x.getNumbers()[2]));
                ((TextView) listItemView.findViewById(R.id.ten)).setText(String.valueOf(x.getNumbers()[3]));
                ((TextView) listItemView.findViewById(R.id.hundred)).setText(String.valueOf(x.getNumbers()[4]));
                break;
            case 6:
                ((TextView) listItemView.findViewById(R.id.ten)).setVisibility(View.VISIBLE);
                ((TextView) listItemView.findViewById(R.id.hundred)).setVisibility(View.VISIBLE);
                ((TextView) listItemView.findViewById(R.id.thousand)).setVisibility(View.VISIBLE);

                ((TextView) listItemView.findViewById(R.id.hundreds)).setText(String.valueOf(x.getNumbers()[0]));
                ((TextView) listItemView.findViewById(R.id.decimals)).setText(String.valueOf(x.getNumbers()[1]));
                ((TextView) listItemView.findViewById(R.id.single)).setText(String.valueOf(x.getNumbers()[2]));
                ((TextView) listItemView.findViewById(R.id.ten)).setText(String.valueOf(x.getNumbers()[3]));
                ((TextView) listItemView.findViewById(R.id.thousand)).setText(String.valueOf(x.getNumbers()[5]));
                break;
            default:
                ((TextView) listItemView.findViewById(R.id.hundreds)).setText(String.valueOf(x.getNumbers()[0]));
                ((TextView) listItemView.findViewById(R.id.decimals)).setText(String.valueOf(x.getNumbers()[1]));
                ((TextView) listItemView.findViewById(R.id.single)).setText(String.valueOf(x.getNumbers()[2]));
                break;
        }


        return listItemView;
    }

}
