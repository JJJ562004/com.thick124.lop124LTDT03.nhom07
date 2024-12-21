package com.thicuoiky124.lttd03.nhom07.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.albumia.MainActivity;
import com.example.albumia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class ThemthongtinanhActivity extends Fragment {
    private Button dang_anh_btn;
    private Button return_btn;
    private DatabaseReference mDatabase;
    private EditText addInfoTextView;

    // Create a Cloud Storage reference from the app
    StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

    private Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_themthongtinanh, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addInfoTextView = view.findViewById(R.id.themthongtinanh_editText);
        ImageView imageView = view.findViewById(R.id.themthongtinanh_imageView4);
//        String imagePath = getArguments().getString("imagePath");
        String imagePath;
        Bundle args = getArguments();
        if (args != null && args.getString("imagePath") != null) {
            imagePath = args.getString("imagePath");
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap = rotateBitmap(bitmap, 90);
            imageView.setImageBitmap(bitmap);
        } else {
            Log.e("NULL ImagePath", "Arguments or imagePath is null.");
        }

        dang_anh_btn = view.findViewById(R.id.themthongtinanh_button19);

        return_btn = view.findViewById(R.id.themthongtinanh_button18);

        return_btn.setOnClickListener(new View.OnClickListener() {
            // return trang chu
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchToFragment(4);
            }
        });

        dang_anh_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (addInfoTextView.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a name for the image", Toast.LENGTH_SHORT).show();
                    return;
                }
                String imagePath;
                Bundle args_1 = getArguments();
                if (args_1 != null && args_1.getString("imagePath") != null) {
                    imagePath = args_1.getString("imagePath");

                    // Convert file path to URI
                    File file = new File(imagePath);
                    Uri fileUri = FileProvider.getUriForFile(
                            requireContext(),
                            requireContext().getPackageName() + ".fileprovider",
                            file);
                    // Retrieve the id
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String userName = sharedPreferences.getString("UserName", null); // Returns null if not found

                    // Upload file
                    StorageReference fileRef = storageRef.child(addInfoTextView.getText() + ".jpg");
                    if (userName != null) {
                        StorageReference userFileRef = storageRef.child(userName + "/" +addInfoTextView.getText() + ".jpg");
                        userFileRef.putFile(fileUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(requireContext(), "Upload successful!", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).switchToFragment(2);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }
                    else{
                        Toast.makeText(getContext(), "Can't find the user ID, automatically added image to uploads/ folder", Toast.LENGTH_SHORT).show();
                    }

                    fileRef.putFile(fileUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                Toast.makeText(requireContext(), "Upload successful!", Toast.LENGTH_SHORT).show();
                                ((MainActivity) getActivity()).switchToFragment(2);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Log.e("NULL ImagePath", "Arguments or imagePath is null.");
                }

            }
        });
        return view;
    }
}