package com.vinod.lib.productconsumer_threading;

public class Consumer extends  Thread {
    private   ProductQueue queue;

    public Consumer(ProductQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0 ;i<1000;i++)
        {
//            System.out.println(Thread.currentThread().getName()+" "+i);
            queue.consume();
        }

    }
}
