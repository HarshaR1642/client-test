package com.service.keylessrn.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.service.keylessrn.V5AidlInterface;
import com.service.keylessrn.ResponseCallback;
import com.service.keylessrn.model.LoginResponseModel;

public class MainActivity extends AppCompatActivity {

    V5AidlInterface v5AidlInterface;
    ServiceConnection serviceConnection;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.login);
        button.setOnClickListener(this::onClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    public void init() {
        if (v5AidlInterface != null) {
            return;
        }
        TextView connectionStatusTextView = findViewById(R.id.connectionStatusText);
        connectionStatusTextView.setText("Connecting...");
        connectionStatusTextView.setTextColor(getColor(R.color.black));
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i("KeylessRN", "Service Connected");
                v5AidlInterface = V5AidlInterface.Stub.asInterface(iBinder);
                connectionStatusTextView.setText("Connected");
                connectionStatusTextView.setTextColor(getColor(R.color.green));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                v5AidlInterface = null;
            }
        };

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.service.keylessrn", "com.service.keylessrn.service.ClientService"));

        boolean bound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.i("KeylessRN", "Bound " + bound);
        handler = new Handler();
        runnable = () -> {
            if (v5AidlInterface == null) {
                connectionStatusTextView.setText("Connection Failed");
                connectionStatusTextView.setTextColor(getColor(R.color.red));
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public void onClick(View view) {
            init();
            Bundle bundle = new Bundle();
            bundle.putString("email", "harsha.m@mailinator.com");
            bundle.putString("password", "Harsha@123");
            if (v5AidlInterface != null) {
                try {
                    v5AidlInterface.login(bundle, new ResponseCallback.Stub() {
                        @Override
                        public void onResponse(LoginResponseModel response) {
                            Log.i("KeylessRN Access Token", response.getAccess_token());
                            Log.i("KeylessRN Refresh Token", response.getRefresh_token());
                            Log.i("KeylessRN Id Token", response.getId_token());

                        }
                    });
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("KeylessRN", "Unable to establish connection");
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}