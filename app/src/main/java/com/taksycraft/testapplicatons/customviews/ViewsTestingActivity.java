package com.taksycraft.testapplicatons.customviews;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ViewsTestingActivity extends AppCompatActivity {

    private SignatureView sv;
    private ImageView ivPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_views_testing);
//        sv = (SignatureView)findViewById(R.id.sv);
//        ivPreview= (ImageView)findViewById(R.id.ivPreview);
//        ivPreview.setVisibility(View.GONE);
//        sv.setBrushWidth(15);
        setContentView(new ImageViewWithZoom(this));
    }

    public void onClkPerform(View view) {
//        ivPreview.setImageBitmap(sv.getBitMap());
        BitmapUtils.saveImage(sv.getBitMap());
//        sv.clear();
//        sv.setBrushColor(Color.RED);
//

    }

}
