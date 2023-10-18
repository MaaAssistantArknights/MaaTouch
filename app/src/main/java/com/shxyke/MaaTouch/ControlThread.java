package com.shxyke.MaaTouch;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControlThread {

    private final LinkedBlockingQueue<Queue<ControlMessage>> queue;
    private final Controller controller;

    public ControlThread(LinkedBlockingQueue<Queue<ControlMessage>> queue, Controller controller) {
        this.queue = queue;
        this.controller = controller;
    }

    public void handleMessage(ControlMessage msg) {
        switch (msg.getType()) {
            case ControlMessage.TYPE_EVENT_TOUCH_RESET:
                controller.resetAll();
                break;
            case ControlMessage.TYPE_EVENT_TOUCH_DOWN:
                controller.injectTouchDown(msg.getPointerId(), msg.getPoint(), msg.getPressure());
                break;
            case ControlMessage.TYPE_EVENT_TOUCH_MOVE:
                controller.injectTouchMove(msg.getPointerId(), msg.getPoint(), msg.getPressure());
                break;
            case ControlMessage.TYPE_EVENT_TOUCH_UP:
                controller.injectTouchUp(msg.getPointerId());
                break;
            case ControlMessage.TYPE_EVENT_KEY_DOWN:
                controller.injectKeyDown(msg.getKeycode(), msg.getRepeat(), msg.getMetaState());
                break;
            case ControlMessage.TYPE_EVENT_KEY_UP:
                controller.injectKeyUp(msg.getKeycode(), msg.getRepeat(), msg.getMetaState());
                break;
            case ControlMessage.TYPE_EVENT_WAIT:
                try {
                    long sleepTime = msg.getTargetTimestamp() - System.currentTimeMillis();
                    Thread.sleep(sleepTime > 0 ? sleepTime : 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void run() {
        while (true) {
            try {
                Queue<ControlMessage> subqueue = queue.take();
                while (!subqueue.isEmpty()) {
                    handleMessage(subqueue.poll());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
