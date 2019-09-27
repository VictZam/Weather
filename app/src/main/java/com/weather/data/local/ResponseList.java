package com.weather.data.local;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseList<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("message")
    private String message;

    public ResponseList() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
