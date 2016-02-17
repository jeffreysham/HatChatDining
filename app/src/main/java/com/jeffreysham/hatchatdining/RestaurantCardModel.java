package com.jeffreysham.hatchatdining;

import com.andtinder.model.CardModel;

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

    public RestaurantCardModel(String title, String description, double rating,
                               double distance, String address, String photoURL, String number) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.distance = distance;
        this.address = address;
        this.photoURL = photoURL;
        this.number = number;
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

    @Override
    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistance() {
        return distance;
    }
}
