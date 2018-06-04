package com.example.lucas.teset;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lucas on 5/31/18.
 */

public class Item {

    public double getPrice() {
        return price;
    }

    public String getDisplayPricePerUnit() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return "$" + formatter.format(price / quantity) + " per Unit";
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getEndDateString() {
        DateFormat df =  new SimpleDateFormat("yyyy/MM/dd");
        return df.format(endDate);
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemURL() {
        return itemURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getPackageIdentifier() {
        if (!trackingNumber.equals("null"))
            return trackingNumber;
        if (zipCode != -9999)
            return Integer.toString(zipCode);
        return "";
    }

    private String itemID;

    public String getItemID() {
        return itemID;
    }

    private String itemURL;
    private String imageURL;
    private String trackingNumber;
    private String title;
    private String sellerID;
    private String sellerEmail;
    private int zipCode, quantity;
    private double price;
    private Date endDate;
    private boolean delivered = false;
    private static String currentDate;



    public Item(String itemID, String endDate, String itemURL, String imageURL, String trackingNumber, String title, String sellerID, String sellerEmail, String zipCode, double price, int quantity, boolean delivered) {
        this.itemID = itemID;
        try {
            this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        }
        catch (Exception e) {this.endDate = null; }
        this.itemURL = itemURL;
        this.imageURL = imageURL;
        if (trackingNumber.equals("null"))
            this.trackingNumber = "null";
        else
            this.trackingNumber = trackingNumber;

        this.title = title;
        this.sellerID = sellerID;
        this.sellerEmail = sellerEmail;
        if (zipCode.equals("null"))
            this.zipCode = -9999;
        else
            this.zipCode = Integer.parseInt(zipCode);

        this.price = price;
        if (quantity < 0)
            this.quantity = -1;
        else
            this.quantity = quantity;
        this.delivered = delivered;


        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        this.currentDate = df.format(new Date());
    }





}
