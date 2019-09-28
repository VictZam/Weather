package com.weather.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.weather.R;
import com.weather.data.db.WeatherLocation;
import com.weather.data.local.Principal;
import com.weather.services.api.WeatherApi;
import com.weather.ui.adapters.ViewPagerAdapter;
import com.weather.ui.fragments.TodayWeatherFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;


public class HomeActivity extends AppCompatActivity implements LocationListener {

    private androidx.appcompat.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CoordinatorLayout coordinatorLayout;

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

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.rootView);

        toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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

                            saveCurrentLocationWeather();

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
        adapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //validatePermition();
       // locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //validatePermition();
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

        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            Log.e("TAG", "GPS is on");
            Principal.lon = location.getLongitude();
            Principal.lat = location.getLatitude();
            saveCurrentLocationWeather();
        }
        else{
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        }
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
                Principal.locality = coder.getFromLocation(Principal.lat, Principal.lon, 1).get(0).getLocality();

                Toast.makeText(this, Principal.locality, Toast.LENGTH_SHORT).show();

                WeatherApi.getInstance().fetchWeather(
                        String.valueOf(Principal.lat), String.valueOf(Principal.lon), Principal.locality, isSuccess -> {
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

            /*txtCity.setText(String.format("%s", weatherLocation.getLocality()));
            txtLastUpdate.setText(String.format("Last Updated: %s", weatherLocation.getWeather().get(0).getDate()));
            txtDescription.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue()));
            txtHumidity.setText(String.format("%.1f", weatherLocation.getCurrentCondition().get(0).getHumidity()));
            txtTime.setText(String.format("%s", weatherLocation.getCurrentCondition().get(0).getObservationTime()));
            txtCelsius.setText(String.format("%.2f °C", weatherLocation.getCurrentCondition().get(0).getCelcius()));
            Picasso.with(HomeActivity.this)
                    .load(R.drawable.nube)
                    .into(imageView);*/
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

    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

}
