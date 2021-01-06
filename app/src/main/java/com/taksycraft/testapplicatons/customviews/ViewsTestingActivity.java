package com.taksycraft.testapplicatons.customviews;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.BitmapUtils;
import com.taksycraft.testapplicatons.databinding.ActivityViewsTestingBinding;

public class ViewsTestingActivity extends AppCompatActivity {
    private static final String TAG = "ViewsTestingActivity";
    private SignatureView sv;
    private ImageView ivPreview;
    private ActivityViewsTestingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_views_testing);
        setAutoCompleteTextView();
//        sv = (SignatureView)findViewById(R.id.sv);
//        ivPreview= (ImageView)findViewById(R.id.ivPreview);
//        ivPreview.setVisibility(View.GONE);
//        sv.setBrushWidth(15);
//        setContentView(new MultiTouchView(this));
//        setContentView(getRegionView());
    }
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain","Abc","ABC","ABCD","ABCE","BC","BD"
    };

    private void setAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        binding.actvCountry.setAdapter(adapter);
        binding.actvCountry.setThreshold(1);
        binding.actvCountry.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemClick: " +i +" "+binding.actvCountry.getText().toString() );
                binding.btnPerform.setText(binding.btnPerform.getText().toString()+","+binding.actvCountry.getText().toString());
                binding.actvCountry.setText("");
            }

        } );

//        binding.actvCountry.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

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
