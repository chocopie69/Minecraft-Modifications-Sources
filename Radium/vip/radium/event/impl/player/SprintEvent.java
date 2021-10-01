// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.player;

import vip.radium.event.Event;

public final class SprintEvent implements Event
{
    private boolean sprinting;
    
    public SprintEvent(final boolean sprinting) {
        this.sprinting = sprinting;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
