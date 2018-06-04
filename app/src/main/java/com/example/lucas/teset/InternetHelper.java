package com.example.lucas.teset;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lucas on 6/1/18.
 */

public class InternetHelper implements Runnable{
    private ArrayList<Item> myItems = null;
    private ItemAdapter adapter = null;

    public InternetHelper(ArrayList<Item> _myItems, ItemAdapter _adapter) {
        myItems = _myItems;
        adapter = _adapter;
    }

    public void run() {
        String result = null;
        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://10.0.0.20:5000/getUndelivered");
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
            Log.i("result", result);


        } catch (Exception e) {
            Log.i("main", "read http error");
            Log.i("main", e.toString());
        } finally {
            urlConnection.disconnect();
        }

        try {

            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    // TODO get rid of email and use name instead
                    JSONObject object = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String itemID = object.getString("itemID");
                    String endDate = object.getString("endTime");
                    String itemURL = object.getString("url");
                    String imageURL = object.getString("pictureURL");
                    String trackingNumber = object.getString("trackingNumber");
                    String title = object.getString("title");
                    String zipCode = object.getString("zipCode");
                    String sellerID = object.getString("sellerID");
                    String sellerEmail = object.getString("sellerEmail");
                    Double price = object.getDouble("totalPricePaid");
                    int quantity = object.getInt("quantity");
                    // assuming only receiving undelivered items from webservice
                    myItems.add(new Item(itemID, endDate, itemURL, imageURL, trackingNumber, title, sellerID, sellerEmail, zipCode, price, quantity, false));


                    } catch (JSONException e) {
                    Log.i("InternetHelper", "json for loop error");
                    Log.i("InternetHelper", e.toString());
                }
            }
            Log.i("InternetHelper", "helper is done");
            adapter.notifyDataSetChanged(); // can't update UI thread

        } catch (Exception e) {
        }


    }
}