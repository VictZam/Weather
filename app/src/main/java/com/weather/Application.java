package com.weather;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Weather.realm")
                .build();
        Realm.setDefaultConfiguration(config);

    }

    public static Context getContext() {
        return context;
    }

}
