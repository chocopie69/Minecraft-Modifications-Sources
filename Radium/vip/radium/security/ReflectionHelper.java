// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.security;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import vip.radium.utils.StringUtils;
import java.util.concurrent.ThreadLocalRandom;

public final class ReflectionHelper
{
    private static final int EXIT_CODE;
    private static final Class<?> CLASS_CLASS;
    private static final Class<?> API_CLASS;
    private static final Class<?> RUNTIME_CLASS;
    public static boolean refreshed;
    
    static {
        EXIT_CODE = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        CLASS_CLASS = getClass(StringUtils.fromCharCodes(SecurityManager.CLASS_CLASS));
        RUNTIME_CLASS = getClass(StringUtils.fromCharCodes(SecurityManager.RUNTIME_CLASS_PATH));
        API_CLASS = getAPIClass(StringUtils.fromCharCodes(SecurityManager.API_CLASS_PATH));
    }
    
    private ReflectionHelper() {
    }
    
    private static Class<?> getAPIClass(final String name) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        }
        catch (ClassNotFoundException var2) {
            exit();
            return null;
        }
    }
    
    private static Class<?> getClass(final String name) {
        try {
            if (ReflectionHelper.CLASS_CLASS == null) {
                return Class.forName(name);
            }
            try {
                final Method forNameM = getMethod(StringUtils.fromCharCodes(SecurityManager.FOR_NAME_METHOD), ReflectionHelper.CLASS_CLASS, String.class);
                return (Class<?>)forNameM.invoke(null, name);
            }
            catch (IllegalAccessException var2) {
                exit();
                return null;
            }
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (ClassNotFoundException ex3) {}
    }
    
    private static Method getMethod(final String name, final Class<?> parent, final Class<?>... params) {
        try {
            return parent.getDeclaredMethod(name, params);
        }
        catch (NoSuchMethodException var4) {
            exit();
            return null;
        }
    }
    
    public static void shutdown() {
        try {
            final Method shutdownM = getMethod(StringUtils.fromCharCodes(SecurityManager.SHUTDOWN_METHOD), ReflectionHelper.API_CLASS, (Class<?>[])new Class[0]);
            shutdownM.invoke(null, new Object[0]);
        }
        catch (InvocationTargetException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException var1 = ex;
            shutdown();
        }
    }
    
    public static void exit() {
        try {
            final Method exitM = getMethod(StringUtils.fromCharCodes(SecurityManager.EXIT_METHOD), ReflectionHelper.RUNTIME_CLASS, Integer.TYPE);
            final Method getRuntimeM = getMethod(StringUtils.fromCharCodes(SecurityManager.GET_RUNTIME_METHOD), ReflectionHelper.RUNTIME_CLASS, (Class<?>[])new Class[0]);
            exitM.invoke(getRuntimeM.invoke(null, new Object[0]), new Object[0]);
            Runtime.getRuntime().exit(ReflectionHelper.EXIT_CODE);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException var5) {
            System.exit(ReflectionHelper.EXIT_CODE);
            return;
        }
        finally {
            shutdown();
            exit();
        }
        shutdown();
        exit();
    }
    
    public static void refresh() {
        try {
            final Method refreshM = getMethod(StringUtils.fromCharCodes(SecurityManager.REFRESH_METHOD), ReflectionHelper.API_CLASS, (Class<?>[])new Class[0]);
            refreshM.invoke(null, new Object[0]);
            ReflectionHelper.refreshed = true;
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException var1) {
            exit();
            ReflectionHelper.refreshed = false;
            while (true) {}
        }
        finally {
            if (!ReflectionHelper.refreshed) {
                exit();
                while (true) {}
            }
        }
        if (ReflectionHelper.refreshed) {
            return;
        }
        exit();
        while (true) {}
    }
    
    public static String getUsername() {
        try {
            final Method getUsernameM = getMethod(StringUtils.fromCharCodes(SecurityManager.GET_USERNAME_METHOD), ReflectionHelper.API_CLASS, (Class<?>[])new Class[0]);
            return (String)getUsernameM.invoke(null, new Object[0]);
        }
        catch (Exception var2) {
            exit();
            return null;
        }
    }
    
    public static int getUid() {
        int retValue = -1;
        try {
            final Method getUidM = getMethod(StringUtils.fromCharCodes(SecurityManager.GET_UID_METHOD), ReflectionHelper.API_CLASS, (Class<?>[])new Class[0]);
            retValue = (int)getUidM.invoke(null, new Object[0]);
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException var6) {
            exit();
        }
        Label_0054: {
            if (retValue == -1) {
                shutdown();
                break Label_0054;
            }
            break Label_0054;
        }
        while (true) {}
    }
}
