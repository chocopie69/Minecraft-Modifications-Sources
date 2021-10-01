// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.world;

import vip.radium.event.Event;

public final class ScoreboardHeaderChangeEvent implements Event
{
    private final String header;
    
    public ScoreboardHeaderChangeEvent(final String header) {
        this.header = header;
    }
    
    public String getHeader() {
        return this.header;
    }
}
