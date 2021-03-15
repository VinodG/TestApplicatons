package com.vinod.lib.alternative_threads_sequence;

public class ThreadTesting {
    public static void main(String arg [])
    {
        System.out.println("from main");
        ProductQueue queue = new ProductQueue();
        Thread1 th1 = new Thread1(queue);
        Thread2 th2 = new Thread2(queue);
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
