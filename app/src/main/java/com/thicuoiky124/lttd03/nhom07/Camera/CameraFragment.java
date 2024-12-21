package com.thicuoiky124.lttd03.nhom07.Camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.albumia.MainActivity;
import com.example.albumia.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraXApp";
    private static final int CAMERA_REQUEST_CODE = 100;

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
    private ExecutorService cameraExecutor;
    private ImageButton returnButton;

    private File photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_capture, container, false);
        previewView = view.findViewById(R.id.imageScreen);
        ImageButton switchCameraButton = view.findViewById(R.id.switch_camera_btn);
        ImageButton takePhotoButton = view.findViewById(R.id.take_photo_button);
        ImageButton backButton = view.findViewById(R.id.backButton);
//        returnButton = view.findViewById(R.id.backButton);
//        returnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        cameraExecutor = Executors.newSingleThreadExecutor();

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            setupCamera();
        }

        switchCameraButton.setOnClickListener(v -> {
            cameraSelector = (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    ? CameraSelector.DEFAULT_FRONT_CAMERA
                    : CameraSelector.DEFAULT_BACK_CAMERA;
            setupCamera();
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(2);
            }
        });

        takePhotoButton.setOnClickListener(v -> captureImage());

        return view;
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void setupCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Initialize Preview use case
                Preview preview = new Preview.Builder()
                        .build();

                // Initialize ImageCapture use case
                imageCapture = new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation()) // Automatically adjust rotation
                        .build();

                // Unbind all use cases before rebinding
                cameraProvider.unbindAll();

                // Bind Preview and ImageCapture use cases to the lifecycle
                cameraProvider.bindToLifecycle(
                        this, // LifecycleOwner
                        cameraSelector, // Camera selector (front/back)
                        preview, // Preview use case
                        imageCapture // ImageCapture use case
                );

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Failed to bind camera use cases", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void captureImage() {
        if (imageCapture == null) {
            Log.e(TAG, "ImageCapture is null.");
            return;
        }

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Log.e(TAG, "Failed to create image file", e);
            return;
        }

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                try {
                    Bitmap correctedBitmap = rotateImageIfRequired(photoFile);

                    // Save the corrected bitmap if needed
                    // Navigate to the result fragment with the correct image path
                    navigateToResultFragment(photoFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(TAG, "Failed to process image orientation", e);
                }
            }


            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "Photo capture failed: " + exception.getMessage());
            }
        });
    }

    public static Bitmap rotateImageIfRequired(File photoFile) throws IOException {
        ExifInterface exif = new ExifInterface(photoFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "JPEG_" + timestamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    private void navigateToResultFragment(String imagePath) {
        Bundle args = new Bundle();
        args.putString("imagePath", imagePath);

        Log.d(TAG, "Navigating with imagePath: " + imagePath); // Debug log

        Fragment resultFragment = new them_anh();
        resultFragment.setArguments(args);

//        ((MainActivity) requireActivity()).customViewPager.setCurrentItem(4);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_capture_camera, resultFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupCamera();
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
