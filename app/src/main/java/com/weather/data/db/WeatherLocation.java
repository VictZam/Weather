package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WeatherLocation extends RealmObject {

    @PrimaryKey
    private String locality;
    @SerializedName("request")
    private RealmList<Request> request;
    @SerializedName("current_condition")
    private RealmList<CurrentCondition> currentCondition;
    @SerializedName("weather")
    private RealmList<Weather> weather;

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public RealmList<Request> getRequest() {
        return request;
    }

    public void setRequest(RealmList<Request> request) {
        this.request = request;
    }

    public RealmList<CurrentCondition> getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(RealmList<CurrentCondition> currentCondition) {
        this.currentCondition = currentCondition;
    }

    public RealmList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(RealmList<Weather> weather) {
        this.weather = weather;
    }


    public void deleteCascadeWeatherLocation(WeatherLocation weatherLocation){
        if(weatherLocation != null) {
            Request.deleteRequest(weatherLocation.getRequest());
            Weather.deleteWeather(weatherLocation.getWeather());
            CurrentCondition.deleteCurrentCondition(weatherLocation.getCurrentCondition());

            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> weatherLocation.deleteFromRealm());
            }
        }
    }
}
