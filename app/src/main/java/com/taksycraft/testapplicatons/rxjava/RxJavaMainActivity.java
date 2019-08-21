package com.taksycraft.testapplicatons.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class RxJavaMainActivity extends AppCompatActivity {

    private static final String TAG ="RxJavaMainActivity" ;
    private Observable observable;
    private Observer observer;
    private DisposableObserver<String>  disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_main);
        observable = getObservable();
//        Observer observer = getCommonObserver();
        observer = getDisposableObserver();
//        observable.subscribe(observer);
        disposable = (DisposableObserver<String>) observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+"-- END");

    }

    private Observer getDisposableObserver() {
        return  new DisposableObserver<String>() {
            @Override
            public void onNext(String o) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+"-- "+o);
                finish(); //to close activity
            }
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
            }
            @Override
            public void onComplete() {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
            }
        } ;
    }

    private Observer getCommonObserver() {
        return new Observer<Integer>() {
            public String TAG = "Observer";

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
            }

            @Override
            public void onNext(Integer s) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "--> " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());

            }

            @Override
            public void onComplete() {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());

            }
        };
    }

    private Observable getObservable() {
//        return Observable.just("A", "B", "C", "D", "E", "F");
//        return Observable.fromArray(new String[]{"A", "B", "C", "D", "E", "F"});
//        return  Observable.interval(1, TimeUnit.SECONDS);//This will print values from 0 after every second.
//    return Observable.range(2, 5);// it will print values from 2 to 6.
//    return Observable.range(2, 5)
//            .repeat(2);// it will print values from 2 to 6.;
        return    ClientBus.getObservable();
    }

    public void onClkDispose(View view) {
        startActivity(new Intent(RxJavaMainActivity.this,RxNextActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         disposable .dispose();
    }
}
