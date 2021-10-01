// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.render;

import vip.radium.utils.render.LockedResolution;
import vip.radium.event.Event;

public final class Render2DEvent implements Event
{
    private final LockedResolution resolution;
    private final float partialTicks;
    
    public Render2DEvent(final LockedResolution resolution, final float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }
    
    public LockedResolution getResolution() {
        return this.resolution;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
