package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class WeatherIconUrl extends RealmObject {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void deleteWeatherIconUrls(RealmList<WeatherIconUrl> weatherIconUrls) {
        if (weatherIconUrls.size() != 0) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> weatherIconUrls.deleteAllFromRealm());
            }
        }
    }
}
