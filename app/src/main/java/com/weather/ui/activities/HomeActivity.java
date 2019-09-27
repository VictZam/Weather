package com.weather.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;
import com.weather.R;
import com.weather.data.db.WeatherLocation;
import com.weather.data.local.Principal;
import com.weather.services.api.ResponseListener;
import com.weather.services.api.WeatherApi;

import java.io.IOException;
import java.util.Locale;

import io.realm.Realm;


public class HomeActivity extends AppCompatActivity implements LocationListener {

    TextView txtCity, txtLastUpdate, txtDescription;
    TextView txtHumidity, txtTime, txtCelsius;
    ImageView imageView;

    LocationManager locationManager;
    String provider;

    int MY_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtCity = findViewById(R.id.txtCity);
        txtLastUpdate = findViewById(R.id.txtLastUpdate);
        txtDescription = findViewById(R.id.txtDescription);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtTime = findViewById(R.id.txtTime);
        txtCelsius = findViewById(R.id.txtCelsius);
        imageView = findViewById(R.id.imageView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        saveCurrentLocationWeather();
    }

    @Override
    protected void onPause() {
        super.onPause();
        validatePermition();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validatePermition();
    }

    public void validatePermition(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            Log.e("TAG", "No Location");
        else
            locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        Principal.lat = location.getLatitude();
        Principal.lon = location.getLongitude();
        saveCurrentLocationWeather();
    }

    public void saveCurrentLocationWeather(){
        try {
            if(Principal.lat != 0 && Principal.lon != 0) {
                Geocoder coder = new Geocoder(this, Locale.ENGLISH);
                String locality = coder.getFromLocation(Principal.lat, Principal.lon, 1).get(0).getLocality();

                WeatherApi.getInstance().fetchWeather(
                        String.valueOf(Principal.lat), String.valueOf(Principal.lon), locality, isSuccess -> {
                            if(isSuccess)
                                setCurrentLocationWeather();
                            else
                                Toast.makeText(HomeActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                        });


            } else {
                Toast.makeText(this, "Por favor tome ubicacion", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentLocationWeather(){
        try(Realm realm = Realm.getDefaultInstance()) {
            WeatherLocation weatherLocation = realm.where(WeatherLocation.class)
                    .findFirst();

            txtCity.setText(String.format("%s", weatherLocation.getLocality()));
            txtLastUpdate.setText(String.format("Last Updated: %s", weatherLocation.getWeather().get(0).getDate()));
            txtDescription.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue()));
            txtHumidity.setText(String.format("%.1f", weatherLocation.getCurrentCondition().get(0).getHumidity()));
            txtTime.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getObservationTime()));
            txtCelsius.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getCelcius()));
            Picasso.with(HomeActivity.this)
                    .load(R.drawable.nube)
                    .into(imageView);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
