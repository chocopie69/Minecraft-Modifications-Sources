// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.world;

import vip.radium.event.Event;

public final class ScoreboardModeChangeEvent implements Event
{
    private final String mode;
    
    public ScoreboardModeChangeEvent(final String mode) {
        this.mode = mode;
    }
    
    public String getMode() {
        return this.mode;
    }
}
