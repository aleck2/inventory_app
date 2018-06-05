package com.example.lucas.teset;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
    * An app to view MySQL table over local network and update entries
    *
    * Future features:
    *   Item alerts over app
    *   Add City, State to ItemAdapter (and table)
    *   Continue to sort by OrderDate (server-side), but display ShipDate (connvert ShipTime from GMT to seller's timezone to get correct date)
    *   Add Search to find by trackingNumber of zipcode
    *   Double check, may need to replace null with NULL after fixing updatePurchases.py
    *   Add more options to dialog box
    *   Cancel dialog box after hitting back
    *   Use local sqlite database rather than going to server on every onCreate
    *   Add widgets to filter items in listview
    *   Use ML Kit to allow shipping label bar code scanning to find item
 */

public class MainActivity extends AppCompatActivity {
    private NotificationManager manager;
    // Evens are off
    // Odds are on
    static final long[] DEFAULT_VIBRATE_PATTERN = {0, 600, 250, 250};
    private ArrayList<Item> myItems = new ArrayList<Item>();
    private ItemAdapter adapter = null;

    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int success = msg.arg1;
            if (success == 1) {
                adapter.notifyDataSetChanged();
            }
        }
    }; // Handler is associated with UI Thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        // TODO use notification channel to receive item alerts through app rather than email
        NotificationChannel mChannel = new NotificationChannel("myId", getString(R.string.app_name) ,
                NotificationManager.IMPORTANCE_HIGH);
        mChannel.enableLights(true);
        mChannel.setDescription("Hey, you reading this?");
        mChannel.setLightColor(Color.GREEN);
        mChannel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
        getManager().createNotificationChannel(mChannel);
        final Context c = this;
//        createNotification();
        adapter = new ItemAdapter(this, myItems);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final Item myItem = adapter.getItem(position);

                View promptsView = LayoutInflater.from(c).inflate(R.layout.input_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setView(promptsView);
                final EditText dateEdit = (EditText) promptsView.findViewById(R.id.date);
                final EditText quantityEdit = (EditText) promptsView.findViewById(R.id.quantity);
                quantityEdit.setText(Integer.toString(myItem.getQuantity()));

                DateFormat df = DateFormat.getDateInstance();
                df.setTimeZone(TimeZone.getTimeZone("gmt"));
                String gmtTime = df.format(new Date());
                dateEdit.setText(gmtTime);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("Delivered", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        // TODO check return code of PostContent before removing from data
                        myItems.remove(myItem);
                        adapter.notifyDataSetChanged();
                        PostContent post = new PostContent(myItem);
                        Thread t2 = new Thread(post);
                        t2.start();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();



            }
        });

        // TODO replace thread.sleep, non-UI thread can't change UI thread, use async task
        InternetHelper helper = new InternetHelper(myItems, adapter, mHandler);

        Thread t = new Thread(helper);
        t.start();



    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "myId")
                .setSmallIcon(R.mipmap.kettlebell)
                //.setOnlyAlertOnce(true)
                .setContentTitle("Cha-Ching")
                .setContentText("Item Found");
        Notification n = mBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(35, n);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}


