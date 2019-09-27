package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Weather extends RealmObject {

    private String locality;
    @SerializedName("date")
    String date;
    @SerializedName("maxtempC")
    float maxtempC;
    @SerializedName("maxtempF")
    float maxtempF;
    @SerializedName("mintempC")
    float mintempC;
    @SerializedName("mintempF")
    float mintempF;
    @SerializedName("avgtempC")
    float avgtempC;
    @SerializedName("avgtempF")
    float avgtempF;
    @SerializedName("totalSnow_cm")
    float totalSnow_cm;
    @SerializedName("sunHour")
    float sunHour;
    @SerializedName("uvIndex")
    int uvIndex;


    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getMaxtempC() {
        return maxtempC;
    }

    public void setMaxtempC(float maxtempC) {
        this.maxtempC = maxtempC;
    }

    public float getMaxtempF() {
        return maxtempF;
    }

    public void setMaxtempF(float maxtempF) {
        this.maxtempF = maxtempF;
    }

    public float getMintempC() {
        return mintempC;
    }

    public void setMintempC(float mintempC) {
        this.mintempC = mintempC;
    }

    public float getMintempF() {
        return mintempF;
    }

    public void setMintempF(float mintempF) {
        this.mintempF = mintempF;
    }

    public float getAvgtempC() {
        return avgtempC;
    }

    public void setAvgtempC(float avgtempC) {
        this.avgtempC = avgtempC;
    }

    public float getAvgtempF() {
        return avgtempF;
    }

    public void setAvgtempF(float avgtempF) {
        this.avgtempF = avgtempF;
    }

    public float getTotalSnow_cm() {
        return totalSnow_cm;
    }

    public void setTotalSnow_cm(float totalSnow_cm) {
        this.totalSnow_cm = totalSnow_cm;
    }

    public float getSunHour() {
        return sunHour;
    }

    public void setSunHour(float sunHour) {
        this.sunHour = sunHour;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }
}
