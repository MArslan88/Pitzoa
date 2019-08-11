package com.example.pitzoa;

public class Flavour {

    /** Flavour Name */
    private String mFlavourHeading;
    /** Flavour Details */
    private String mFlavourDetail;

    private String flavourOfPizza;

    /** Image resource ID for the word*/
    private int mImageResourceId=NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED=-1;

    public Flavour() {
    }

    public Flavour(String flavourOfPizza) {
        this.flavourOfPizza = flavourOfPizza;
    }

    public Flavour(String flavourHeading, String flavourDetail, int imageResourceId) {
        this.mFlavourHeading = flavourHeading;
        this.mFlavourDetail = flavourDetail;
        this.mImageResourceId = imageResourceId;
    }

    public String getmFlavourHeading() {
        return mFlavourHeading;
    }

    public String getmFlavourDetail() {
        return mFlavourDetail;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public String getFlavourOfPizza() {
        return flavourOfPizza;
    }

    public void setFlavourOfPizza(String flavourOfPizza) {
        this.flavourOfPizza = flavourOfPizza;
    }

    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
