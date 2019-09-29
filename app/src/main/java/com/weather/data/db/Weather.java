package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Weather extends RealmObject {

    @SerializedName("date")
    private String date;
    @SerializedName("maxtempC")
    private float maxtempC;
    @SerializedName("maxtempF")
    private float maxtempF;
    @SerializedName("mintempC")
    private float mintempC;
    @SerializedName("mintempF")
    private float mintempF;
    @SerializedName("avgtempC")
    private float avgtempC;
    @SerializedName("avgtempF")
    private float avgtempF;
    @SerializedName("totalSnow_cm")
    private float totalSnow_cm;
    @SerializedName("sunHour")
    private float sunHour;
    @SerializedName("uvIndex")
    private int uvIndex;
    @SerializedName("hourly")
    private RealmList<Hourly> hourly;

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

    public RealmList<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(RealmList<Hourly> hourly) {
        this.hourly = hourly;
    }


    public static void deleteWeather(RealmList<Weather> weathers) {
        if (weathers.size() != 0) {
            for(Weather weather : weathers){
                Hourly.deleteHourly(weather.getHourly());
            }

            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> weathers.deleteAllFromRealm());
            }
        }
    }
}
