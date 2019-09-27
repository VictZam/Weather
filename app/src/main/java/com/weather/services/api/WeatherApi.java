package com.weather.services.api;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weather.data.db.WeatherLocation;
import com.weather.data.local.ResponseList;
import io.realm.Realm;

public class WeatherApi {
    private static WeatherApi instance;
    private static String API_KEY = "4e63f664fea1415ca68170846192609";
    private static String API_LINK = "http://api.worldweatheronline.com/premium/v1/weather.ashx";

    public WeatherApi() {
    }

    public static WeatherApi getInstance() {
        if (instance == null)
            instance = new WeatherApi();
        return instance;
    }

    public static String apiRequest(String lat, String lon) {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?key=%s&q=%s,%s&format=json&num_of_days=7&lang=es", API_KEY, lat, lon));
        return sb.toString();
    }

    public void fetchWeather(String latitude, String longitude, String locality, final ResponseListener listener) {
        AndroidNetworking.get(apiRequest(latitude, longitude))
                .build()
                .getAsObject(ResponseList.class, new ParsedRequestListener<ResponseList>() {
                    @Override
                    public void onResponse(ResponseList response) {

                        final WeatherLocation weatherLocations = new GsonBuilder()
                                .create()
                                .fromJson(new Gson().toJsonTree(response.getData()),
                                        new TypeToken<WeatherLocation>() {
                                        }.getType());

                        try (Realm realm = Realm.getDefaultInstance()) {
                            weatherLocations.setLocality(locality);
                            realm.executeTransaction(realmQuerry -> {
                                WeatherLocation weatherRegisterLocation = realmQuerry
                                        .where(WeatherLocation.class)
                                        .equalTo("locality", locality)
                                        .findFirst();

                                if(weatherRegisterLocation != null) {
                                    for(int i = 0; i < weatherRegisterLocation.getRequest().size(); i++) {
                                        weatherRegisterLocation.getRequest().get(i).deleteFromRealm();
                                    }
                                    for(int i = 0; i < weatherRegisterLocation.getWeather().size(); i++) {
                                        weatherRegisterLocation.getWeather().get(i).deleteFromRealm();
                                    }
                                    for(int i = 0; i < weatherRegisterLocation.getCurrentCondition().size(); i++) {
                                        weatherRegisterLocation.getCurrentCondition().get(i).deleteFromRealm();
                                    }
                                        weatherRegisterLocation.deleteFromRealm();
                                }

                                realmQuerry.insertOrUpdate(weatherLocations);
                            });

                        }

                        listener.onResult(true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.onResult(false);

                    }
                });
    }

}
