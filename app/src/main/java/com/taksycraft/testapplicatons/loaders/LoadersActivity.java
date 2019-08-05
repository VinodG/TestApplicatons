package com.taksycraft.testapplicatons.loaders;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

public class LoadersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> /*implements LoaderManager.LoaderCallbacks<Integer>*/ {

    private  int LOADER_ID = 10 ;
    private Bundle TASK_BUNDLE = null;
    private String TAG = getClass().getSimpleName();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaders);
        tv =(TextView)findViewById(R.id.tv);
        LoaderManager loaderManager = getLoaderManager();
        android.content.Loader<Integer> loader = loaderManager .getLoader(LOADER_ID);
        if(loader == null ) {
            loaderManager.initLoader(LOADER_ID, TASK_BUNDLE, this);
        }
        else
            loaderManager.restartLoader(LOADER_ID, TASK_BUNDLE, this ) ;

    }

    @Override
    public  Loader<Integer> onCreateLoader(int id, Bundle args) {
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());
        return new RunnableAsyncTaskLoader<Integer> (LoadersActivity.this )  ;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Integer> loader, Integer data) {
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());

    }

    @Override
    public void onLoaderReset(android.content.Loader<Integer> loader) {
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG , new Exception().getStackTrace()[0].getMethodName());
    }
}

