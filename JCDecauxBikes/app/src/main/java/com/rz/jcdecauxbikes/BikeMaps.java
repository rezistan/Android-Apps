package com.rz.jcdecauxbikes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BikeMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private ClusterManager<Station> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocationPermission();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Dakar, Senegal.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* Add a marker
        LatLng Dakar = new LatLng(14.7167, -17.4677);
        LatLng Thies = new LatLng(14.791,  -16.9359);
        mMap.addMarker(new MarkerOptions().position(Dakar).title("Marker in Dakar"));
        mMap.addMarker(new MarkerOptions().position(Thies).title("Marker in Thies"));
        */

        //LatLng Lyon = new LatLng(45.7640,  4.8357);

        myAsyncTask donnees = new myAsyncTask(MainActivity.city);

        ArrayList<Station> stationsData = new ArrayList<>();
        if(accesInternet()){
            try {
                stationsData = donnees.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            mClusterManager = new ClusterManager<>(this, mMap);
            mMap.setOnCameraIdleListener(mClusterManager);

            final ArrayList<Station> finalStationsData = stationsData;
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    String infos = "";
                    for(int i = 0; i< finalStationsData.size(); i++){
                        Station current = finalStationsData.get(i);
                        if(current.getName().equals(marker.getTitle())) {
                            infos = current.toString();
                        }
                    }
                    AlertDialog.Builder adb = new AlertDialog.Builder(BikeMaps.this, R.style.AlertDialogCustom);
                    adb.setTitle(marker.getTitle());
                    adb.setMessage(infos);
                    adb.setPositiveButton("Ok", null);
                    adb.show();
                }
            });
            mMap.setOnMarkerClickListener(mClusterManager);
            mClusterManager.addItems(stationsData);
            mClusterManager.cluster();

            final MyClusterRenderer renderer = new MyClusterRenderer(this, mMap, mClusterManager);
            mClusterManager.setRenderer(renderer);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if(myLocation != null){
                    LatLng myLoc = new LatLng(myLocation.getLatitude(),  myLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                }

            } else {
                // Show rationale and request permission.
            }
        }
        else{
            Toast noInternet = Toast.makeText(getApplicationContext(), R.string.internetPrompt, Toast.LENGTH_SHORT);
            finish();
            noInternet.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    public boolean accesInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting());

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BikeMaps.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                BikeMaps.super.recreate();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
