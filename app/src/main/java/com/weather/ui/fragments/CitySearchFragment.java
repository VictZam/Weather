package com.weather.ui.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.weather.R;
import com.weather.data.db.WeatherLocation;
import com.weather.services.api.WeatherApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitySearchFragment extends Fragment {

    private List<String> listCities;

    @BindView(R.id.citiesSearchBar) MaterialSearchBar citiesSearchBar;
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
    @BindView(R.id.txtMaxTemp) TextView txtMaxTemp;
    @BindView(R.id.txtMinTemp) TextView txtMinTemp;
    @BindView(R.id.weatherPanel) LinearLayout weatherPanel;
    @BindView(R.id.progressBar) ProgressBar progressBar;


    static CitySearchFragment instance;

    public static CitySearchFragment getInstance() {
        if (instance == null) {
            instance = new CitySearchFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_search, container, false);
        ButterKnife.bind(this, view);

        citiesSearchBar.setEnabled(false);

        new LoadCities().execute();

            return view;
    }


    private class LoadCities extends SimpleAsyncTask<List<String>>{

        @Override
        protected List<String> doInBackgroundSimple() {
            listCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipOutputStream = new GZIPInputStream(is);

                InputStreamReader reader = new InputStreamReader(gzipOutputStream);
                BufferedReader in = new BufferedReader(reader);

                String readed;
                while ((readed = in.readLine()) != null)
                    builder.append(readed);
                listCities = new Gson().fromJson(builder.toString(), new TypeToken<List<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return listCities;
        }


        @Override
        protected void onSuccess(List<String> list) {
            super.onSuccess(list);

            citiesSearchBar.setEnabled(true);
            citiesSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();
                    for (String search : list){
                        if(search.toLowerCase().contains(citiesSearchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    citiesSearchBar.setLastSuggestions(suggest);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            citiesSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    getWeatherInfo(text.toString());
                    citiesSearchBar.setLastSuggestions(list);
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            citiesSearchBar.setLastSuggestions(list);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void getWeatherInfo(String cityName) {
        WeatherApi.getInstance().fetchWeather(null, null, cityName, isSuccess -> {
            if (isSuccess) {
                setWeatherInfo(cityName);
            }
            else {
                setWeatherInfo(cityName);
                Toast.makeText(getContext(), "Sin conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWeatherInfo(String cityName){
        try (Realm realm = Realm.getDefaultInstance()) {
            WeatherLocation weatherLocation = realm.where(WeatherLocation.class)
                    .equalTo("locality", cityName)
                    .findFirst();

            if (weatherLocation != null) {
                progressBar.setVisibility(View.GONE);
                weatherPanel.setVisibility(View.VISIBLE);


                if(getContext().getSharedPreferences("preferences", MODE_PRIVATE).getString("degrees", "c") == "f"){
                    txtTemperature.setText(String.format("%.2f °F", weatherLocation.getCurrentCondition().get(0).getFarenheit()));
                    txtSensation.setText(String.format("%.2f °F", weatherLocation.getCurrentCondition().get(0).getFeelsLikeF()));
                    txtMaxTemp.setText(String.format("%.2f °F", weatherLocation.getWeather().get(0).getMaxtempF()));
                    txtMinTemp.setText(String.format("%.2f °F", weatherLocation.getWeather().get(0).getMintempF()));
                } else {
                    txtTemperature.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getCelcius()));
                    txtSensation.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getFeelsLikeC()));
                    txtMaxTemp.setText(String.format("%.2f °C", weatherLocation.getWeather().get(0).getMaxtempC()));
                    txtMinTemp.setText(String.format("%.2f °C", weatherLocation.getWeather().get(0).getMintempC()));
                }

                txtCityName.setText(String.format("%s", weatherLocation.getLocality()));
                txtDescription.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue()));
                txtDateTime.setText(String.format("Date: %s ", weatherLocation.getWeather().get(0).getDate()));
                txtWind.setText(String.format("%.2f Km", weatherLocation.getCurrentCondition().get(0).getWindspeedKmph()));
                txtPreassure.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getPressure()));
                txtHumidity.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getHumidity()));
                txtVisibility.setText(String.format("%.2f", weatherLocation.getCurrentCondition().get(0).getVisibility()));

                Picasso.with(getContext())
                        .load(weatherLocation.getCurrentCondition().get(0).getWeatherIconUrl().get(0).getValue())
                        .into(imageWeather);

            } else {
                Toast.makeText(getContext(), "No hay datos", Toast.LENGTH_SHORT).show();
                weatherPanel.setVisibility(View.GONE);
            }
        }
    }

}

