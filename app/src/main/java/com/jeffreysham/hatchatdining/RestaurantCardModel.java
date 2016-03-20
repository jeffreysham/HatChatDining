package com.jeffreysham.hatchatdining;

import com.jeffreysham.hatchatdining.tindercards.CardModel;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class RestaurantCardModel extends CardModel {
    private String title;
    private String description;
    private double rating;
    private double distance;
    private String address;
    private String photoURL;
    private String number;
    private String mobileURL;
    private String id;
    private float latitude;
    private float longitude;

    public RestaurantCardModel(String title, String description, double rating,
                               double distance, String address, String photoURL, String number, String mobileURL,
                               String id, float latitude, float longitude) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.distance = distance;
        this.address = address;
        this.photoURL = photoURL;
        this.number = number;
        this.mobileURL = mobileURL;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public double getDistance() {
        return distance;
    }

    public String getMobileURL() {
        return this.mobileURL;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getId() {
        return this.id;
    }
}
