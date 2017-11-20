package com.example.felix.gasboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        final ArrayList<TextView> digits = new ArrayList<>(6);
        digits.add(((TextView) listItemView.findViewById(R.id.hundreds)));
        digits.add(((TextView) listItemView.findViewById(R.id.decimals)));
        digits.add(((TextView) listItemView.findViewById(R.id.single)));
        digits.add(((TextView) listItemView.findViewById(R.id.ten)));
        digits.add(((TextView) listItemView.findViewById(R.id.hundred)));
        digits.add(((TextView) listItemView.findViewById(R.id.thousand)));

        final ArrayList<Button> btns = new ArrayList<>(12);
        for(int i = 1; i < 7; i++){
            String btnID = "up"+i;
            int resID = listItemView.getResources().getIdentifier(btnID, "id",
                    "com.example.felix.gasboard");
            Log.d("btn", String.valueOf(resID));
            btns.add((Button) listItemView.findViewById(resID));
        }
        for(int i = 1; i < 7; i++){
            String btnID = "down"+i;
            int resID = listItemView.getResources().getIdentifier(btnID, "id",
                    "com.example.felix.gasboard");
            btns.add((Button) listItemView.findViewById(resID));
        }

        final ListEntry x = getItem(position);

        ((TextView) listItemView.findViewById(R.id.nameField)).setText(x.getName());

        switch(x.getWidth()){
            case 4:
                digits.get(3).setVisibility(View.VISIBLE);

                listItemView.findViewById(R.id.up3).setClickable(true);
                listItemView.findViewById(R.id.down3).setClickable(true);

                digits.get(0).setText(String.valueOf(x.getNumbers()[0]));
                digits.get(1).setText(String.valueOf(x.getNumbers()[1]));
                digits.get(2).setText(String.valueOf(x.getNumbers()[2]));
                digits.get(3).setText(String.valueOf(x.getNumbers()[3]));
                break;
            case 5:
                digits.get(3).setVisibility(View.VISIBLE);
                digits.get(4).setVisibility(View.VISIBLE);

                listItemView.findViewById(R.id.up3).setClickable(true);
                listItemView.findViewById(R.id.down3).setClickable(true);
                listItemView.findViewById(R.id.up2).setClickable(true);
                listItemView.findViewById(R.id.down2).setClickable(true);

                digits.get(0).setText(String.valueOf(x.getNumbers()[0]));
                digits.get(1).setText(String.valueOf(x.getNumbers()[1]));
                digits.get(2).setText(String.valueOf(x.getNumbers()[2]));
                digits.get(3).setText(String.valueOf(x.getNumbers()[3]));
                digits.get(4).setText(String.valueOf(x.getNumbers()[4]));
                break;
            case 6:
                digits.get(3).setVisibility(View.VISIBLE);
                digits.get(4).setVisibility(View.VISIBLE);
                digits.get(5).setVisibility(View.VISIBLE);

                listItemView.findViewById(R.id.up3).setClickable(true);
                listItemView.findViewById(R.id.down3).setClickable(true);
                listItemView.findViewById(R.id.up2).setClickable(true);
                listItemView.findViewById(R.id.down2).setClickable(true);
                listItemView.findViewById(R.id.up1).setClickable(true);
                listItemView.findViewById(R.id.down1).setClickable(true);


                digits.get(0).setText(String.valueOf(x.getNumbers()[0]));
                digits.get(1).setText(String.valueOf(x.getNumbers()[1]));
                digits.get(2).setText(String.valueOf(x.getNumbers()[2]));
                digits.get(3).setText(String.valueOf(x.getNumbers()[3]));
                digits.get(4).setText(String.valueOf(x.getNumbers()[4]));
                digits.get(5).setText(String.valueOf(x.getNumbers()[5]));
                break;
            default:
                digits.get(0).setText(String.valueOf(x.getNumbers()[0]));
                digits.get(1).setText(String.valueOf(x.getNumbers()[1]));
                digits.get(2).setText(String.valueOf(x.getNumbers()[2]));
                break;
        }

        View.OnClickListener manipulate = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.getId() == R.id.up1)
                {
                    increase(digits.get(5), x, 5);
                }
                if(v.getId() == R.id.up2)
                {
                    increase(digits.get(4), x, 4);
                }
                if(v.getId() == R.id.up3)
                {
                    increase(digits.get(3), x, 3);
                }
                if(v.getId() == R.id.up4)
                {
                    increase(digits.get(2), x, 2);
                }
                if(v.getId() == R.id.up5)
                {
                    increase(digits.get(1), x, 1);
                }
                if(v.getId() == R.id.up6)
                {
                    increase(digits.get(0), x, 0);
                }
                if(v.getId() == R.id.down1)
                {
                    decrease(digits.get(5), x, 5);
                }
                if(v.getId() == R.id.down2)
                {
                    decrease(digits.get(4), x, 4);
                }
                if(v.getId() == R.id.down3)
                {
                    decrease(digits.get(3), x, 3);
                }
                if(v.getId() == R.id.down4)
                {
                    decrease(digits.get(2), x, 2);
                }
                if(v.getId() == R.id.down5)
                {
                    decrease(digits.get(1), x, 1);
                }
                if(v.getId() == R.id.down6)
                {
                    decrease(digits.get(0), x, 0);
                }
            }
        };

        for (Button b : btns) {
            b.setOnClickListener(manipulate);
        }

        return listItemView;
    }

    private void increase(TextView v, ListEntry x, int i){
        int nr = Integer.parseInt(v.getText().toString());
        if(++nr>9)
            nr = 0;
        v.setText(String.valueOf(nr));
        x.getNumbers()[i] = nr;
    }

    private void decrease(TextView v, ListEntry x, int i){
        int nr = Integer.parseInt(v.getText().toString());
        if(--nr<0)
            nr = 9;
        v.setText(String.valueOf(nr));
        x.getNumbers()[i] = nr;
    }

}
