package com.shxyke.MaaTouch;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControlThread {

    private final LinkedBlockingQueue<Queue<ControlMessage>> queue;
    private final Controller controller;
    private final HashMap<Integer,KeyThread> KeyThreads;

    public ControlThread(LinkedBlockingQueue<Queue<ControlMessage>> queue, Controller controller) {
        this.queue = queue;
        this.controller = controller;
        KeyThreads = new HashMap<>();
    }
    public void KeyDown(int key)
    {
        if(!KeyThreads.containsKey(key))
        {
            KeyThread thread = new KeyThread(this.controller,key);
            thread.start();
            KeyThreads.put(key,thread);
        }
    }
    public void KeyUp(int key)
    {
        if(KeyThreads.containsKey(key))
        {
            KeyThread thread = KeyThreads.get(key);
            thread.stopThread();
            KeyThreads.remove(key);
        }
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
                KeyDown(msg.getKeycode());
                break;
            case ControlMessage.TYPE_EVENT_KEY_UP:
                KeyUp(msg.getKeycode());
                break;
            case ControlMessage.TYPE_EVENT_KEY:
                controller.pressReleaseKeycode(msg.getKeycode());
                break;
            case ControlMessage.TYPE_EVENT_TEXT:
                controller.setClipboard(msg.getText());
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
