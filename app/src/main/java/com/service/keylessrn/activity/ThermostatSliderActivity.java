package com.service.keylessrn.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ThermostatSliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thermostat_slider);

        SwipeButton swipeButton = findViewById(R.id.swipeButtonActivity);
        swipeButton.setSwipeToLockListener(() -> {
            Log.i("SwipeButton", "Locked Successfully");
        });

        swipeButton.setSwipeToUnlockListener(() -> {
            Log.i("SwipeButton", "UnLocked Successfully");
        });
    }
}