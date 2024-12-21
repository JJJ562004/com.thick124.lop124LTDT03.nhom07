package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.albumia.Adapters.ImageAdapter;
import com.example.albumia.MainActivity;
import com.example.albumia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class homepage extends Fragment {
   private ViewPager viewPager;
   private bottomNav bnv;
   private ValueEventListener valueEventListener;
   private DatabaseReference databaseReference;

   public homepage() {
      // Default constructor
   }

   public static homepage newInstance(bottomNav bnv) {
      homepage fragment = new homepage();
      fragment.bnv = bnv;
      return fragment;
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_homepage, container, false);

      ImageView bell = view.findViewById(R.id.homepage_bell);
      ImageView menu = view.findViewById(R.id.homepage_overflow_menu);

      bell.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            ((MainActivity) requireActivity()).customViewPager.setCurrentItem(8);
         }
      });

      menu.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            showOverflowMenu(v);
         }
      });

      RecyclerView recyclerView = view.findViewById(R.id.homepage_recyclerview);
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

      List<String> imageUrls = new ArrayList<>();
      ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUrls, imageUrl -> {
         // Navigate to the new fragment
         Fragment newFragment = new comment_section();

         // Pass the clicked image URL to the new fragment using Bundle
         Bundle args = new Bundle();
         args.putString("imageUrl", imageUrl);
         newFragment.setArguments(args);

         // Replace the current fragment
         requireActivity().getSupportFragmentManager()
                 .beginTransaction()
                 .replace(R.id.homepage_layout, newFragment)
                 .addToBackStack(null)
                 .commit();
      });
      recyclerView.setAdapter(imageAdapter);

      // Fetch image list from Firebase Storage
      StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads/");

      storageReference.listAll().addOnSuccessListener(result -> {
         for (StorageReference item : result.getItems()) {
            item.getDownloadUrl().addOnSuccessListener(uri -> {
               String imageUrl = uri.toString();
               Log.d("Image URL", imageUrl);  // Log the image URL for debugging
               imageUrls.add(imageUrl);  // Add URL to the list
               imageAdapter.notifyDataSetChanged();  // Notify the adapter
            }).addOnFailureListener(e -> {
               Log.e("FirebaseStorage", "Failed to get image URL", e);
            });
         }
      }).addOnFailureListener(e -> {
         Log.e("FirebaseStorage", "Failed to list images", e);
      });

      return view;
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      if (databaseReference != null && valueEventListener != null) {
         databaseReference.removeEventListener(valueEventListener);
      }
   }

   private void clearActiveUser() {
      SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

      sharedPreferences.edit().remove("UserName").apply();
   }


   private void showOverflowMenu(View view) {
      PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.CENTER);
      popupMenu.getMenuInflater().inflate(R.menu.overflow_menu, popupMenu.getMenu());

      popupMenu.setOnMenuItemClickListener(item -> {
         if (item.getItemId() == R.id.overflow_menu_like_image) {
            ((MainActivity) getActivity()).switchToFragment(6);
            return true;
         }
         if (item.getItemId() == R.id.overflow_menu_following) {
            bnv.switchToFragment(1);
            return true;
         }
         if (item.getItemId() == R.id.overflow_menu_upimage) {
            ((MainActivity) getActivity()).switchToFragment(6);
            return true;
         }
         if (item.getItemId() == R.id.overflow_menu_profile) {
            bnv.switchToFragment(0);
            return true;
         }
         if (item.getItemId() == R.id.overflow_menu_signout) {
            clearActiveUser();
            ((MainActivity) getActivity()).switchToFragment(0);
            return true;
         }
         return false;
      });

      popupMenu.show();
   }
}
