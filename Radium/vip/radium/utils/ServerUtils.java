// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import java.util.HashMap;
import java.util.Map;

public final class ServerUtils
{
    private static final Map<String, Long> serverIpPingCache;
    private static final String HYPIXEL = "hypixel.net";
    
    static {
        serverIpPingCache = new HashMap<String, Long>();
    }
    
    private ServerUtils() {
    }
    
    public static void update(final String ip, final long ping) {
        ServerUtils.serverIpPingCache.put(ip, ping);
    }
    
    public static long getPingToServer(final String ip) {
        return ServerUtils.serverIpPingCache.get(ip);
    }
    
    public static boolean isOnServer(final String ip) {
        return !Wrapper.getMinecraft().isSingleplayer() && getCurrentServerIP().endsWith(ip);
    }
    
    public static String getCurrentServerIP() {
        return Wrapper.getMinecraft().isSingleplayer() ? "Singleplayer" : Wrapper.getMinecraft().getCurrentServerData().serverIP;
    }
    
    public static boolean isOnHypixel() {
        return isOnServer("hypixel.net");
    }
    
    public static long getPingToCurrentServer() {
        return Wrapper.getMinecraft().isSingleplayer() ? 0L : getPingToServer(getCurrentServerIP());
    }
}
