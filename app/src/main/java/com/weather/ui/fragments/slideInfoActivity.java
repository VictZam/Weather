package com.weather.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.weather.R;
import com.weather.ui.activities.HomeActivity;

import java.io.IOException;
import java.util.Locale;

public class slideInfoActivity extends AppIntro {


    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
            addSlide(AppIntro2Fragment.newInstance("SOL",
                    "Recuerda beber mucha agua en temporada de calor",
                    R.drawable.sol,
                    Color.parseColor("#51e2b7")));

            addSlide(AppIntro2Fragment.newInstance("TORMENTAS ELECTRICAS",
                    "Desconecta tus aparatos electronicos durante las tormentas electricas",
                    R.drawable.trueno,
                    Color.parseColor("#51e2b7")));

            addSlide(AppIntro2Fragment.newInstance("LLUVIA",
                    "Durante la empoca de lluvia, recuerda comer alimentos ricos en vitamina C",
                    R.drawable.soltar,
                    Color.parseColor("#51e2b7")));
        } else {
            addSlide(AppIntro2Fragment.newInstance("SUN",
                    "Remember to drink a lot of water in hot season",
                    R.drawable.sol,
                    Color.parseColor("#51e2b7")));

            addSlide(AppIntro2Fragment.newInstance("THUNDER STORMS",
                    "Disconnect your electronic devices during thunderstorms",
                    R.drawable.trueno,
                    Color.parseColor("#51e2b7")));

            addSlide(AppIntro2Fragment.newInstance("RAIN",
                    "During the rainy season, remember to eat foods rich in vitamin C",
                    R.drawable.soltar,
                    Color.parseColor("#51e2b7")));
        }

        showStatusBar(false);
        setBarColor(Color.parseColor("#333639"));
        setSeparatorColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onDonePressed(){
        validatePermition();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment){
        validatePermition();
        startActivity(new Intent(this, HomeActivity.class));
        Toast.makeText(this, "On Skip Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlideChanged() {
        validatePermition();
        Toast.makeText(this, "On Skip Changed", Toast.LENGTH_SHORT).show();
    }


    public void validatePermition() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");

                Double lon = location.getLongitude();
                Double lat = location.getLatitude();
                Geocoder coder = new Geocoder(this, Locale.ENGLISH);
                String locality = coder.getFromLocation(lat, lon, 1).get(0).getLocality();

                SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lon", String.valueOf(lon));
                editor.putString("lat", String.valueOf(lat));
                editor.putString("locality", locality);
                editor.apply();

            } else {
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
