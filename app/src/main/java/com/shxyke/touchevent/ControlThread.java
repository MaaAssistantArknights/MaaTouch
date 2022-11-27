package com.shxyke.touchevent;

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
            case ControlMessage.TYPE_EVENT_RESET:
                controller.resetAll();
                break;
            case ControlMessage.TYPE_EVENT_DOWN:
                controller.injectTouchDown(msg.getPointerId(), msg.getPoint(), msg.getPressure());
                break;
            case ControlMessage.TYPE_EVENT_MOVE:
                controller.injectTouchMove(msg.getPointerId(), msg.getPoint(), msg.getPressure());
                break;
            case ControlMessage.TYPE_EVENT_UP:
                controller.injectTouchUp(msg.getPointerId());
                break;
            case ControlMessage.TYPE_EVENT_WAIT:
                try {
                    Thread.sleep(msg.getMillis());
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
