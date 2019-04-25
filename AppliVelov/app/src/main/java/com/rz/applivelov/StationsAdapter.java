package com.rz.applivelov;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sony Vaio on 25/03/2018.
 */

public class StationsAdapter extends ArrayAdapter<Station>{
    private Context mContext;
    private int layoutResourceId;
    ArrayList<Station> data = new ArrayList<>();

    /*
     * @mContext - app context
     *
     * @layoutResourceId - the listview_item_row.xml
     *
     * @data - the ListItem data
     */
    StationsAdapter(BikeList mContext, int layoutResourceId, ArrayList<Station> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    /*
     * @We'll overried the getView method which is called for every ListItem we
     * have.
     *
     * @There are lots of different caching techniques for Android ListView to
     * achieve better performace especially if you are going to have a very long
     * ListView.
     */
    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        // inflate the listview_item_row.xml parent
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        // get the elements in the layout
        TextView textViewStationName = (TextView) listItem
                .findViewById(R.id.textViewStationName);

        TextView textViewStationDetails = (TextView) listItem
                .findViewById(R.id.textViewStationDetails);

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
        Station Station = data.get(position);

        textViewStationName.setText(Station.getName());
        textViewStationDetails.setText(Station.available());

        return listItem;
    }
}
