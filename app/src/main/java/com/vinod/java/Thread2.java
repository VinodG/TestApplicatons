package com.vinod.java;

public class Thread2 extends  Thread {
    private   SharedClass sharedClass;

    public Thread2(SharedClass sharedClass) {
        this.sharedClass = sharedClass;
    }

    @Override
    public void run() {
        super.run();
        sharedClass.test();
    }
}
