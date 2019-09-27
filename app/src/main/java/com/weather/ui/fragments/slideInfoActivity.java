package com.weather.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.weather.R;
import com.weather.ui.activities.HomeActivity;

public class slideInfoActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance("CLIMA",
                "Sol",
                R.drawable.sol,
                Color.parseColor("#51e2b7")));

        addSlide(AppIntro2Fragment.newInstance("CLIMA",
                "Nube",
                R.drawable.nube,
                Color.parseColor("#51e2b7")));

        addSlide(AppIntro2Fragment.newInstance("CLIMA",
                "Noche",
                R.drawable.noche,
                Color.parseColor("#51e2b7")));

        addSlide(AppIntro2Fragment.newInstance("CLIMA",
                "Trueno",
                R.drawable.trueno,
                Color.parseColor("#51e2b7")));

        addSlide(AppIntro2Fragment.newInstance("CLIMA6",
                "Lluvia",
                R.drawable.soltar,
                Color.parseColor("#51e2b7")));

        showStatusBar(false);
        setBarColor(Color.parseColor("#333639"));
        setSeparatorColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onDonePressed(){
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment){
        startActivity(new Intent(this, HomeActivity.class));
        Toast.makeText(this, "On Skip Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlideChanged() {
        Toast.makeText(this, "On Skip Changed", Toast.LENGTH_SHORT).show();
    }

}
