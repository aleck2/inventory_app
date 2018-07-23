package com.example.lucas.teset;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * Created by Lucas on 7/16/18.
 */

public class ViewNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notification);

        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView priceTextView = (TextView) findViewById(R.id.totalPrice);
        TextView urlTextView = (TextView) findViewById(R.id.url);


        ImageView thumbnail = (ImageView) findViewById(R.id.imageView);
        if (getIntent().getExtras() != null) {
            Log.i("ViewNotification", "here");
            titleTextView.setText((String) getIntent().getExtras().get("title"));
            priceTextView.setText((String) getIntent().getExtras().get("price"));
            urlTextView.setText((String) getIntent().getExtras().get("url"));
        }


        Context mContext = this;

        Glide.with(mContext).load("https://images.pexels.com/photos/104827/cat-pet-animal-domestic-104827.jpeg?cs=srgb&dl=animal-animal-photography-cat-104827.jpg&fm=jpg").into(thumbnail); // use Glide library for image caching (otherwise constantly reload pictures when scrolling)



    }
}
