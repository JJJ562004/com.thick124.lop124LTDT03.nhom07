package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumia.Adapters.FollowerAdapter;
import com.example.albumia.CLasses.User;
import com.example.albumia.R;

import java.util.ArrayList;
import java.util.List;

public class follower extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.follower_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<User> followers = new ArrayList<>();
        followers.add(new User("Alice", "Professional Model", 192, R.drawable.test_image));
        followers.add(new User("Bob", "Photographer", 305, R.drawable.test_image));
        followers.add(new User("Charlie", "Blogger", 500, R.drawable.test_image));

        FollowerAdapter adapter = new FollowerAdapter(followers);
        recyclerView.setAdapter(adapter);
        return view;
    }
}