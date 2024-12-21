package com.thicuoiky124.lttd03.nhom07.Camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.albumia.Fragments.ThemthongtinanhActivity;
import com.example.albumia.R;

public class them_anh extends Fragment {

    private Button add_btn;
    private ImageButton close_btn;
    private ImageView capturedImageView;


    private Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_anh, container, false);

        ImageView imageView = view.findViewById(R.id.captured_image_view);
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

        close_btn = view.findViewById(R.id.them_anh_closeBtn);
        add_btn = view.findViewById(R.id.them_anh_button);

        close_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(4);
//            }
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(7);
                String imagePath;
                Bundle args = getArguments();
                imagePath = args.getString("imagePath");
                args.putString("imagePath", imagePath);
                Log.d("ERROR view", "Navigating with imagePath: " + imagePath); // Debug log
                Fragment resultFragment = new ThemthongtinanhActivity();
                resultFragment.setArguments(args);

//        ((MainActivity) requireActivity()).customViewPager.setCurrentItem(4);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_capture_camera, resultFragment)
                        .addToBackStack(null)
                        .commit();
            }

        });
        return view;
    }
}
