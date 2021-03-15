package com.vinod.lib.productconsumer_threading;


public class Producer extends  Thread {
    private   ProductQueue queue;

    public Producer(ProductQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0 ;i<1000;i++)
        {
//            System.out.println(Thread.currentThread().getName()+" "+i);
            queue.produce("product "+i);
        }

    }
}
