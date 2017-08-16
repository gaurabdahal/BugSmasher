//Gaurab Dahal
package com.gaurabdahal.bugsmasher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends Activity {

    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1=(Button) findViewById(R.id.button1);
        b2=(Button) findViewById(R.id.button2);

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(TitleActivity.this, MainActivity.class));
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(TitleActivity.this, PrefsActivity.class));
            }
        });
    }


    @Override
    public void onBackPressed () {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    protected void onResume(){
        super.onResume();
        Assets.score=0;
        Assets.isplaying=true;
    }

}