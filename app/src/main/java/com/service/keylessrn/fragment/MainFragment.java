package com.service.keylessrn.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.service.keylessrn.ResponseCallback;
import com.service.keylessrn.V5AidlInterface;
import com.service.keylessrn.activity.R;
import com.service.keylessrn.activity.databinding.FragmentMainBinding;
import com.service.keylessrn.model.LoginResponseModel;

public class MainFragment extends Fragment implements View.OnTouchListener {
    public static final String SERVICE_APP_PACKAGE = "com.service.keylessrn";
    public static final String SERVICE_APP_RECEIVER_CLASS = "com.service.keylessrn.receiver.Receiver";
    public static final String ACTION_DISABLE_LOCK_MODE = "android.intent.action.DISABLE_LOCK_MODE";
    V5AidlInterface v5AidlInterface;
    ServiceConnection serviceConnection;
    Handler handler;
    Runnable runnable;
    LinearLayout settingsLayout;
    MainFragmentClickHandlers clickHandlers;
    FragmentMainBinding fragmentMainBinding;
    NavController navController;
    float initialTouchY = 0;
    float initialHeight = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        settingsLayout = requireView().findViewById(R.id.settingsLayout);
        clickHandlers = new MainFragmentClickHandlers(view);
        fragmentMainBinding.setClickHandlers(clickHandlers);
        view.findViewById(R.id.login).setOnClickListener(this::login);
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        if (v5AidlInterface != null) {
            return;
        }
        TextView connectionStatusTextView = requireView().findViewById(R.id.connectionStatusText);
        connectionStatusTextView.setText("Connecting...");
        connectionStatusTextView.setTextColor(requireContext().getColor(R.color.black));
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i("KeylessRN", "Service Connected");
                v5AidlInterface = V5AidlInterface.Stub.asInterface(iBinder);
                connectionStatusTextView.setText("Connected");
                connectionStatusTextView.setTextColor(requireContext().getColor(R.color.green));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                v5AidlInterface = null;
            }
        };
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.service.keylessrn", "com.service.keylessrn.service.ClientService"));
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        handler = new Handler();
        runnable = () -> {
            if (v5AidlInterface == null) {
                connectionStatusTextView.setText("Connection Failed");
                connectionStatusTextView.setTextColor(requireContext().getColor(R.color.red));
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    public void login(View view) {
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
    public void onDestroy() {
        super.onDestroy();
        requireContext().unbindService(serviceConnection);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float Y = event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialTouchY = Y;
                initialHeight = settingsLayout.getLayoutParams().height;
                return true;
            case MotionEvent.ACTION_MOVE:
                adjustViewHeight(initialHeight + (Y - initialTouchY));
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return true;
    }

    private void adjustViewHeight(float deltaY) {
        ViewGroup.LayoutParams layoutParams = settingsLayout.getLayoutParams();
        layoutParams.height = Math.max((int) deltaY, 0);
        settingsLayout.setLayoutParams(layoutParams);
    }

    public static class MainFragmentClickHandlers {
        NavController navController;

        public MainFragmentClickHandlers(View view) {
            navController = Navigation.findNavController(view);
        }

        public void removeServiceAppAsOwner(View view) {
            Intent intent = new Intent(ACTION_DISABLE_LOCK_MODE);
            intent.setComponent(new ComponentName(SERVICE_APP_PACKAGE, SERVICE_APP_RECEIVER_CLASS));
            view.getContext().sendBroadcast(intent);
        }

        public void swipeButton(View view) {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToSwipeButtonFragment());
        }

        public void thermostatSlider(View view) {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToThermostatSliderFragment());
        }

        public void stepCounter(View view) {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToStepsFragment());
        }

        public void rotaryDialer(View view) {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToRotaryDialerFragment());
        }

        public void cubeButton(View view) {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToTrainingFragment());
        }
    }
}