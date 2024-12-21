    package com.thicuoiky124.lttd03.nhom07.Fragments;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.os.Handler;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.viewpager.widget.ViewPager;

    import com.example.albumia.Adapters.ImageAdapter;
    import com.example.albumia.MainActivity;
    import com.example.albumia.R;
    import com.google.android.gms.auth.api.signin.GoogleSignIn;
    import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
    import com.google.android.gms.auth.api.signin.GoogleSignInClient;
    import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
    import com.google.android.gms.common.api.ApiException;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthCredential;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.auth.GoogleAuthProvider;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class login extends Fragment {

        private ViewPager viewPager;
        private RecyclerView recyclerView;
        private ImageAdapter imageAdapter;
        private Handler handler = new Handler();
        private boolean isScrollingDown = true;
        private Button login_signup_button;
        private Button login_login_button;
        private int currentScrollIndex = 0;
        private EditText login_username;
        private Button member_btn;
        private EditText login_pass;
        private FirebaseAuth auth;
        private DatabaseReference databaseReference;
        private Button login_google;
        private static final int RC_SIGN_IN = 9001;
        private GoogleSignInClient googleSignInClient;

        public login() {
        }

        ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        if (result.getResultCode() == Activity.RESULT_CANCELED) {
                            // Error Easter Egg
                            ((MainActivity) requireActivity()).customViewPager.setCurrentItem(2);
                            Log.d("GoogleSignIn", "Sign-In was canceled by the user");
                        }
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account);
                                Log.d("GoogleSignIn", "Google account selected: " + account.getDisplayName());
                            } else {
                                Log.d("GoogleSignIn NULL", "NULL GG ACCOUNT");
                            }


                        } catch (ApiException e) {
                            Log.w("GoogleSignIn", "Google sign-in failed", e);
                        }
                    } else {
                        Log.d("RESULT CODE", "resultCode: " + result.getResultCode());
                        Log.w("GoogleSignIn Data", "Data: " + result.getData());
                    }
                }
        );

        private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // Add the user to the Realtime Database
                                addUserToDatabase(user);
                                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(2);
                            }
                        } else {
                            Log.w("FirebaseAuth", "Authentication failed", task.getException());
                        }
                    });
        }

        private void signInWithGoogle(GoogleSignInClient googleSignInClient) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        }

        private void addUserToDatabase(FirebaseUser user) {
            // Get reference to the database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("accounts");

            // Create a unique key
            String userId = databaseReference.push().getKey();

            // User details
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("email", user.getEmail());
            userDetails.put("name", user.getDisplayName());
            userDetails.put("gender", "Male");
            userDetails.put("phone", user.getPhoneNumber() != null ? user.getPhoneNumber() : "0912343512");
            userDetails.put("password", "123");  // base password

            // Save the user under the unique key
            if (userId != null) {
                databaseReference.child(userId).setValue(userDetails)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseDB", "User added successfully");
                            } else {
                                Log.e("FirebaseDB", "Failed to add user", task.getException());
                            }
                        });
            } else {
                Log.e("NULL", "USER ID IS NULL");
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_login, container, false);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

            login_login_button = view.findViewById(R.id.login_dn);
            login_signup_button = view.findViewById(R.id.login_dk);
            login_username = view.findViewById(R.id.login_username);
            recyclerView = view.findViewById(R.id.login_recyleView);
            member_btn = view.findViewById(R.id.member_button);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            login_pass = view.findViewById(R.id.login_pass);
            login_google = view.findViewById(R.id.login_gg);
            login_google.setOnClickListener(v -> {
                Log.d("GoogleSignIn", "Google sign-in button clicked");
                signInWithGoogle(googleSignInClient);
            });



            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", login_username.getText().toString());
            editor.apply();

            List<String> imageUrls = new ArrayList<>();
            imageAdapter = new ImageAdapter(getContext(), imageUrls);
            recyclerView.setAdapter(imageAdapter);
            fetchImages(imageUrls);
            //auto_scroll_login_recycleView_Image_function
            startScrolling(recyclerView);

            login_login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String u_name = login_username.getText().toString().trim();
                    String u_pass = login_pass.getText().toString().trim();

                    if (u_name.isEmpty() || u_pass.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        auth = FirebaseAuth.getInstance();
                        // Query the 'accounts' node to find a matching username and password
                        DatabaseReference accountsReference = FirebaseDatabase.getInstance().getReference("accounts");

                        accountsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean loginSuccessful = false;

                                for (DataSnapshot accountSnapshot : snapshot.getChildren()) {
                                    String name = accountSnapshot.child("name").getValue(String.class);
                                    String password = accountSnapshot.child("password").getValue(String.class);

                                    // Check if the provided username and password match
                                    if (u_name.equals(name) && u_pass.equals(password)) {
                                        saveUsername(name);

                                        loginSuccessful = true;
                                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                        // Navigate to the desired page
                                        ((MainActivity) requireActivity()).customViewPager.setCurrentItem(2);
                                        break;
                                    }
                                }

                                if (!loginSuccessful) {
                                    // Username or password is incorrect
                                    Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database errors
                                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            member_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) requireActivity()).customViewPager.setCurrentItem(9);
                }
            });
            login_signup_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) requireActivity()).customViewPager.setCurrentItem(1);
                }
            });
//            imageAdapter = new ImageAdapter(viewPager, images, new ImageAdapter.OnImageClickListener() {
//                @Override
//                public void onImageClick(int position) {
//
//                }
//            });


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

        private void saveUsername(String username) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

            sharedPreferences.edit().putString("UserName", username).apply();
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
    }