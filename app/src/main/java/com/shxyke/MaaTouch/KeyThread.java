package com.shxyke.MaaTouch;

public class KeyThread extends Thread {
    private boolean isRunning = true;
    private final Controller controller;
    private int keyCode;
    private int repeat = 0;
    public KeyThread(Controller controller,int keyCode) {
        this.controller = controller;
        this.keyCode = keyCode;
    }
    @Override
    public void run() {
        do {
            controller.injectKeyDown(keyCode,repeat++,0);
            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(this.isRunning);
        controller.injectKeyUp(keyCode,0,0);
    }
    public void stopThread() {
        this.isRunning = false;
    }
}
