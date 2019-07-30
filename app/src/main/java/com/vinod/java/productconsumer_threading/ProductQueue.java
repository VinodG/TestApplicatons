package com.vinod.java.productconsumer_threading;

import com.taksycraft.testapplicatons.common.CommonAdapter;

import java.util.Vector;

public class ProductQueue {
    Vector<String> queue = new Vector<String>();
    int LOAD_SIZE =50 ;
    public synchronized  void produce(String productName )
    {
        if(queue.size()>LOAD_SIZE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
        {
            System.out.println(productName+" is added ");
            queue.add(productName);
            notify();
        }

    }
    public synchronized  void consume (  )
    {
        if(queue.size()== 0)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
        {
            System.out.println(queue.remove(0)+" is removed ");
            notify();
        }

    }
}
