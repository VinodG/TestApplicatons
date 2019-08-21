package com.taksycraft.testapplicatons.camera;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.taksycraft.testapplicatons.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private  String TAG =ImagePickerActivity.class.getSimpleName() ;
    private  int REQUEST_GALLERY =101 ;// gallery
    private  int REQUEST_IMAGE_CAPTURE = 102; //for thumbnail
    static final int REQUEST_TAKE_PHOTO = 103; //save image in sdcard
    private ImageView ivPreview;
    private Button btnClick;
    private Bitmap bitmap;
    private Button btnCameraClick;
    private String currentPhotoPath;
    private File capturedFile;
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ivPreview=(ImageView)findViewById(R.id.ivPreview);
        btnClick=(Button)findViewById(R.id.btnClick);
        btnCameraClick=(Button)findViewById(R.id.btnCameraClick);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading.....");
        btnClick.setOnClickListener(this);
        btnCameraClick.setOnClickListener(this);
        checkCameraPermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnClick:
                pickAnImage(); //picks an image from gallery
                break;
            case R.id.btnCameraClick:
//                openCamera(); //picks an image from camera
                takePictureIntent();
                break;
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickAnImage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        InputStream stream = null;
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            try{
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                ivPreview .setImageBitmap(bitmap);

            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            try{
                if (bitmap != null) {
                    bitmap.recycle();
                }
                makeIconAndBindToView(data);

            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }else if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){

            showLoader("Uploading Image...");
            try{
                if (bitmap != null) {
                    bitmap.recycle();
                }
                //rotate image
                ExifInterface ei = new ExifInterface(capturedFile.getPath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                bitmap = BitmapFactory.decodeFile(capturedFile.getPath());
                Log.e(TAG, "Before -> " +bitmap.getWidth()+" X "+ bitmap.getHeight());
                int actualWidth = bitmap.getWidth();
                int actualHeight = bitmap.getHeight();
                int  maxDim = Math.max(actualWidth, actualHeight);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                bitmap = Bitmap.createScaledBitmap( rotatedBitmap, (int)(400*(rotatedBitmap.getWidth()/(float)maxDim)) ,(int)(400*(rotatedBitmap.getHeight()/(float)maxDim))/*(400*actualHeight)/maxDim*/,false);
                File file = new File(capturedFile.getAbsolutePath());
                if(file.exists())
                    file.delete();
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bitmapdata = bos.toByteArray();
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                ivPreview.setImageBitmap(bitmap);
                hideLoader();
                Log.e(TAG, "After -> " +bitmap.getWidth()+" X "+ bitmap.getHeight());

            }catch (Exception e)
            {
                hideLoader();
                e.printStackTrace();
            }finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private void showLoader(String str) {
        if(progressDialog!=null && !progressDialog.isShowing())
        {
            progressDialog.setMessage(str+"");
            progressDialog.show();
        }

    }
    private void hideLoader() {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    private void makeIconAndBindToView(Intent data) {
        Bundle extras = data.getExtras();
        bitmap = (Bitmap) extras.get("data");
        ivPreview.setImageBitmap(bitmap);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath()+"/"+imageFileName+".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public   Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public void checkCameraPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }



    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            capturedFile = null;
            try {
                capturedFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (capturedFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        /*defined provider authories in menifesto file*/
                        "com.taksycraft.testapplicatons.fileproviderforcamera",
                        capturedFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
