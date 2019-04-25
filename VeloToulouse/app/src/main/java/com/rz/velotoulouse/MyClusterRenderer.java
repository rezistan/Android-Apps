package com.rz.velotoulouse;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyClusterRenderer extends DefaultClusterRenderer<Station> {

    private final Context mContext;

    public MyClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<Station> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(Station item, MarkerOptions markerOptions) {
        if(item.getAvailableBikes() > 0 && item.getAvailableBikes() < 5){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        else if(item.getAvailableBikes() >= 5) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
    }
}