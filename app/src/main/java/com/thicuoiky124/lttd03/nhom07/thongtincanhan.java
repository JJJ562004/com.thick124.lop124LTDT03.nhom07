package com.thicuoiky124.lttd03.nhom07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class thongtincanhan extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.thongtincanhan, container, false);

        Button returnbutton = view.findViewById(R.id.about_return_button);
        Button addbutton = view.findViewById(R.id.add_member_button);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(0);
            }
        });

        return view;
    }
}
