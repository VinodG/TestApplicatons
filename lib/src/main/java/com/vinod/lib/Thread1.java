package com.vinod.lib;

public class Thread1 extends  Thread {
    private   SharedClass sharedClass;

    public Thread1(SharedClass sharedClass) {
        this.sharedClass = sharedClass;
    }

    @Override
    public void run() {
        super.run();

        sharedClass.test();
    }
}
