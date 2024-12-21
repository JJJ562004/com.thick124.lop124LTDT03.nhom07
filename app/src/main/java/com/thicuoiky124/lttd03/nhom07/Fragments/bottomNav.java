package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.albumia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thicuoiky124.lttd03.nhom07.Adapters.ViewPageAdapter;
import com.thicuoiky124.lttd03.nhom07.MainActivity;

public class bottomNav extends Fragment {
    private ViewPager viewPager;
    private BottomNavigationView bnv;

    public  bottomNav(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_nav, container, false);

        viewPager = view.findViewById(R.id.bottom_nav_viewpage);
        bnv = view.findViewById(R.id.bottomNavigation);

        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFragment(new TrangcanhanActivity());
        adapter.addFragment(new following());
        adapter.addFragment(new homepage());
        adapter.addFragment(new follower());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        bnv.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;
                    case 1:
                        bnv.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 2:
                        bnv.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 4:
                        bnv.getMenu().findItem(R.id.menu_group).setChecked(true);
                        break;
                    case 3:
                        bnv.getMenu().findItem(R.id.menu_add).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_add){
                    ((MainActivity) requireActivity()).customViewPager.setCurrentItem(4);
                }
                if (item.getItemId() == R.id.menu_profile) {
                    viewPager.setCurrentItem(0);
                }
                if (item.getItemId() == R.id.menu_home) {
                    viewPager.setCurrentItem(2);
                }
                if(item.getItemId() == R.id.menu_search){
                    viewPager.setCurrentItem(1);
                }
                if (item.getItemId() == R.id.menu_group){
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
        return view;
    }
    public void switchToFragment(int position) {
        viewPager.setCurrentItem(position);
    }
}