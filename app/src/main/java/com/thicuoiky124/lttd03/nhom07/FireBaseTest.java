package com.thicuoiky124.lttd03.nhom07;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseTest
{

    // Firebase database instance
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    // Constructor
    public FireBaseTest() {
        // Initialize Firebase instance and reference
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users"); // Reference to the 'users' node
    }

    // User data model class
    public static class UserTest {
        public String name;
        public String email;

        public UserTest() {
            // Default constructor required for Firebase
        }

        public UserTest(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }



    // Method to add a user to Firebase Realtime Database
    public void addUser(String name, String email) {
        String userId = myRef.push().getKey(); // Generate a unique ID for the user
        if (userId != null) {
            UserTest user = new UserTest(name, email);
            myRef.child(userId).setValue(user); // Add the user to the database
        }
    }

    // Method to read users (optional for testing)
    public void readUsers() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserTest user = snapshot.getValue(UserTest.class);
                    if (user != null) {
                        System.out.println("Name: " + user.name + ", Email: " + user.email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Failed to read value: " + error.toException());
            }
        });
    }
}
