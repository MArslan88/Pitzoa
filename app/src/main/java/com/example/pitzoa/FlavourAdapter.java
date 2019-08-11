package com.example.pitzoa;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FlavourAdapter extends ArrayAdapter<Flavour> {

    private int mColorResourceID;

    public FlavourAdapter(Activity context, ArrayList<Flavour> flavours) {
        super(context, 0, flavours);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link currentWord} object located at this position in the list
        Flavour currentFlavour = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.flavour_heading);
        // Get the flavour from the current Flavour object and
        // set this text on the heading TextView
        miwokTextView.setText(currentFlavour.getmFlavourHeading());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.flavour_detail);

        defaultTextView.setText(currentFlavour.getmFlavourDetail());

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if(currentFlavour.hasImage()){
            // Set the ImageView to the image resource specified in the current Word
            imageView.setImageResource(currentFlavour.getmImageResourceId());

            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        }else{
            // Otherwise hide the ImageView (set visibility to GONE)
            imageView.setVisibility(View.GONE);
        }

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
