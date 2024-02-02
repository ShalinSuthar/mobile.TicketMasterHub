package com.example.csci571;

import org.json.JSONArray;

import java.util.List;

public class SearchResult {
    private String title;
    private String date;
    private String location;

    private String time;

    private String category;

    private String eventImage;

    private boolean isMusicEvent;

    private String[]  attractionsList;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private String venueName;
    private String id;
    // other fields and methods

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[]  getAttractionsList(){ return attractionsList;}

    public void setAttractionsList(String[]  attractionsList){this.attractionsList = attractionsList;}

    public boolean  getIsMusicEvent() {
        return isMusicEvent;
    }

    public void serIsMusicEvent(boolean isMusicEvent) {
        this.isMusicEvent = isMusicEvent;
    }

    public String  getVenue() {
        return venueName;
    }

//    public void setVenue(String venueName) {
//        this.venueName = venueName;
//    }


    public SearchResult(String title, String date, String location, String time, String category, String eventImage, boolean isFavorite) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.time = time;
        this.category = category;
        this.eventImage = eventImage;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getEventImage() {
        return eventImage;
    }
}
