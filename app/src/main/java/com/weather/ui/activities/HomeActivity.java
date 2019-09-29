package com.weather.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.weather.R;
import com.weather.ui.adapters.ViewPagerAdapter;
import com.weather.ui.fragments.CitySearchFragment;
import com.weather.ui.fragments.ForecastFragment;
import com.weather.ui.fragments.TodayWeatherFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private androidx.appcompat.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CoordinatorLayout coordinatorLayout;

    @BindView(R.id.buttonSetting) Button buttonSetting;

    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.rootView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        validatePermition();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            provider = locationManager.getBestProvider(new Criteria(), false);

                            viewPager = (ViewPager)findViewById(R.id.viewPager);
                            setupViewPage(viewPager);
                            tabLayout = (TabLayout)findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();
    }

    private void setupViewPage(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
            adapter.addFragment(TodayWeatherFragment.getInstance(), "CLIMA DE HOY");
            adapter.addFragment(ForecastFragment.getInstance(), "7 DIAS");
            adapter.addFragment(CitySearchFragment.getInstance(), "BUSQUEDA");
        } else {
            adapter.addFragment(TodayWeatherFragment.getInstance(), "TODAY WEATHER");
            adapter.addFragment(ForecastFragment.getInstance(), "7 DAYS");
            adapter.addFragment(CitySearchFragment.getInstance(), "SEARCH");
        }
        viewPager.setAdapter(adapter);

    }

    
    @Override
    protected void onPause() {
        super.onPause();
        validatePermition();
        //locationManager.removeUpdates(this);
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
           return;
        }

        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            Log.e("TAG", "GPS is on");

            SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("lon", String.valueOf(location.getLongitude()));
            editor.putString("lat", String.valueOf(location.getLatitude()));
            editor.apply();

            saveCurrentLocationWeather();
        }
        else{
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lon", String.valueOf(location.getLongitude()));
        editor.putString("lat", String.valueOf(location.getLatitude()));
        editor.apply();
        saveCurrentLocationWeather();
    }

    public void saveCurrentLocationWeather(){
        try {
            if(getSharedPreferences("preferences", MODE_PRIVATE).getString("lat", null) != "0"
                    && getSharedPreferences("preferences", MODE_PRIVATE).getString("lon", null) != "0") {
                Geocoder coder = new Geocoder(this, Locale.ENGLISH);

                SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("locality", coder.getFromLocation(Double.parseDouble(getSharedPreferences("preferences", MODE_PRIVATE).getString("lat", null)),
                        Double.parseDouble(getSharedPreferences("preferences", MODE_PRIVATE).getString("lon", null)),
                        1).get(0).getLocality());
                editor.apply();

                Snackbar.make(coordinatorLayout,  getSharedPreferences("preferences", MODE_PRIVATE).getString("locality", null), Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                if(getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")) {
                    Snackbar.make(coordinatorLayout, "Por favor tome ubicacion", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Snackbar.make(coordinatorLayout, "Please take location", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @OnClick(R.id.buttonSetting)
    public void OnClickSetting(View view) {
        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("setting", false);
        editor.apply();

        this.startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }



}
