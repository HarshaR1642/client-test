package com.service.keylessrn.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView dot3 = findViewById(R.id.dot3);
        ImageView dot4 = findViewById(R.id.dot4);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.move);
        dot1.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.move);
        dot2.startAnimation(animation2);

        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.move);
        dot3.startAnimation(animation3);

        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.move);
        dot4.startAnimation(animation4);
    }
}