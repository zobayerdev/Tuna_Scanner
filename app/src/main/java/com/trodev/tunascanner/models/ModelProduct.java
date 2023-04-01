package com.trodev.tunascanner.models;

public class ModelProduct {
    // variable
    int icon;
    String title, description;
    float rating;

    // ##########
    //constructor
    public ModelProduct(int icon, String title, String description, float rating) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.rating = rating;
    }


    // ##############
    /*Getter Setters*/
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
