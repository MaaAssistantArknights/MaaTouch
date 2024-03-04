package com.shxyke.MaaTouch;

/**
 * Union of all supported event types, identified by their {@code type}.
 */
public final class ControlMessage {

    public static final int TYPE_EVENT_TOUCH_DOWN = 0;
    public static final int TYPE_EVENT_TOUCH_UP = 1;
    public static final int TYPE_EVENT_TOUCH_MOVE = 2;
    public static final int TYPE_EVENT_WAIT = 3;
    public static final int TYPE_EVENT_TOUCH_RESET = 4;
    public static final int TYPE_EVENT_KEY_DOWN = 5;
    public static final int TYPE_EVENT_KEY_UP = 6;
    public static final int TYPE_EVENT_WAIT_TIMESTAMP_SYNC = 100;

    private int type;
    private long pointerId;
    private float pressure;
    private Point point;
    private long millis;
    private int keycode;
    private int repeat;
    private int metaState;

    private ControlMessage(int type) {
        this.type = type;
    }

    public static ControlMessage createKeyDownEvent(int keycode, int repeat, int metaState) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_KEY_DOWN);
        msg.keycode = keycode;
        msg.repeat = repeat;
        msg.metaState = metaState;
        return msg;
    }

    public static ControlMessage createKeyUpEvent(int keycode, int repeat, int metaState) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_KEY_UP);
        msg.keycode = keycode;
        msg.repeat = repeat;
        msg.metaState = metaState;
        return msg;
    }

    public static ControlMessage createTouchDownEvent(long pointerId, Point point, float pressure) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_TOUCH_DOWN);
        msg.pointerId = pointerId;
        msg.pressure = pressure;
        msg.point = point;
        return msg;
    }

    public static ControlMessage createTouchUpEvent(long pointerId) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_TOUCH_UP);
        msg.pointerId = pointerId;
        return msg;
    }

    public static ControlMessage createTouchMoveEvent(long pointerId, Point point, float pressure) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_TOUCH_MOVE);
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

    public static ControlMessage createWaitTimestampSyncEvent(long milis) {
        ControlMessage msg = new ControlMessage(TYPE_EVENT_WAIT_TIMESTAMP_SYNC);
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

    public int getMetaState() {
        return metaState;
    }

    public int getKeycode() {
        return keycode;
    }

    public int getRepeat() {
        return repeat;
    }
}
