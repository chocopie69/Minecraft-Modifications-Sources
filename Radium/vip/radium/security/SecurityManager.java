// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.security;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public final class SecurityManager
{
    public static final int[] RUNTIME_CLASS_PATH;
    public static final int[] CLASS_CLASS;
    public static final int[] GET_RUNTIME_METHOD;
    public static final int[] EXIT_METHOD;
    public static final int[] FOR_NAME_METHOD;
    public static final int[] API_CLASS_PATH;
    public static final int[] SHUTDOWN_METHOD;
    public static final int[] REFRESH_METHOD;
    public static final int[] GET_USERNAME_METHOD;
    public static final int[] GET_UID_METHOD;
    
    static {
        RUNTIME_CLASS_PATH = new int[] { 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 82, 117, 110, 116, 105, 109, 101 };
        CLASS_CLASS = new int[] { 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 67, 108, 97, 115, 115 };
        GET_RUNTIME_METHOD = new int[] { 103, 101, 116, 82, 117, 110, 116, 105, 109, 101 };
        EXIT_METHOD = new int[] { 101, 120, 105, 116 };
        FOR_NAME_METHOD = new int[] { 102, 111, 114, 78, 97, 109, 101 };
        API_CLASS_PATH = new int[] { 109, 101, 46, 120, 101, 110, 46, 97, 110, 116, 105, 108, 101, 97, 107, 46, 65, 80, 73 };
        SHUTDOWN_METHOD = new int[] { 115, 104, 117, 116, 100, 111, 119, 110 };
        REFRESH_METHOD = new int[] { 114, 101, 102, 114, 101, 115, 104 };
        GET_USERNAME_METHOD = new int[] { 103, 101, 116, 85, 115, 101, 114, 110, 97, 109, 101 };
        GET_UID_METHOD = new int[] { 103, 101, 116, 85, 73, 68 };
    }
    
    private SecurityManager() {
    }
    
    public static void init() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(ReflectionHelper::refresh, 30L, 30L, TimeUnit.MINUTES);
    }
}
