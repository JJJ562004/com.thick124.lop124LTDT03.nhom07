package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumia.Adapters.AdapterGridChonAnh;
import com.example.albumia.Adapters.ImageAdapter;
import com.example.albumia.CLasses.chonanh;
import com.example.albumia.MainActivity;
import com.example.albumia.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TrangcanhanActivity extends Fragment {
    int[] image = {R.drawable.thiennhien1, R.drawable.thiennhien1, R.drawable.thiennhien1, R.drawable.thiennhien1, R.drawable.thiennhien1, R.drawable.thiennhien1};

    RecyclerView rv;
    ImageView imageView;
    ArrayList<chonanh> Gridchonanh;
    AdapterGridChonAnh adapterGridChonAnh;
    Button ayt, anh_dang, btn1, btn2;
    TextView profName;
    private bottomNav bnv;
    private ImageAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trangcanhan, container, false);

        // Initialize views
        initViews(view);

        // Set button listeners
        setButtonListeners();

        // Initialize RecyclerView and ImageAdapter
        List<String> imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(getContext(), imageUrls);
        rv.setAdapter(imageAdapter);

        // Fetch profile images from Firebase Storage
        fetchProfileImages(imageUrls);
        profName.setText(getActiveUsername());

        return view;
    }

    /**
     * Initialize views from the layout
     */
    private void initViews(View view) {
        rv = view.findViewById(R.id.rvh);
        ayt = view.findViewById(R.id.button_yeuthich);
        anh_dang = view.findViewById(R.id.button_dobantao);
        btn1 = view.findViewById(R.id.button);
        btn2 = view.findViewById(R.id.button2);
        profName = view.findViewById(R.id.textView3);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    /**
     * Set listeners for the buttons
     */
    private void setButtonListeners() {
        btn1.setOnClickListener(v -> showOverflowMenu(v));

        btn2.setOnClickListener(v -> {
            ((MainActivity) getActivity()).switchToFragment(8);
        });

        ayt.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).customViewPager.setCurrentItem(6);
        });

        anh_dang.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).customViewPager.setCurrentItem(8);
        });
    }

    /**
     * Fetch profile images from Firebase Storage and update the RecyclerView
     *
     * @param imageUrls List to store image URLs
     */
    private void fetchProfileImages(List<String> imageUrls) {
        String activeUserName = getActiveUsername(); // Get active username

        if (activeUserName != null) {
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("uploads/" + activeUserName + "/");

            storageReference.listAll().addOnSuccessListener(result -> {
                for (StorageReference item : result.getItems()) {
                    fetchImageDownloadUrl(item, imageUrls);
                }
            }).addOnFailureListener(e -> {
                Log.e("FirebaseStorage", "Failed to list images for user: " + activeUserName, e);
            });
        } else {
            Log.e("UserPrefs", "ActiveUserName not found in JSON");
        }
    }


    /**
     * Fetch the download URL for a single image and update the list
     *
     * @param item      StorageReference for the image
     * @param imageUrls List to store image URLs
     */
    private void fetchImageDownloadUrl(StorageReference item, List<String> imageUrls) {
        item.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            Log.d("Image URL", imageUrl); // Log the image URL for debugging
            imageUrls.add(imageUrl);       // Add URL to the list
            imageAdapter.notifyDataSetChanged(); // Notify the adapter to update the UI
        }).addOnFailureListener(e -> {
            Log.e("FirebaseStorage", "Failed to get image URL", e);
        });
    }

    private String getActiveUsername() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("UserName", null);
    }


    private void clearActiveUser() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        sharedPreferences.edit().remove("UserName").apply();
    }


    private void showOverflowMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.CENTER);
        popupMenu.getMenuInflater().inflate(R.menu.overflow_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.overflow_menu_like_image){
                    ((MainActivity) getActivity()).switchToFragment(6);
                    return true;
                }

                if(item.getItemId() == R.id.overflow_menu_following){
                    bnv.switchToFragment(1);
                    return true;
                }
                if(item.getItemId() == R.id.overflow_menu_upimage){
                    ((MainActivity) getActivity()).switchToFragment(6);
                    return true;
                }
                if(item.getItemId() == R.id.overflow_menu_profile){
                    bnv.switchToFragment(0);
                    return true;
                }
                if(item.getItemId() == R.id.overflow_menu_signout){
                    clearActiveUser();
                    ((MainActivity) getActivity()).switchToFragment(0);
                    return true;
                }
                return false;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }


}
