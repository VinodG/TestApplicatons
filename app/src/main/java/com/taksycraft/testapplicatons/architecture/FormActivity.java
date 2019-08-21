package com.taksycraft.testapplicatons.architecture;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;

import java.util.List;

public class FormActivity extends AppCompatActivity {

    private    String DATABASE_NAME = "MyRoomDatabase.db";
    private EditText etName,etOccupation,etAmount;
    private MyRoomDatabase db;
    private String _TAG =getClass().getSimpleName();
    private RecyclerView rv;
    private RecordAdapter adapter;
    private List<Record> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        etName = (EditText)findViewById(R.id.etName);
        etOccupation= (EditText)findViewById(R.id.etOccupation);
        etAmount= (EditText)findViewById(R.id.etAmount);
        db = MyRoomDatabase.getInstance(getApplicationContext());
        rv =(RecyclerView)findViewById(R.id.rv);
        adapter = new RecordAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        rv.setAdapter(adapter);
        db.getRecordDAO().getRecord().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                adapter.refresh(records);
            }
        });

    }

    public void onClkSubmit(View view) {
        Record obj = new Record();
        obj.name = etName.getText().toString();
        obj.occupation = etOccupation.getText().toString();
        obj.amount = etAmount.getText().toString();
        new InsertRecord(obj ).execute();
        clearFields();
    }

    private void clearFields() {
        etName.setText("");
        etOccupation.setText("");
        etAmount.setText("");
    }

    public void onClkRead(View view) {
//        new ReadRecord().execute();
    }
    class InsertRecord extends AsyncTask<Record,Void,Void>
    {

        private   Record record;

        public InsertRecord(Record record) {
            this.record =record;
        }

        @Override
        protected Void doInBackground(Record... records) {
            Log.e(_TAG,"doInBackground for insertion");
            db.getRecordDAO().insertRecord(record);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clearFields();
        }
    }
    class ReadRecord extends AsyncTask<Record,Void,Void>
    {
        @Override
        protected Void doInBackground(Record... records) {
            Log.e(_TAG,"doInBackground for insertion");
//            list = db.getRecordDAO().getRecord();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.refresh(list);
        }
    }
}
