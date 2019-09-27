package com.weather.data.local;

import com.google.gson.annotations.SerializedName;

public class ResponseObject<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseObject() {

    }
}
