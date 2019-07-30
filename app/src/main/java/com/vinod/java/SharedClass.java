package com.vinod.java;

public class SharedClass {


    public   synchronized   void test()
    {
        System.out.println("before wait");
        try {
            wait( );
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after wait");
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
    public  void test2()
    {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("current " + Thread.currentThread().getName() + " " + i);
        }
    }
    public       void test3()
    {
        for (int i = 0 ;i<10;i++)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("current "+Thread.currentThread().getName()+" "+i );
        }
    }
    public    synchronized    void refresh() {
        notify();
    }
}
