package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumia.Adapters.ImageAdapter;
import com.example.albumia.MainActivity;
import com.example.albumia.R;

import java.util.ArrayList;
import java.util.List;

public class uploadedimage extends Fragment {

    private ImageButton btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploadedimage, container, false);

        btn = view.findViewById(R.id.return_image_upload);
        RecyclerView recyclerView = view.findViewById(R.id.upImage_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToFragment(2);
            }
        });

        //Static images data
//        List<Image> imagesList = new ArrayList<>();
//        imagesList.add(new Image( R.drawable.test_image, "Professional Picture", 192, comment_section.getCommentList(), false));
//        imagesList.add(new Image( R.drawable.test_image, "Beautiful Picture", 300, comment_section.getCommentList(), false));
//        imagesList.add(new Image( R.drawable.test_image, "Hello Picture", 192, comment_section.getCommentList(), false));
        List<String> imageUrls = new ArrayList<>();
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUrls);
        recyclerView.setAdapter(imageAdapter);
        return view;
    }
}