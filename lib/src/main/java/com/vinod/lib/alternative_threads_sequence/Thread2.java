package com.vinod.lib.alternative_threads_sequence;

public class Thread2 extends  Thread {
    private ProductQueue queue;

    public Thread2(ProductQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0 ;i<1000;i++)
        {
            System.out.println(Thread.currentThread().getName()+" "+i);
            queue.thread2( );
        }

    }
}
