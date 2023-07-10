package com.service.keylessrn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.service.keylessrn.activity.R;

public class SwipeButtonFragment extends Fragment {
    public SwipeButtonFragment() {
    }

    public static SwipeButtonFragment newInstance(String param1, String param2) {
        return new SwipeButtonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_button, container, false);
    }
}