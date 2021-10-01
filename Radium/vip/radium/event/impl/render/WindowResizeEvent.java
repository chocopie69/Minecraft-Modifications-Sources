// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import vip.radium.event.Event;

public final class WindowResizeEvent implements Event
{
    private final ScaledResolution scaledResolution;
    
    public WindowResizeEvent(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
    
    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }
}
