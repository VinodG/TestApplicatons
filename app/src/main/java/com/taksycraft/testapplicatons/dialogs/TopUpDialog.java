package com.taksycraft.testapplicatons.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taksycraft.testapplicatons.R;

public class TopUpDialog extends Dialog {
    public TopUpDialog(@NonNull Context context) {
        super(context);
    }

    public TopUpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TopUpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_top_to_bottom);
        setWindowSettings();

    }

    private void setWindowSettings() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
    }
}
