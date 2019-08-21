package com.taksycraft.testapplicatons.parsing;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.taksycraft.testapplicatons.R;

import java.util.HashMap;

public class ParsingTestingActivity extends AppCompatActivity {

    String TAG =ParsingTestingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_testing);
        parseString();
    }

    private void parseString() {
        String str1="{ \"id\" : \"3\",  \"name\" : \"vinod\"}";
        String str2="{ \"id\" : \"3\",  \"name\" : \"vinod\"}";

        String str = "{ \"object\" : ["+str1+","+str2+"] ,\"object2\" : [\"+str1+\",\"+str2+\"] }";
        Log.e(TAG, str);
        Object obj = new Gson().fromJson(str, HashMap.class);
        Log.e(TAG, "after finishing ");
    }
    class NameDO
    {
        public String id ="";
        public String name ="";
    }
}