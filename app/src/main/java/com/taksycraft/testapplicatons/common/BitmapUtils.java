package com.taksycraft.testapplicatons.common;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtils {
    public static boolean saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();
        // the directory where the signature will be saved
        File myDir = new File(root + "/signature");
        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        // set the file name of your choice
        String fname = "signature.png";
        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
