package com.weather.ui.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.weather.R;
import com.weather.data.db.WeatherLocation;
import com.weather.services.api.WeatherApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    @BindView(R.id.imageViewWeather) ImageView imageWeather;
    @BindView(R.id.txtCityName) TextView txtCityName;
    @BindView(R.id.txtHumidity) TextView txtHumidity;
    @BindView(R.id.txtSensation) TextView txtSensation;
    @BindView(R.id.txtVisibility) TextView txtVisibility;
    @BindView(R.id.txtPreasure) TextView txtPreassure;
    @BindView(R.id.txtTemperature) TextView txtTemperature;
    @BindView(R.id.txtDescription) TextView txtDescription;
    @BindView(R.id.txtDateTime) TextView txtDateTime;
    @BindView(R.id.txtWind) TextView txtWind;
    @BindView(R.id.txtGeoCoord) TextView txtGeoCoord;
    @BindView(R.id.weatherPanel) LinearLayout weatherPanel;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if(instance == null){
            instance = new TodayWeatherFragment();
        }
        return instance;
    }


    public TodayWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        ButterKnife.bind(this, view);
        setCurrenWeatherData(view);

        return view;
    }


    public void setCurrenWeatherData(View view) {
        if(isNetDisponible()) {
            WeatherApi.getInstance().fetchWeather(
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("lat", null),
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("lon", null),
                    view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null) , isSuccess -> {
                        if (isSuccess)
                            setCurrentLocationWeather(view);
                        else {
                            setCurrentLocationWeather(view);
                        }
                    });
        } else {
            setCurrentLocationWeather(view);
        }
    }

    public void setCurrentLocationWeather(View view){
        try(Realm realm = Realm.getDefaultInstance()) {
            WeatherLocation weatherLocation = realm.where(WeatherLocation.class)
                    .equalTo("locality", view.getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null))
                    .findFirst();

            if(weatherLocation != null) {
                progressBar.setVisibility(View.INVISIBLE);
                weatherPanel.setVisibility(View.VISIBLE);

                txtCityName.setText(String.format("%s", weatherLocation.getLocality()));
                txtTemperature.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getCelcius()));
                txtDescription.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue()));
                txtDateTime.setText(String.format("Date: %s ", weatherLocation.getWeather().get(0).getDate()));
                txtWind.setText(String.format("%.2f Km", weatherLocation.getCurrentCondition().get(0).getWindspeedKmph()));
                txtPreassure.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getPressure()));
                txtHumidity.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getHumidity()));
                txtSensation.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getFeelsLikeC()));
                txtVisibility.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getVisibility()));
                txtGeoCoord.setText(String.format("%s", weatherLocation.getRequest().get(0).getQuery()).replace("and",",").replace("Lat", "").replace("Lon", ""));

                Picasso.with(view.getContext())
                        .load(weatherLocation.getCurrentCondition().get(0).getWeatherIconUrl().get(0).getValue())
                        .into(imageWeather);
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
