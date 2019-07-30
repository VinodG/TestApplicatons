package com.vinod.java.alternative_threads_sequence;

import java.util.Vector;

public class ProductQueue {
    long thread1 = System.currentTimeMillis();
    long thread2 = System.currentTimeMillis();
    long MAX_TIME_SLOT = 2*1000;
    boolean isThread1 = true;
    public synchronized  void thread1( )
    {
        long current  = System.currentTimeMillis();
//        if(current - thread1 < MAX_TIME_SLOT )
//        {
//            System.out.println(Thread.currentThread().getName()+ " is in wait "+ (current - thread1));
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }else
        if(isThread1)
        {
            try {
                wait(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
        {
            isThread1 = !isThread1 ;
            notify();
            thread1 = System.currentTimeMillis();
        }

    }
    public synchronized  void thread2( )
    {
        long current  = System.currentTimeMillis();
//        if(current - thread2 < MAX_TIME_SLOT )
//        {
//            System.out.println(Thread.currentThread().getName()+ " is in wait "+ (current - thread2));
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }else
        if(!isThread1)
        {
            try {
                wait(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
        {
            notify();
            isThread1 = !isThread1 ;
            thread2 = System.currentTimeMillis();

        }

    }

}
