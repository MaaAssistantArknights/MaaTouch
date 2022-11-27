package com.shxyke.MaaTouch;

/**
 * Union of all supported event types, identified by their {@code type}.
 */
public final class ControlMessage {

    public static final int TYPE_EVENT_DOWN = 0;
    public static final int TYPE_EVENT_UP = 1;
    public static final int TYPE_EVENT_MOVE = 2;
    public static final int TYPE_EVENT_WAIT = 3;
    public static final int TYPE_EVENT_RESET = 4;

    private int type;
    private long pointerId;
    private float pressure;
    private Point point;
    private long millis;

    private ControlMessage(int type) {
        this.type = type;
    }

    public static ControlMessage createDownEvent(long pointerId, Point point, float pressure) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_DOWN);
        msg.pointerId = pointerId;
        msg.pressure = pressure;
        msg.point = point;
        return msg;
    }

    public static ControlMessage createUpEvent(long pointerId) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_UP);
        msg.pointerId = pointerId;
        return msg;
    }

    public static ControlMessage createMoveEvent(long pointerId, Point point, float pressure) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_MOVE);
        msg.pointerId = pointerId;
        msg.pressure = pressure;
        msg.point = point;
        return msg;
    }

    public static ControlMessage createWaitEvent(long milis) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_WAIT);
        msg.millis = milis;
        return msg;
    }

    public static ControlMessage createEmpty(int type) {
        ControlMessage msg = new ControlMessage(type);
        msg.type = type;
        return msg;
    }

    public int getType() {
        return type;
    }

    public long getPointerId() {
        return pointerId;
    }

    public float getPressure() {
        return pressure;
    }

    public Point getPoint() {
        return point;
    }

    public long getMillis() {
        return millis;
    }
}
