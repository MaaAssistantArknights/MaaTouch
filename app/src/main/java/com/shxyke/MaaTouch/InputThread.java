package com.shxyke.MaaTouch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputThread extends Thread {

    private final BufferedReader stdin;
    private final LinkedBlockingQueue<Queue<ControlMessage>> queue;

    private boolean isRunning = true;

    private static final Pattern DOWN_PATTERN = Pattern.compile("d\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
    private static final Pattern MOVE_PATTERN = Pattern.compile("m\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
    private static final Pattern WAIT_PATTERN = Pattern.compile("w\\s+(\\d+)");
    private static final Pattern UP_PATTERN = Pattern.compile("u\\s+(\\d+)");
    private static final Pattern COMMIT_PATTERN = Pattern.compile("c");
    private static final Pattern RESET_PATTERN = Pattern.compile("r");

    private Queue<ControlMessage> subqueue = new LinkedList<>();

    public InputThread(BufferedReader stdin, LinkedBlockingQueue<Queue<ControlMessage>> queue) {
        this.stdin = stdin;
        this.queue = queue;
    }

    private void parseDown(String s) {
        Matcher m = DOWN_PATTERN.matcher(s);
        if (m.find()) {
            long pointerId = Long.parseLong(m.group(1));
            Point point = new Point(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            float pressure = Integer.parseInt(m.group(4)) / 255;
            while (!subqueue.offer(ControlMessage.createDownEvent(pointerId, point, pressure)));
        }
    }

    private void parseMove(String s) {
        Matcher m = MOVE_PATTERN.matcher(s);
        if (m.find()) {
            long pointerId = Long.parseLong(m.group(1));
            Point point = new Point(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            float pressure = Integer.parseInt(m.group(4)) / 255;
            while (!subqueue.offer(ControlMessage.createMoveEvent(pointerId, point, pressure)));
        }
    }

    private void parseUp(String s) {
        Matcher m = UP_PATTERN.matcher(s);
        if (m.find()) {
            long pointerId = Long.parseLong(m.group(1));
            while (!subqueue.offer(ControlMessage.createUpEvent(pointerId)));
        }
    }

    private void parseWait(String s) {
        Matcher m = WAIT_PATTERN.matcher(s);
        if (m.find()) {
            long milis = Long.parseLong(m.group(1));
            while (!subqueue.offer(ControlMessage.createWaitEvent(milis)));
        }
    }

    private void commitSubqueue() {
        if (!subqueue.isEmpty()) {
            try {
                queue.put(subqueue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.subqueue = new LinkedList<>();
        }
    }

    private void parseCommit(String s) {
        Matcher m = COMMIT_PATTERN.matcher(s);
        if (m.find()) {
            commitSubqueue();
        }
    }

    private void parseReset(String s) {
        Matcher m = RESET_PATTERN.matcher(s);
        if (m.find()) {
            subqueue.clear();
            while (!subqueue.offer(ControlMessage.createEmpty(ControlMessage.TYPE_EVENT_RESET)));
            commitSubqueue();
        }
    }

    private void parseInput(String s) {
        switch (s.charAt(0)) {
            case 'c':
                parseCommit(s);
                break;
            case 'r':
                parseReset(s);
                break;
            case 'd':
                parseDown(s);
                break;
            case 'm':
                parseMove(s);
                break;
            case 'u':
                parseUp(s);
                break;
            case 'w':
                parseWait(s);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while (this.isRunning) {
            String s;
            try {
                s = stdin.readLine();
                if (s.length() != 0) {
                    parseInput(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        this.isRunning = false;
    }
}
