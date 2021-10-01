// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.player;

import vip.radium.event.CancellableEvent;

public final class SendMessageEvent extends CancellableEvent
{
    private String message;
    
    public SendMessageEvent(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
