package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Request extends RealmObject {

    @SerializedName("type")
    String type;
    @SerializedName("query")
    String query;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public static void deleteRequest(RealmList<Request> request) {
        if (request.size() != 0) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realmQuerry -> request.deleteAllFromRealm());
            }
        }
    }
}
