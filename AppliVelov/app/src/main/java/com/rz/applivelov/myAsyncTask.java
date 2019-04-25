package com.rz.applivelov;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sony Vaio on 23/03/2018.
 */

public class myAsyncTask extends AsyncTask<String , Void , ArrayList<Station> > {

        private ArrayList<Station> stationsData = new ArrayList<>();

        @Override
        protected ArrayList<Station> doInBackground(String... strings) {
            URL url;
            String location = "Lyon";
            String myAPIkey = "0c2dd115dad612763e8030ed01ea4696df67ee94";

            HttpsURLConnection urlConnection = null;

            try {
                url = new URL("https://api.jcdecaux.com/vls/v1/stations?contract="+location+"&apiKey="+myAPIkey);

                urlConnection = (HttpsURLConnection) url
                        .openConnection();


                //String builder
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String inputS = "";

                try {
                    while ((inputS = reader.readLine()) != null) {
                        builder.append(inputS);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String text = builder.toString();

                // parsage du flux
                JSONArray jsonarray = new JSONArray(text);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    int number = jsonobject.getInt("number");
                    String name = jsonobject.getString("name");
                    String address = jsonobject.getString("address");
                    double lat = jsonobject.getJSONObject("position").getDouble("lat");
                    double lng = jsonobject.getJSONObject("position").getDouble("lng");
                    boolean banking = jsonobject.getBoolean("banking");
                    boolean bonus = jsonobject.getBoolean("bonus");
                    String status = jsonobject.getString("status");
                    String contractName = jsonobject.getString("contract_name");
                    int bikeStands = jsonobject.getInt("bike_stands");
                    int availableBikeStands = jsonobject.getInt("available_bike_stands");
                    int availableBikes = jsonobject.getInt("available_bikes");
                    long LonglastUpdate = jsonobject.getLong("last_update");
                    Date lastUpdate = new Date(LonglastUpdate);

                    Station current = new Station(number, name, address, lat, lng, banking, bonus, status, contractName, bikeStands, availableBikeStands, availableBikes, lastUpdate);
                    stationsData.add(current);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            //Tri des stations par numéro
            Collections.sort(stationsData, new Comparator<Station>(){
                @Override
                public int compare(Station station1, Station station2) {
                    return Integer.compare(station1.getNumber(), station2.getNumber());
                }
            });

            return stationsData;
        }

}
