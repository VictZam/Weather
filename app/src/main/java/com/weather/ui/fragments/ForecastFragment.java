package com.weather.ui.fragments;


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
import com.weather.data.local.Principal;
import com.weather.ui.adapters.WeatherForecastAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    @BindView(R.id.txtCityName) TextView txtCityName;
    @BindView(R.id.txtGeoCoord) TextView txtGeoCord;
    RecyclerView recycleyViewForecast;

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

        recycleyViewForecast = (RecyclerView) view.findViewById(R.id.recycleyViewForecast);

        recycleyViewForecast.setHasFixedSize(true);
        recycleyViewForecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        try (Realm realm = Realm.getDefaultInstance()) {
            WeatherLocation weatherLocation = realm.where(WeatherLocation.class)
                    .equalTo("locality", Principal.locality)
                    .findFirst();

            if (weatherLocation != null) {
                txtCityName.setText(""+Principal.locality);
                txtGeoCord.setText(String.format("%s", weatherLocation.getRequest().get(0)
                        .getQuery()).replace("and", ",").replace("Lat",
                        "").replace("Lon", ""));

                ArrayList<Weather> weathers = new ArrayList<>();
                weathers.addAll(realm.copyFromRealm(weatherLocation.getWeather()));

                WeatherForecastAdapter adapter = new WeatherForecastAdapter
                        (getContext(), weathers);
                recycleyViewForecast.setAdapter(adapter);
            }

            return view;
        }

    }
}
