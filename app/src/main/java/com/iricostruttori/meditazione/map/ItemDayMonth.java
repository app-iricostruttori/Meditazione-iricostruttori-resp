package com.iricostruttori.meditazione.map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemDayMonth {
    private int id;
    private String day_month;
    private String txt_day_month;

    public ItemDayMonth(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.day_month = object.getString("giorno_mese");
            this.txt_day_month = object.getString("testo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayMonth() {
        return day_month;
    }

    public void setDayMonth(String day_month) {
        this.day_month = day_month;
    }

    public String getTxtDayMonth() {
        return txt_day_month;
    }

    public void setTxtDayMonth(String txt_day_month) {
        this.txt_day_month = txt_day_month;
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<ItemDayMonth> fromJson(JSONArray jsonObjects) {
        ArrayList<ItemDayMonth> items = new ArrayList<ItemDayMonth>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                items.add(new ItemDayMonth(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

}