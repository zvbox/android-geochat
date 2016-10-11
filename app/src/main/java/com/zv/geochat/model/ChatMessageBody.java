package com.zv.geochat.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatMessageBody {
    private String text;
    private Double lng;
    private Double lat;
    // TODO: additional fields (e.g. lat, lng, time)

    public ChatMessageBody(String text){
        this.text = text;
    }

    public ChatMessageBody(String text, double lng, double lat){
        this.text = text;
        this.lng = lng;
        this.lat = lat;
    }

    public String getText() {
        return text;
    }

    public Double getLng() { return lng; }

    public Double getLat() { return lat; }

    public boolean hasLocation(){
        return lat!= null && lng != null;
    }

    public static ChatMessageBody fromJson(String jsonString) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(jsonString, ChatMessageBody.class);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(this);
    }
}
