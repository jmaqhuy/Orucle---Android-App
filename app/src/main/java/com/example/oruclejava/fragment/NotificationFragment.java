package com.example.oruclejava.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oruclejava.R;


public class NotificationFragment extends Fragment {

    private TextView helloText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        helloText = view.findViewById(R.id.textView);

        return view;
    }
}