package com.mrhui.automatic.interceptor;

public class Test extends Thread{
    private int i = 0;
    public Test(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Hello"+i);
    }
}
