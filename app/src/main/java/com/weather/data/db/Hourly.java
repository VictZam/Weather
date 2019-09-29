package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Hourly extends RealmObject {

    @SerializedName("weatherIconUrl")
    RealmList<WeatherIconUrl> weatherIconUrl;
    @SerializedName("weatherDesc")
    RealmList<WeatherDesc> weatherDesc;
    @SerializedName("lang_es")
    RealmList<WeatherDescSpanish> weatherDescSpanis;

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

    public RealmList<WeatherDescSpanish> getWeatherDescSpanis() {
        return weatherDescSpanis;
    }

    public void setWeatherDescSpanis(RealmList<WeatherDescSpanish> weatherDescSpanis) {
        this.weatherDescSpanis = weatherDescSpanis;
    }

    public static void deleteHourly(RealmList<Hourly> hourlies) {
        if (hourlies.size() != 0) {
            for(Hourly hourly : hourlies){
                WeatherIconUrl.deleteWeatherIconUrls(hourly.getWeatherIconUrl());
                WeatherDesc.deleteWeatherDesc(hourly.getWeatherDesc());
                WeatherDescSpanish.deleteWeatherDesc(hourly.getWeatherDescSpanis());
            }

            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> hourlies.deleteAllFromRealm());
            }
        }
    }

}
