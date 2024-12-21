package com.thicuoiky124.lttd03.nhom07.Camera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback, ScaleGestureDetector.OnScaleGestureListener {

    Camera camera;
    SurfaceHolder holder;
    private ScaleGestureDetector scaleGestureDetector;
    private float currentZoomLevel = 0; // current zoom level
    private float maxZoomLevel; // max zoom level

    public ShowCamera(Context context, Camera camera, FrameLayout frameLayout) {
        super(context);
        this.camera = camera;
        this.holder = getHolder();
        holder.addCallback(this);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        frameLayout.addView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true; // Consume the event
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Camera.Parameters params = camera.getParameters();

        if (params.isZoomSupported()) {
            maxZoomLevel = params.getMaxZoom();
        }

        List<Camera.Size> supportedSizes = params.getSupportedPreviewSizes();
        Camera.Size selectedSize = supportedSizes.get(0); // Select the first supported size (or choose based on your needs)

        // Set the selected size
        params.setPreviewSize(selectedSize.width, selectedSize.height);
        // Change camera orientation
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }
        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        camera.setParameters(params);
        try{
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (holder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // Ignore: tried to stop a non-existent preview
        }

        // Set camera parameters again if needed
        Camera.Parameters params = camera.getParameters();
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean onScale(@NonNull ScaleGestureDetector detector) {
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            if (params.isZoomSupported()) {
                float scaleFactor = detector.getScaleFactor();

                int newZoomLevel = (int) (currentZoomLevel + (scaleFactor - 1) * maxZoomLevel);

                if (newZoomLevel >= 0 && newZoomLevel <= maxZoomLevel) {
                    currentZoomLevel = newZoomLevel;
                    params.setZoom((int) currentZoomLevel);
                    camera.setParameters(params);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(@NonNull ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(@NonNull ScaleGestureDetector scaleGestureDetector) {

    }
}
