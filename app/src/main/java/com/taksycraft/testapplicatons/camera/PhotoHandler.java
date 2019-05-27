package com.taksycraft.testapplicatons.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class PhotoHandler implements Camera.PictureCallback {
    private final Context context;

    public PhotoHandler(Context applicationContext) {
        this.context =applicationContext;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        File pictureFileDir =getDir();
        if(!pictureFileDir.exists() && !pictureFileDir.mkdirs())
        {
            toast("can not create directory to save an image ");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_"+date+".jpg";
        String filename = pictureFileDir.getPath() + File.separator + photoFile;
        File pictureFile = new File(filename);
        try {
            FileOutputStream fos =new FileOutputStream(pictureFile);
            fos.write(bytes);
            fos.close();
            toast("New image is saved "+photoFile);
        } catch (Exception   e) {
            e.printStackTrace();
            toast("Image culd not be savged");
        }

    }

    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "CameraAPIDemo");
    }
    private void toast(String msg) {
        Toast.makeText(context, msg+"",Toast.LENGTH_LONG).show();
    }
}
