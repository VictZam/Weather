package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class WeatherDesc extends RealmObject {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void deleteWeatherDesc(RealmList<WeatherDesc> weatherDescs) {
        if (weatherDescs.size() != 0) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> weatherDescs.deleteAllFromRealm());
            }
        }
    }
}
