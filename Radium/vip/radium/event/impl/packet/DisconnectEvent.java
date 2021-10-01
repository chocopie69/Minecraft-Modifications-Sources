// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.packet;

import vip.radium.event.Event;

public final class DisconnectEvent implements Event
{
    private final String reason;
    
    public DisconnectEvent(final String reason) {
        this.reason = reason;
    }
    
    public String getReason() {
        return this.reason;
    }
}
