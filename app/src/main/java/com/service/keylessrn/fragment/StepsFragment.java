package com.service.keylessrn.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.service.keylessrn.activity.R;

public class StepsFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;

    private boolean running = false;

    private float totalSteps = 0f;

    private float previousTotalSteps = 0f;

    public StepsFragment() {
    }

    public static StepsFragment newInstance() {
        return new StepsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        resetSteps();

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            Toast.makeText(getContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void resetSteps() {
        TextView tv_stepsTaken = requireView().findViewById(R.id.tv_stepsTaken);
        tv_stepsTaken.setOnClickListener(view -> Toast.makeText(getContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show());

        tv_stepsTaken.setOnClickListener(view -> {
            previousTotalSteps = totalSteps;

            tv_stepsTaken.setText(String.valueOf(0));

            saveData();
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousTotalSteps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        previousTotalSteps = sharedPreferences.getFloat("key1", 0f);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TextView tv_stepsTaken = requireView().findViewById(R.id.tv_stepsTaken);

        if (running) {
            totalSteps = sensorEvent.values[0];

            Log.i("totalSteps", "" + totalSteps);

            int currentSteps = (int) (totalSteps - previousTotalSteps);

            tv_stepsTaken.setText(String.valueOf(currentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}