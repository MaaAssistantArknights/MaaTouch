package com.shxyke.MaaTouch.wrappers;

import android.annotation.SuppressLint;
import android.os.Build;
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
    private ClipboardManager clipboardManager;

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
                Method getInstanceMethod;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    getInstanceMethod = Class.forName("android.hardware.input.InputManagerGlobal").getMethod("getInstance");
                } else {
                    getInstanceMethod = android.hardware.input.InputManager.class.getDeclaredMethod("getInstance");
                }
                Object im = getInstanceMethod.invoke(null);
                inputManager = new InputManager(im);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new AssertionError(e);
            }
        }
        return inputManager;
    }
    public ClipboardManager getClipboardManager() {
        if (clipboardManager == null) {
            IInterface clipboard = getService("clipboard", "android.content.IClipboard");
            if (clipboard == null) {
                return null;
            }
            clipboardManager = new ClipboardManager(clipboard);
        }
        return clipboardManager;
    }
}
