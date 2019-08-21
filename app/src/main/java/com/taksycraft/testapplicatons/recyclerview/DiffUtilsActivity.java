package com.taksycraft.testapplicatons.recyclerview;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.NameDO;

import java.util.Vector;
//https://www.journaldev.com/20873/android-recyclerview-diffutil
//check below link to merge
//http://blogs.quovantis.com/how-to-use-diffutil-with-recyclerview-adapter-in-android/
public class DiffUtilsActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private Button btnUpdate;
    private Button btnAdd;
    private EditText etName;
    private EditText etId;
    private Vector<NameDO> vec ;
    private CommonDiffAdapter adapter;
    private String TAG = DiffUtilsActivity.class.getSimpleName();
    private MergeDiffAdapter adapterNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_utils);
        rvList = (RecyclerView)findViewById(R.id.rvList);
        btnUpdate= (Button)findViewById(R.id.btnUpdate);
        btnAdd= (Button)findViewById(R.id.btnAdd);
        etId= (EditText)findViewById(R.id.etId);
        etName= (EditText)findViewById(R.id.etName);
        vec = new Vector<NameDO>();
        clearText();
        setList();
        setListeners ();
    }

    private void setListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vec.clear();
                for(int i = 0 ;i<65;i++)
                {
                    NameDO obj =new NameDO();
                    obj.id = (i+1)+"";
                    obj.name  = (i+1)+" old";
                    vec.add(obj);
                }
                adapterNew.submitList(vec);
                clearText();
//                addToList(vec);
//                for(int i = 0 ;i<65;i++)
//                {
//                    NameDO obj =new NameDO();
//                    obj.id = (i+1)+"";
//                    obj.name  = (i+1)+" old";
//                    vec.add(obj);
//                }
//                adapter.refresh(vec);
//                clearText();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateData();
                insertNewData();


            }
        });
    }

    private void updateData() {
        if(isValid()) {
            int position = Integer.parseInt(etId.getText().toString()) - 1;
            vec.removeElementAt(position);
            NameDO obj = new NameDO();
            obj.id = (position + 1) + "";
            obj.name = etName.getText().toString();
            vec.add(position, obj);
            Vector<NameDO> vecNew = new Vector<NameDO>();
            vecNew.addAll(vec);
            adapter.updateList(vecNew);
//                    adapterNew.submitList(vecNew);
            clearText();
        }
    }
    private void insertNewData() {
        if(isValid()) {
//            int position = Integer.parseInt(etId.getText().toString()) - 1;
            int position = vec.size();
            NameDO obj = new NameDO();
            obj.id = (position ) + "";
            obj.name = etName.getText().toString();
            vec.add(obj);
            Vector<NameDO> vecNew = new Vector<NameDO>();
            vecNew = (Vector<NameDO>)vec.clone();
            adapterNew.submitList(vecNew);
            clearText();
        }
    }

    private void addToList(Vector<NameDO> vec) {
        if(isValid())
        {
            NameDO obj =  new NameDO();
            obj.id = etId.getText().toString();
            obj.name = etName.getText().toString();
            vec.add(obj);
        }
        adapter.refresh(vec);
    }

    private void setList() {
        adapter  = new CommonDiffAdapter(vec, new CommonDiffAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonDiffAdapter.CommonHolder holder, int position) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" -- "+position );
                NameDO obj = (NameDO) adapter.vec.get(position);
                holder.getTextView().setText(obj.id+" "+obj.name);
            }

            @Override
            public void onItemClick(Object object, int position) {
            }
        });
        adapterNew = new MergeDiffAdapter();
        rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
//        rvList.setAdapter(adapter);
        rvList.setAdapter(adapterNew);
        adapterNew.submitList(null);
    }

    private void clearText() {
        etId.setText("");
        etName.setText("");
    }
    private boolean isValid(){
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        if(TextUtils.isEmpty(id.trim()))
        {
            toast("Id is empty");
            return false;

        }else if(TextUtils.isEmpty(name.trim()))
        {
            toast("Name is empty");
            return false;
        }
        return true;

    }

    private void toast(String str) {
        try {
            Toast.makeText(DiffUtilsActivity.this,str, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
