// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.entity;

import vip.radium.event.CancellableEvent;

public final class SpawnParticleEntityEvent extends CancellableEvent
{
    private final int type;
    private int multiplier;
    
    public SpawnParticleEntityEvent(final int type, final int multiplier) {
        this.type = type;
        this.multiplier = multiplier;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getMultiplier() {
        return this.multiplier;
    }
    
    public void setMultiplier(final int multiplier) {
        this.multiplier = multiplier;
    }
}
