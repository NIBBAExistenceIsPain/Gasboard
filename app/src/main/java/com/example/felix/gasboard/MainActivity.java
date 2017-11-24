package com.example.felix.gasboard;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Scanner;


public class MainActivity extends AppCompatActivity
{
    Button b1;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //initialising activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating views
        b1 = (Button) findViewById(R.id.btnToList);
    }


    @Override
    //Confirm for second click on back
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



    public void goToList(View view){
        Intent intent = new Intent(this, EditingScreen.class);
        startActivity(intent);

    }

    @SuppressLint("ValidFragment")
    public class LoginFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton(R.string.Password, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Button btn = (Button) findViewById(R.id.item1);
                            btn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Scanner sc = new Scanner(System.in);

                                    int i = sc.nextInt();

                                    if ( 970074==i) {
                                        android.R.string.ok;
                                    } else {
                                        android.R.string.cancel;
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            return builder.create();
        }
    }


}

