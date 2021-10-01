// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.world;

import vip.radium.event.CancellableEvent;

public final class PlaySoundEvent extends CancellableEvent
{
    private final double posX;
    private final double posY;
    private final double posZ;
    private final double dist;
    private final String soundName;
    
    public PlaySoundEvent(final double posX, final double posY, final double posZ, final double dist, final String soundName) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.dist = dist;
        this.soundName = soundName;
    }
    
    public double getDist() {
        return this.dist;
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public String getSoundName() {
        return this.soundName;
    }
}
