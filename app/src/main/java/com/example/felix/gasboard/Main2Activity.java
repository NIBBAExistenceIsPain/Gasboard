package com.example.felix.gasboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    Spinner p1,p2,p3,p4;
    CheckBox p5,p7;
    SeekBar p6,p8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        p1 = (Spinner) findViewById(R.id.p1);
        p2 = (Spinner) findViewById(R.id.p2);
        p3 = (Spinner) findViewById(R.id.p3);
        p4 = (Spinner) findViewById(R.id.p4);
        p5 = (CheckBox) findViewById(R.id.p5);
        p7 = (CheckBox) findViewById(R.id.p7);
        p6 = (SeekBar) findViewById(R.id.p6);
        p8 = (SeekBar) findViewById(R.id.p8);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Integer> params = (ArrayList<Integer>) bundle.getSerializable("params");

        p1.setSelection(params.get(3)-1);
        p2.setSelection(params.get(4)-1);
        p3.setSelection(params.get(5));
        p4.setSelection(params.get(6));
        p5.setChecked(params.get(7) == 1);
        p7.setChecked(params.get(9) == 1);
        p6.setProgress(params.get(8));
        p8.setProgress(params.get(10));

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

    public void send(View view)
    {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(0xEE);
        temp.add(0xB6);
        temp.add(p1.getSelectedItemPosition());
        temp.add(p2.getSelectedItemPosition());
        temp.add(p3.getSelectedItemPosition());
        temp.add(p4.getSelectedItemPosition());
        temp.add(p5.isChecked()?1:0);
        temp.add(p6.getProgress());
        temp.add(p7.isChecked()?1:0);
        temp.add(p8.getProgress());
        temp.add(0);
        temp.add(0);
        temp.add(0xE5);
        temp.set(temp.size()-2,checkSum(temp));

        Intent returnIntent = new Intent();
        returnIntent.putExtra("params",temp);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

}
