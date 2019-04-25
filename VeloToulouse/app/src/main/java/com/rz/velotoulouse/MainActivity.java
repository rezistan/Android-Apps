package com.rz.velotoulouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button accessList = (Button) findViewById(R.id.accessList);
        accessList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list = new Intent(MainActivity.this, BikeList.class);
                startActivity(list);
            }
        });

        final Button accessMap = (Button) findViewById(R.id.accessMap);
        accessMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(MainActivity.this, BikeMaps.class);
                startActivity(map);
            }
        });
    }
}
