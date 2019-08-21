package com.taksycraft.testapplicatons.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.taksycraft.testapplicatons.R;


public class MyBottomSheetFragment extends BottomSheetDialogFragment {
    public static  String TAG = "MyBottomSheetFragment";
    private TextView tvClose;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e(TAG, "onCreateDialog");
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(  R.layout.bottom_frag,null);
        Log.e(TAG, "onCreateDialog");
        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainFragmentActivity.setWindowParams(getDialog().getWindow());
    }
}
