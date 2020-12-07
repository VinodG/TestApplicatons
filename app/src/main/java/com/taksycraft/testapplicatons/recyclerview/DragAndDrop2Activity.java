package com.taksycraft.testapplicatons.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.taksycraft.testapplicatons.R;

import java.util.ArrayList;
import java.util.List;

public class DragAndDrop2Activity extends AppCompatActivity {

    private RecyclerView rvUp,rvDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);

        rvUp = (RecyclerView) findViewById(R.id.rvUp);
        rvDown = (RecyclerView) findViewById(R.id.rvDown);
        List<ItemDO> list = getData();
    }
    private List<ItemDO> getData(){
        List<ItemDO> list = new ArrayList<>();
        ItemDO obj;
        for (int i = 1; i<=20; i++){
            obj = new ItemDO();
            obj.name ="ITEM ------ "+(i);
            obj.type =0;
            list.add(obj);
        }
        obj = new ItemDO();
        obj.name ="Drag And Drop";
        obj.type =1;
        list.add(obj);
        for (int i = 11 ;i<=20;i++){
            obj = new ItemDO();
            obj.name ="ITEM ------ "+(i);
            obj.type =0;
            list.add(obj);
        }
        return list;
    }
    class ItemDO{
        String name;
        int type;
    }
}