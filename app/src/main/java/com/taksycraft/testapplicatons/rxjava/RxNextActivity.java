package com.taksycraft.testapplicatons.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

import io.reactivex.subjects.BehaviorSubject;

public class RxNextActivity extends AppCompatActivity {

    private TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_next);

    }
    public void onClkDispose(View view) {
        ((BehaviorSubject<String>)ClientBus.getObservable()).onNext("asd");

    }
}
