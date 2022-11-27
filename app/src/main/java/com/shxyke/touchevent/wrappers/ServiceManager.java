package com.shxyke.touchevent.wrappers;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("PrivateApi,DiscouragedPrivateApi")
public final class ServiceManager {

    public static final String PACKAGE_NAME = "com.android.shell";
    public static final int USER_ID = 0;

    private final Method getServiceMethod;

    private DisplayManager displayManager;
    private InputManager inputManager;

    public ServiceManager() {
        try {
            getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private IInterface getService(String service, String type) {
        try {
            IBinder binder = (IBinder) getServiceMethod.invoke(null, service);
            Method asInterfaceMethod = Class.forName(type + "$Stub").getMethod("asInterface", IBinder.class);
            return (IInterface) asInterfaceMethod.invoke(null, binder);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public DisplayManager getDisplayManager() {
        if (displayManager == null) {
            displayManager = new DisplayManager(getService("display", "android.hardware.display.IDisplayManager"));
        }
        return displayManager;
    }

    public InputManager getInputManager() {
        if (inputManager == null) {
            try {
                Method getInstanceMethod = android.hardware.input.InputManager.class.getDeclaredMethod("getInstance");
                android.hardware.input.InputManager im = (android.hardware.input.InputManager) getInstanceMethod.invoke(null);
                inputManager = new InputManager(im);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new AssertionError(e);
            }
        }
        return inputManager;
    }
}
