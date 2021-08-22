package com.vinod.lib.productconsumer_threading;



public class ThreadTesting {
    public static void main(String arg [])
    {
//        System.out.println("from main");
//        ProductQueue queue = new ProductQueue();
//        Producer th1 = new Producer(queue);
//        Consumer th2 = new Consumer(queue);
//        th1.start();
//        th2.start();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(new B(10).index+"" );

    }

    static class A{
        public int index = 1 ;
    }
    static class B extends A {
        B(int index ){
            index = index;
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
