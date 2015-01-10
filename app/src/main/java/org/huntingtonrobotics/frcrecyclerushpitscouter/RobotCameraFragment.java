package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by 2015H_000 on 1/8/2015.
 */
public class RobotCameraFragment extends Fragment{
    private static final String TAG = "RobotCameraFragment";

    public static final String EXTRA_PHOTO_FILENAME =
        "org.huntingtonrobotics.frcrecyclerushpitscouter.photo_filename";

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    //implement callbacks for takepicture

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //Display the progress indicater
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback(){
        public void onPictureTaken(byte[] data, Camera camera){
            //create a file name
            String filename = UUID.randomUUID().toString() + ".jpg";
            //save the jpeg data to disk
            FileOutputStream os = null;
            boolean success = true;

            try{
                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e){
                Log.e(TAG, "ERROR writing to file " + filename, e);
                success = false;
            } finally {
                try{
                    if (os != null)
                        os.close();
                }catch (Exception e){
                    Log.e(TAG, "ERROR closing file "+filename, e);
                    success = false;
                }
            }

            //set the photo filename on the result intent

            if(success){
                Log.i(TAG, "JPEG saved at " + filename);
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK, i);
            }else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();

        }
    };

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_robot_camera, parent, false);

        mProgressContainer = v.findViewById(R.id.robot_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        Button takePictureButton = (Button)v.findViewById(R.id.robot_camera_takePictureBtn);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement takepicture on button click
                if (mCamera!=null){
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
            }
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.robot_camera_surfaceView);

        SurfaceHolder holder = mSurfaceView.getHolder();
        //setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated but are requried for Camera to work on pre 3.0 devices
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //implement surfaceholder.callback
        holder.addCallback(new SurfaceHolder.Callback(){
            public void surfaceCreated(SurfaceHolder holder){
                //tell camera to use this surface as its preview area
                try{
                    if (mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch (IOException exception){
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder){
                //we can no longer display on this surface so stop the preview
                if (mCamera != null){
                    mCamera.stopPreview();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
                if (mCamera == null) return;

                //The surface has changed size, update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                //set the picture size
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);

                try{
                    mCamera.startPreview();
                }catch (Exception e){
                    Log.e(TAG, "Could not start preview",e);
                    mCamera.release();
                    mCamera = null;
                }
            }
        });

        return v;
    }

    //open camera on resume
    @TargetApi(9)
    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = Camera.open(0);
        }else{
            mCamera = Camera.open();
        }
    }

    //release camera on pause
    @Override
    public void onPause(){
        super.onPause();

        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    // A simple algorithm to get the largest size available.
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width*bestSize.height;
        for(Camera.Size s : sizes){
            int area = s.width*s.height;
            if (area > largestArea){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;

    }
}
