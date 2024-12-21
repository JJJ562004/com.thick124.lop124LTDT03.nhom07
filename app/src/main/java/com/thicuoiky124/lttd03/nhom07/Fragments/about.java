package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.albumia.MainActivity;
import com.example.albumia.R;

public class about extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        Button returnbutton = view.findViewById(R.id.about_return_button);

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(0);
            }
        });

        return view;
    }
}