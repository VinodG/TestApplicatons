package com.vinod.java.productconsumer_threading;

import com.vinod.java.SharedClass;
import com.vinod.java.Thread1;
import com.vinod.java.Thread2;

public class ThreadTesting {
    public static void main(String arg [])
    {
        System.out.println("from main");
        ProductQueue queue = new ProductQueue();
        Producer th1 = new Producer(queue);
        Consumer th2 = new Consumer(queue);
        th1.start();
        th2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    static class MyRunnable implements Runnable
    {

        @Override
        public void run() {
//            for (int i = 0 ;i<20;i++)
//            {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("current "+Thread.currentThread().getName()+" "+i );
//            }
        }
    }

}
