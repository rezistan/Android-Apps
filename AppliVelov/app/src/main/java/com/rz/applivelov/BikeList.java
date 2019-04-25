package com.rz.applivelov;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BikeList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_list);

        myAsyncTask donnees = new myAsyncTask();

        ArrayList<Station> stationsData = new ArrayList<>();
        if(accesInternet()) {
            try {
                stationsData = donnees.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            final ListView listViewArticles;
            final StationsAdapter adapter = new StationsAdapter(this,
                    R.layout.listview_item_row, stationsData);


            listViewArticles = (ListView) findViewById(R.id.listView1);
            listViewArticles.setAdapter(adapter);

            final ArrayList<Station> finalStationsData = stationsData;

            listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Station current = finalStationsData.get(i);

                    AlertDialog.Builder adb = new AlertDialog.Builder(BikeList.this, R.style.AlertDialogCustom);
                    adb.setTitle(current.getName());
                    adb.setMessage(current.toString());
                    adb.setPositiveButton("Ok", null);
                    adb.show();
                }
            });
        }
        else{
            Toast noInternet = Toast.makeText(getApplicationContext(), R.string.internetPrompt, Toast.LENGTH_SHORT);
            finish();
            noInternet.show();
        }
    }


    public boolean accesInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());

    }
}
