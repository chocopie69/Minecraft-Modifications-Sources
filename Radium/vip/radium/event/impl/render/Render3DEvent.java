// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import vip.radium.event.Event;

public final class Render3DEvent implements Event
{
    private final ScaledResolution scaledResolution;
    private final float partialTicks;
    
    public Render3DEvent(final ScaledResolution scaledResolution, final float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }
    
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
