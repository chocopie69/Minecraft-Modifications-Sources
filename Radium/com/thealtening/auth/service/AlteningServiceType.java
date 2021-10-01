// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.auth.service;

public enum AlteningServiceType
{
    MOJANG("MOJANG", 0, "https://authserver.mojang.com/", "https://sessionserver.mojang.com/"), 
    THEALTENING("THEALTENING", 1, "http://authserver.thealtening.com/", "http://sessionserver.thealtening.com/");
    
    private final String authServer;
    private final String sessionServer;
    
    private AlteningServiceType(final String name, final int ordinal, final String authServer, final String sessionServer) {
        this.authServer = authServer;
        this.sessionServer = sessionServer;
    }
    
    public String getAuthServer() {
        return this.authServer;
    }
    
    public String getSessionServer() {
        return this.sessionServer;
    }
}
