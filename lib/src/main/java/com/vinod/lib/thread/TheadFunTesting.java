package com.vinod.lib.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheadFunTesting   {
    static class MyRunnable implements Runnable  {
        @Override
        public void run() {
            for (int i = 1; i <30 ; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" "+i);
            }
            System.out.println(Thread.currentThread().getName()+"  is ended");
        }
    };


    public static void main(String args[])  {
        Thread t1 = new Thread(new MyRunnable(), "First" );
        Thread t2 = new Thread(new MyRunnable(),"Second");
        Thread t3 = new Thread(new MyRunnable(), "Third");
//        executeThreadsInSequence(t1,t2);
//        executeThreadsInParallel(t1,t2);
//        executeThreadsInParallelWithPriority(t1,t2,t3);
        setThreadPoolExecutor(t1);
        
        System.out.println("end");
    }

    private static void setThreadPoolExecutor(Thread t1) {
        int NO_OF_THREADS = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(NO_OF_THREADS);
        for (int i = 0; i < NO_OF_THREADS ; i++) {
            t1.setName("Thread "+(i+1));
            executor.submit(new MyRunnable());
        }
        executor.shutdown();
        while (executor.isTerminated());
    }

    private static void executeThreadsInParallelWithPriority( Thread t1, Thread t2,Thread t3) {

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.NORM_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void executeThreadsInParallel(Thread t1, Thread t2) {
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void executeThreadsInSequence(Thread t1, Thread t2) {

        try {
            t1.start();
            t1.join();
            t2.start();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
