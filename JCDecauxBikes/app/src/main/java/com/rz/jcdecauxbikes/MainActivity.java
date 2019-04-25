package com.rz.jcdecauxbikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    public static String city;

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

        //Getting the list of cities//////////////////////////////
        myAsyncTask donnees = new myAsyncTask(MainActivity.city);

        ArrayList<Station> stationsData = new ArrayList<>();
        try {
            stationsData = donnees.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<String> cities = new ArrayList<>();
        for(int i=0; i<stationsData.size(); i++){
            String contractName = stationsData.get(i).getContractName();
            cities.add(contractName);
        }
        Set<String> citySet = new HashSet<>(cities); //In order to select distinct cities
        cities = new ArrayList<>(citySet);
        Collections.sort(cities);

        ///////////////////////////////////////////////////////////

        //Filling the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCityChoice);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //spinner.setSelection(cities.indexOf(getString(R.string.hometown)));

        //Handling user's selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
