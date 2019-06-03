package com.taksycraft.testapplicatons.customviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.ImageView;

import com.taksycraft.testapplicatons.common.BitmapUtils;

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
//        setContentView(new MultiTouchView(this));
        setContentView(getRegionView());
    }

    private ZoomAndDragView getRegionView() {
        // Allocate a RegionView.
        final ZoomAndDragView lRegionView = new ZoomAndDragView(this);
        // Add some example items to drag.
        lRegionView.addView(new AnalogClock(this));
        lRegionView.addView(new AnalogClock(this));
        lRegionView.addView(new AnalogClock(this));
        // Assert that we only want to drag Views within the confines of the RegionView.
        lRegionView.setWrapContent(true);
        // Assert that after we've finished scaling a View, we want to stop being able to drag it until a new drag is started.
        lRegionView.setDropOnScale(true);
        // Look at the RegionView.
        return  lRegionView;
    }

    public void onClkPerform(View view) {
//        ivPreview.setImageBitmap(sv.getBitMap());
        BitmapUtils.saveImage(sv.getBitMap());
//        sv.clear();
//        sv.setBrushColor(Color.RED);
//

    }

}
