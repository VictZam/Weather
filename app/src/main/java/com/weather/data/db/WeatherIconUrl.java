package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class WeatherIconUrl extends RealmObject {

    @SerializedName("value")
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
