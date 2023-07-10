package com.service.keylessrn.components;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.service.keylessrn.activity.R;

interface SwipeToLockListener {
    void onSwipeToLock();
}

interface SwipeToUnlockListener {
    void onSwipeToUnlock();
}

public class SwipeButton extends FrameLayout {

    private float L_dX;
    private float R_dX;
    private LinearLayout swipeLayout;
    private ImageButton swipeRight;
    private ImageButton swipeLeft;
    private LottieAnimationView lockAnimationView;
    private LottieAnimationView unlockAnimationView;
    LottieAnimationView rippleAnimationView;
    private SwipeToLockListener swipeToLockListener;
    private SwipeToUnlockListener swipeToUnlockListener;
    private boolean lockButtonTouched = false;
    private boolean unlockButtonTouched = false;

    public SwipeButton(Context context) {
        super(context);
        init(context, null);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setSwipeToLockListener(SwipeToLockListener listener) {
        this.swipeToLockListener = listener;
    }

    public void setSwipeToUnlockListener(SwipeToUnlockListener listener) {
        this.swipeToUnlockListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.swipe_button_layout, this, true);

        swipeLayout = findViewById(R.id.swipe_layout);
        swipeRight = findViewById(R.id.swipe_right);
        swipeLeft = findViewById(R.id.swipe_left);

        lockAnimationView = findViewById(R.id.lockAnimationView);
        unlockAnimationView = findViewById(R.id.unlockAnimationView);
        rippleAnimationView = findViewById(R.id.rippleAnimationView);

        swipeRight.setOnTouchListener(this::lockTouchListener);
        swipeLeft.setOnTouchListener(this::unlockTouchListener);
    }

    private boolean lockTouchListener(View v, MotionEvent event) {
        if (unlockButtonTouched) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L_dX = v.getX() - event.getRawX();
                lockAnimationView.setVisibility(View.GONE);
                lockButtonTouched = true;
                return true;
            case MotionEvent.ACTION_UP:
                lockButtonTouched = false;
                float _x = event.getRawX() + L_dX;
                if (_x > ((swipeLayout.getWidth() - 2 * swipeLeft.getWidth()) * 0.7)) {
                    startAndHideRipple();
                    if (this.swipeToLockListener != null) {
                        this.swipeToLockListener.onSwipeToLock();
                    }
                }
                lockAnimationView.setVisibility(View.GONE);
                final ValueAnimator positionAnimator =
                        ValueAnimator.ofFloat(swipeRight.getX(), 0);
                positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                positionAnimator.addUpdateListener(animation -> {
                    float x = (Float) positionAnimator.getAnimatedValue();
                    swipeRight.setX(x);
                });

                positionAnimator.setDuration(300);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(positionAnimator);
                animatorSet.start();
                return true;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + L_dX;
                if (newX < 0) {
                    swipeRight.setX(0);
                } else if (newX > (swipeLayout.getWidth() - 2 * swipeLeft.getWidth())) {
                    swipeRight.setX(swipeLayout.getWidth() - 2 * swipeLeft.getWidth());
                } else {
                    swipeRight.setX(newX);
                }
                return true;
        }
        return false;
    }

    private boolean unlockTouchListener(View v, MotionEvent event) {
        if (lockButtonTouched) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                unlockButtonTouched = true;
                R_dX = v.getX() - event.getRawX();
                unlockAnimationView.setVisibility(View.GONE);
                return true;
            case MotionEvent.ACTION_UP:
                unlockButtonTouched = false;
                float _x = event.getRawX() + R_dX;
                if (_x < (swipeLeft.getWidth()) * 1.3) {
                    startAndHideRipple();
                    if (this.swipeToUnlockListener != null) {
                        this.swipeToUnlockListener.onSwipeToUnlock();
                    }
                }

                unlockAnimationView.setVisibility(View.GONE);
                final ValueAnimator positionAnimator =
                        ValueAnimator.ofFloat(swipeLeft.getX(), swipeLayout.getWidth() - swipeLeft.getWidth());
                positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                positionAnimator.addUpdateListener(animation -> {
                    float x = (Float) positionAnimator.getAnimatedValue();
                    swipeLeft.setX(x);
                });

                positionAnimator.setDuration(300);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(positionAnimator);
                animatorSet.start();
                return true;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + R_dX;
                if (newX < swipeRight.getWidth()) {
                    swipeLeft.setX(swipeRight.getWidth());
                } else {
                    swipeLeft.setX(Math.min(newX, swipeLayout.getWidth() - swipeLeft.getWidth()));
                }

                return true;
        }
        return false;
    }

    public void startAndHideRipple() {
        rippleAnimationView.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rippleAnimationView.setVisibility(View.GONE);
            }
        }, 5000);
    }
}
