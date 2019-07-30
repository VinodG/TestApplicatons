package com.vinod.java;

import java.util.Collections;

public class ThreadTesting {
    public static void main(String arg [])
    {
        System.out.println("from main");
        SharedClass sharedClass = new SharedClass();
        SharedClass sharedClass2 = new SharedClass();
        Thread1 th1 = new Thread1(sharedClass);
        Thread2 th2 = new Thread2(sharedClass);
        th1.start();
        th2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        sharedClass.refresh();
//        sharedClass.refresh();
//                MyRunnable sharedClass = new MyRunnable();
//        Thread th1 = new Thread  (sharedClass);
//        Thread  th2 = new Thread (sharedClass);
//        th1.start();
//        th2.start();

    }
    static class MyRunnable implements Runnable
    {

        @Override
        public void run() {
            for (int i = 0 ;i<100;i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("current "+Thread.currentThread().getName()+" "+i );

            }
        }
    }

}
