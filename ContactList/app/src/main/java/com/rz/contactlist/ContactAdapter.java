package com.rz.contactlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rz.contactlist.MainActivity.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    Context mContext;
    int layoutResourceId;
    ArrayList<Contact> data = new ArrayList<>();

    /*
     * @mContext - app context
     *
     * @layoutResourceId - the listview_item_row.xml
     *
     * @data - the ListItem data
     */
    public ContactAdapter(Context mContext, int layoutResourceId, ArrayList<Contact> data) {

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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        // inflate the listview_item_row.xml parent
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        // get the elements in the layout
        ImageView imageViewContactIcon = (ImageView) listItem
                .findViewById(R.id.imageViewContactIcon);

        TextView textViewContactName = (TextView) listItem
                .findViewById(R.id.textViewContactName);

        TextView textViewContactPhone = (TextView) listItem
                .findViewById(R.id.textViewContactPhone);

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
        Contact Contact = data.get(position);

        imageViewContactIcon.setImageResource(Contact.contactIcon);
        textViewContactName.setText(Contact.contactName);
        textViewContactPhone.setText(Contact.contactPhone);

        return listItem;
    }

}
