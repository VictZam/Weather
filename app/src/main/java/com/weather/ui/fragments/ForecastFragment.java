package com.weather.ui.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weather.R;
import com.weather.data.db.Weather;
import com.weather.data.db.WeatherLocation;
import com.weather.services.api.WeatherApi;
import com.weather.ui.adapters.WeatherForecastAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    @BindView(R.id.txtTittle) TextView txtTittle;
    @BindView(R.id.txtCityName) TextView txtCityName;
    @BindView(R.id.recycleyViewForecast) RecyclerView recycleyViewForecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null) {
            instance = new ForecastFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, view);

        recycleyViewForecast.setHasFixedSize(true);
        recycleyViewForecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        if(view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
            txtTittle.setText("PRONÓSTICO DEL TIEMPO DE 7 DÍAS");
        }

        if(isNetDisponible()) {
            WeatherApi.getInstance().fetchWeather(
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("lat", null),
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("lon", null),
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null), isSuccess -> {
                        if (isSuccess)
                            setInfoForecast();
                        else
                            setInfoForecast();
                    });
        } else {
            setInfoForecast();
        }

        return view;
    }

    public void setInfoForecast(){
        try (Realm realm = Realm.getDefaultInstance()) {
            WeatherLocation weatherLocation = realm.where(WeatherLocation.class)
                    .equalTo("locality", getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null))
                    .findFirst();

            if (weatherLocation != null) {
                txtCityName.setText(getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null));

                ArrayList<Weather> weathers = new ArrayList<>();
                weathers.addAll(realm.copyFromRealm(weatherLocation.getWeather()));

                WeatherForecastAdapter adapter = new WeatherForecastAdapter
                        (getContext(), weathers);
                recycleyViewForecast.setAdapter(adapter);
            }
        }
    }


    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
}
