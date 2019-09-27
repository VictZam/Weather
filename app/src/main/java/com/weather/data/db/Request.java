package com.weather.data.db;

import com.google.gson.annotations.SerializedName;

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
}
