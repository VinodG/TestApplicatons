package com.taksycraft.testapplicatons.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


class RunnableAsyncTaskLoader< Integer> extends AsyncTaskLoader<java.lang.Integer> {
    String TAG = RunnableAsyncTaskLoader.class.getSimpleName();

    public RunnableAsyncTaskLoader(Context applicationContext) {
        super(applicationContext);
        onContentChanged();
    }
    private java.lang.Integer cachedResult;


    @Override
    protected void onStartLoading() {
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());

//                if(cachedResult ==null) {
        if(takeContentChanged()) {
            forceLoad();
            Log.e(TAG , "forceLoad");
        }
//        else {
//            Log.e(TAG , "not forceLoad");
//            deliverResult(cachedResult);
//        }
    }

    @Override
    public java.lang.Integer loadInBackground() {
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());
        for (int i = 0 ;i<10;i++)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG , " value "+i);
        }
        return 1;
    }

    @Override
    public void deliverResult(java.lang.Integer data) {
        cachedResult = data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

    }

}
