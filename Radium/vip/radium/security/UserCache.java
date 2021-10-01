// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.security;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public final class UserCache
{
    public static String username;
    public static int uid;
    
    private UserCache() {
    }
    
    public static void init() {
        UserCache.username = ReflectionHelper.getUsername();
        UserCache.uid = ReflectionHelper.getUid();
        ReflectionHelper.refresh();
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            if (!ReflectionHelper.refreshed) {
                ReflectionHelper.exit();
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }
    
    public static String getUsername() {
        return UserCache.username;
    }
    
    public static int getUid() {
        return UserCache.uid;
    }
}
