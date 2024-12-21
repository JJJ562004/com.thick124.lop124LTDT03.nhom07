package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.albumia.Adapters.ImageAdapter;
import com.example.albumia.CLasses.Account;
import com.example.albumia.MainActivity;
import com.example.albumia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class signup extends Fragment {
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private Handler handler = new Handler();
    private boolean isScrollingDown = true;
    private Button signup_signup_button;
    private Button signup_login_button;
    private TextView maleTextView;
    private TextView femaleTextView;
    private TextView otherTextView;
    private Button member_btn;
    private int currentScrollIndex =0;
    private EditText mail_editview;
    private EditText password_editview;
    private EditText name_editview;
    private EditText phone_editview;
    private EditText dob_editview;
    private String selectedGender = "";

    public signup(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        signup_login_button = view.findViewById(R.id.signup_dn);
        signup_signup_button = view.findViewById(R.id.signup_dk);
        maleTextView = view.findViewById(R.id.boxMale);
        femaleTextView = view.findViewById(R.id.boxFemale);
        otherTextView = view.findViewById(R.id.boxOther);
        member_btn = view.findViewById(R.id.member_button);
        recyclerView = view.findViewById(R.id.signup_recyleView);
        mail_editview = view.findViewById(R.id.signup_email);
        password_editview = view.findViewById(R.id.signup_pass);
        name_editview = view.findViewById(R.id.signup_name);
        phone_editview = view.findViewById(R.id.signup_sdt);
        dob_editview = view.findViewById(R.id.signup_bod);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //Static Image Data for Testing
        List<String> imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(getContext(), imageUrls);
        recyclerView.setAdapter(imageAdapter);
        fetchImages(imageUrls);
        //auto_scroll_login_recycleView_Image_function
        startScrolling(recyclerView);

        maleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(maleTextView);
                setUnselected(femaleTextView, otherTextView);
                selectedGender = "Male";
            }
        });
        femaleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(femaleTextView);
                setUnselected(maleTextView, otherTextView);
                selectedGender = "Female";
            }
        });
        otherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(otherTextView);
                setUnselected(femaleTextView, femaleTextView);
                selectedGender = "Other";
            }
        });

        signup_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(0);
            }
        });
        signup_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAccountData();
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(0);
            }
        });

        member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(9);
            }
        });

//        imageAdapter = new ImageAdapter(viewPager, images, new ImageAdapter.OnImageClickListener() {
//            @Override
//            public void onImageClick(int position) {
//            }
//        });
        recyclerView.setAdapter(imageAdapter);

        //login_recycleView_Image
        startScrolling(recyclerView);

        return view;
    }

    private void fetchImages(List<String> imageUrls) {
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
    }

    private void submitAccountData() {
        // Get values from EditText fields
        String email = mail_editview.getText().toString().trim();
        String password = password_editview.getText().toString().trim();
        String name = name_editview.getText().toString().trim();
        String phone = phone_editview.getText().toString().trim();

        // Check if gender is selected
        if (selectedGender.isEmpty()) {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure all fields are filled
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Account object
        Account account = new Account(email, password, name, phone, selectedGender);

        // Get a reference to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accounts");

        // Push the account data to Firebase
        databaseReference.push().setValue(account)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                        // Optionally navigate to another fragment or screen
                    } else {
                        Toast.makeText(getContext(), "Failed to create account. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startScrolling(RecyclerView rv) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollAmount = 300;
                if (isScrollingDown) {
                    rv.smoothScrollBy(0, scrollAmount);
                    currentScrollIndex += 1;

                    if (currentScrollIndex * scrollAmount >= rv.computeVerticalScrollRange() - rv.getHeight()) {
                        isScrollingDown = false;
                    }
                } else {
                    rv.smoothScrollBy(0, -scrollAmount);
                    currentScrollIndex -= 1;

                    if (currentScrollIndex <= 0) {
                        isScrollingDown = true;
                    }
                }

                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void setSelected(TextView tv){
        tv.setTextColor(Color.parseColor("#FF000000"));
        tv.setBackgroundResource(R.drawable.corner_round_editext);
    }

    private void setUnselected(TextView tv1, TextView tv2){
        tv1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tv1.setTextColor(Color.parseColor("#FFFFFFFF"));
        tv2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        tv2.setTextColor(Color.parseColor("#FFFFFFFF"));
    }
}