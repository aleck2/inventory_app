package com.example.lucas.teset;
/**
 * Created by Lucas on 4/28/18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class ItemAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<Item> dataArray;

    public ItemAdapter(Context c, ArrayList<Item> inputData) {
        super(c, 0, inputData);
        mContext = c;
        dataArray = inputData;
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Item getItem(int position) {
        return dataArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }

        // Get the data item for this position
        Item myItem = getItem(position);

        // Lookup view for data population
        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        TextView userNameView = (TextView) convertView.findViewById(R.id.username);
        TextView endDateView = (TextView) convertView.findViewById(R.id.endDate);
        TextView sellerEmailView = (TextView) convertView.findViewById(R.id.sellerEmail);
        TextView locationView = (TextView) convertView.findViewById(R.id.location);

        ImageView img=(ImageView) convertView.findViewById(R.id.imageView);

        Glide.with(mContext).load(myItem.getImageURL()).into(img); // use Glide library for image caching (otherwise constantly reload pictures when scrolling)


        titleView.setText(myItem.getTitle());
        userNameView.setText(myItem.getSellerID());
        sellerEmailView.setText(myItem.getSellerEmail());
        endDateView.setText(myItem.getEndDateString());
        locationView.setText(myItem.getPackageIdentifier());

        return convertView;

    }


}