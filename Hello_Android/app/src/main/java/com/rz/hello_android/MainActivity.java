package com.rz.hello_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button okay = (Button) findViewById(R.id.valider);
        final TextView tv = (TextView) findViewById(R.id.myText);

        okay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(tv.getVisibility() == View.VISIBLE){
                    tv.setVisibility(View.INVISIBLE);
                    okay.setText(R.string.cacher);
                }
                else{
                    tv.setVisibility(View.VISIBLE);
                    okay.setText(R.string.montrer);
                }
            }
        });


        /*
        //Affichage du nom de l'appli
        text = new TextView(this);
        text.setText(R.string.app_name);
        setContentView(text);


        //Affichage du nom de l'appli avec manipulation
        String appliName = getResources().getString(R.string.app_name);

        appliName = appliName.replace("Android", "Rezistan");

        TextView view = new TextView(this);
        view.setText(appliName);

        setContentView(view);
        */
    }
}
