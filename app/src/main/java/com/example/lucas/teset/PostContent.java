package com.example.lucas.teset;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Lucas on 6/1/18.
 */

public class PostContent implements Runnable{
    private Item myItem = null;

    public PostContent(Item _myItem) {
        myItem = _myItem;
    }

    public void run() {
        String result = null;
        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            String data = String.format("{\"itemID\": \"%s\", \"quantity\": \"%d\", \"delivered\": \"%d\"}", myItem.getItemID(), myItem.getQuantity(), 1);
            url = new URL("http://10.0.0.20:5000/updateDeliveries");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            urlConnection.connect();


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            StringBuilder sb = new StringBuilder();
            while((bytesRead = in.read(contents)) != -1) {
                sb.append(new String(contents, 0, bytesRead));
            }

            String strFileContents = sb.toString();
            Log.i("result", strFileContents);


        } catch (Exception e) {
            Log.i("PostContentError", e.toString());
        } finally {
            urlConnection.disconnect();
        }
    }



}