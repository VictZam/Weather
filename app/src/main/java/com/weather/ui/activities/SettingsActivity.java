package com.weather.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.weather.R;
import com.weather.ui.fragments.slideInfoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {


    @BindView(R.id.imageButtonGps)
    ImageButton imageButtonGps;
    @BindView(R.id.imageButtonMemory)
    ImageButton imageButtonMemory;
    @BindView(R.id.imageButtonCelcius)
    ImageButton imageButtonCelcius;
    @BindView(R.id.imageButtonFahrenheit)
    ImageButton imageButtonFahrenheit;
    @BindView(R.id.imageButtonSpanish)
    ImageButton imageButtonSpanish;
    @BindView(R.id.imageButtonEnglish)
    ImageButton imageButtonEnglish;

    @BindView(R.id.txtGps) TextView txtGps;
    @BindView(R.id.txtMemory) TextView txtMemory;
    @BindView(R.id.txtSetting) TextView txtSetting;

    @BindView(R.id.buttonSave) Button buttonSave;

    private CoordinatorLayout coordinatorLayout;

    private boolean gps = false;
    private boolean memory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.rootView);


        if(getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("setting", false)){
            this.startActivity(new Intent(this, slideInfoActivity.class));
        }
    }

    @OnClick(R.id.imageButtonCelcius)
    public void OnClickCelcius(View view) {
        imageButtonCelcius.setBackground(this.getDrawable(R.drawable.cel2));
        imageButtonFahrenheit.setBackground(this.getDrawable(R.drawable.fe));

        Toast.makeText(this, "Celcius", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("degrees", "c");
        editor.apply();
    }

    @OnClick(R.id.imageButtonFahrenheit)
    public void OnClickFahrenheit(View view) {
        imageButtonFahrenheit.setBackground(this.getDrawable(R.drawable.fah2));
        imageButtonCelcius.setBackground(this.getDrawable(R.drawable.ce));

        Toast.makeText(this, "Fahrenheit", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("degrees", "f");
        editor.apply();
    }

    @OnClick(R.id.imageButtonSpanish)
    public void OnClickSpanish(View view) {
        imageButtonEnglish.setBackground(this.getDrawable(R.drawable.usa2));
        imageButtonSpanish.setBackground(this.getDrawable(R.drawable.mex));
        txtSetting.setText("COFIGURACION");
        txtGps.setText("Permiso de ubicacion requerido");
        txtMemory.setText("Permiso de almacenamiento requerido");
        Toast.makeText(this, "Español", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", "es");
        editor.apply();
    }

    @OnClick(R.id.imageButtonEnglish)
    public void OnClickEnglish(View view) {
        imageButtonEnglish.setBackground(this.getDrawable(R.drawable.usa));
        imageButtonSpanish.setBackground(this.getDrawable(R.drawable.mex2));
        txtSetting.setText("SETTING");
        txtGps.setText("Location Permission Required");
        txtMemory.setText("Storage Permission Required");
        Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("language", "us");
        editor.apply();
    }


    @OnClick(R.id.imageButtonGps)
    public void OnClickGsp(View view) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            permissionGranted(1);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();

    }

    @OnClick(R.id.imageButtonMemory)
    public void OnClickMemory(View view) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permissionGranted(2);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();

    }


    public void permissionGranted(int mode){
        switch (mode) {
            case 1:
                imageButtonGps.setBackground(this.getDrawable(R.drawable.gps2));
                gps = true;
                break;
            case 2:
                imageButtonMemory.setBackground(this.getDrawable(R.drawable.memoria2));
                memory = true;
                break;
        }
    }


    @OnClick(R.id.buttonSave)
    public void OnClickSave(View view) {
        if(gps && memory) {
            SharedPreferences sharedPref = getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("setting", true);
            editor.apply();
            this.startActivity(new Intent(this, slideInfoActivity.class));
            finish();
        } else {
            if(getSharedPreferences("preferences", MODE_PRIVATE).getString("language", "es").equals("es")){
                Snackbar.make(coordinatorLayout, "Permisos requeridos para poder continuar", Snackbar.LENGTH_LONG)
                        .show();
            } else {
                Snackbar.make(coordinatorLayout, "Permissions required to continue", Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }


}
