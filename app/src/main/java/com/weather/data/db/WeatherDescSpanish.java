package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class WeatherDescSpanish extends RealmObject {

    @SerializedName("value")
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void deleteWeatherDesc(RealmList<WeatherDescSpanish> weatherDescSpanish) {
        if (weatherDescSpanish.size() != 0) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> weatherDescSpanish.deleteAllFromRealm());
            }
        }
    }
}


