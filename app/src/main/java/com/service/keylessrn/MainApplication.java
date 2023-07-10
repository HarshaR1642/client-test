package com.service.keylessrn;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

interface ErrorBoundary {
    void uncaughtException(Thread thread, Throwable throwable);
}

public class MainApplication extends Application implements ErrorBoundary, View.OnTouchListener {

    @Override
    public void onCreate() {
        super.onCreate();
        setupExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        Log.d("uncaughtException", throwable.getMessage());
        try {
            Looper.loop();
        } catch (Throwable t) {
            if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
                uncaughtException(Looper.getMainLooper().getThread(), t);
            }
        }
    }

    public void setupExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
    Log.i("onTouch", "onTouch");
        return false;
    }
}
