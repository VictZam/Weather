package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class CurrentCondition extends RealmObject {
    @SerializedName("observation_time")
    String observationTime;
    @SerializedName("temp_C")
    float celcius;
    @SerializedName("temp_F")
    float farenheit;
    @SerializedName("weatherCode")
    int wheatherCode;
    @SerializedName("weatherIconUrl")
    RealmList<WeatherIconUrl> weatherIconUrl;
    @SerializedName("weatherDesc")
    RealmList<WeatherDesc> weatherDesc;
    @SerializedName("windspeedMiles")
    float windspeedMiles;
    @SerializedName("windspeedKmph")
    float windspeedKmph;
    @SerializedName("winddirDegree")
    float winddirDegree;
    @SerializedName("winddir16Point")
    String winddir16Point;
    @SerializedName("recipMM")
    float recipMM;
    @SerializedName("precipInches")
    float precipInches;
    @SerializedName("humidity")
    float humidity;
    @SerializedName("visibility")
    float visibility;
    @SerializedName("visibilityMiles")
    float visibilityMiles;
    @SerializedName("pressure")
    float pressure;
    @SerializedName("pressureInches")
    float pressureInches;
    @SerializedName("cloudcover")
    float cloudcover;
    @SerializedName("FeelsLikeC")
    float FeelsLikeC;
    @SerializedName("FeelsLikeF")
    float FeelsLikeF;
    @SerializedName("uvIndex")
    int uvIndex;

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public float getCelcius() {
        return celcius;
    }

    public void setCelcius(float celcius) {
        this.celcius = celcius;
    }

    public float getFarenheit() {
        return farenheit;
    }

    public void setFarenheit(float farenheit) {
        this.farenheit = farenheit;
    }

    public int getWheatherCode() {
        return wheatherCode;
    }

    public void setWheatherCode(int wheatherCode) {
        this.wheatherCode = wheatherCode;
    }

    public RealmList<WeatherIconUrl> getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public void setWeatherIconUrl(RealmList<WeatherIconUrl> weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }

    public RealmList<WeatherDesc> getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(RealmList<WeatherDesc> weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public float getWindspeedMiles() {
        return windspeedMiles;
    }

    public void setWindspeedMiles(float windspeedMiles) {
        this.windspeedMiles = windspeedMiles;
    }

    public float getWindspeedKmph() {
        return windspeedKmph;
    }

    public void setWindspeedKmph(float windspeedKmph) {
        this.windspeedKmph = windspeedKmph;
    }

    public float getWinddirDegree() {
        return winddirDegree;
    }

    public void setWinddirDegree(float winddirDegree) {
        this.winddirDegree = winddirDegree;
    }

    public String getWinddir16Point() {
        return winddir16Point;
    }

    public void setWinddir16Point(String winddir16Point) {
        this.winddir16Point = winddir16Point;
    }

    public float getRecipMM() {
        return recipMM;
    }

    public void setRecipMM(float recipMM) {
        this.recipMM = recipMM;
    }

    public float getPrecipInches() {
        return precipInches;
    }

    public void setPrecipInches(float precipInches) {
        this.precipInches = precipInches;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getVisibilityMiles() {
        return visibilityMiles;
    }

    public void setVisibilityMiles(float visibilityMiles) {
        this.visibilityMiles = visibilityMiles;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getPressureInches() {
        return pressureInches;
    }

    public void setPressureInches(float pressureInches) {
        this.pressureInches = pressureInches;
    }

    public float getCloudcover() {
        return cloudcover;
    }

    public void setCloudcover(float cloudcover) {
        this.cloudcover = cloudcover;
    }

    public float getFeelsLikeC() {
        return FeelsLikeC;
    }

    public void setFeelsLikeC(float feelsLikeC) {
        FeelsLikeC = feelsLikeC;
    }

    public float getFeelsLikeF() {
        return FeelsLikeF;
    }

    public void setFeelsLikeF(float feelsLikeF) {
        FeelsLikeF = feelsLikeF;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }


    public void deleteCurrentCondition(RealmList<CurrentCondition> currentConditions) {
        if (currentConditions.size() != 0) {
            for(CurrentCondition currentCondition : currentConditions){
                new WeatherIconUrl().deleteWeatherIconUrls(currentCondition.getWeatherIconUrl());
                new WeatherDesc().deleteWeatherDesc(currentCondition.getWeatherDesc());
            }

            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> currentConditions.deleteAllFromRealm());
            }
        }
    }
}
