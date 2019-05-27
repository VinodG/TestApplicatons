package com.taksycraft.testapplicatons.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.taksycraft.testapplicatons.R;

public class CameraActivity extends AppCompatActivity {

    private int cameraId;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            toast("No camera on this device");
        else{
            cameraId = findFrontCameraId();
            if(cameraId <0)
                toast("No front camera is found");
            else
                camera =Camera.open(cameraId);
        }

    }

    private int findFrontCameraId() {
        int cameraId =-1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for(int i = 0;i<numberOfCameras;i++)
        {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                cameraId=i;
                break;
            }
        }
        return  cameraId;
    }
    public void captureImage(View view) {
        SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
//        camera.setPreviewTexture(st);
        camera.startPreview();
        camera.takePicture(null, null,
                new PhotoHandler(getApplicationContext()));
    }
    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
    private void toast(String msg) {
        Toast.makeText(this, msg+"",Toast.LENGTH_LONG).show();
    }
}
