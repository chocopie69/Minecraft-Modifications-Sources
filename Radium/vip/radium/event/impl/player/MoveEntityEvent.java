// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.player;

import vip.radium.event.CancellableEvent;

public final class MoveEntityEvent extends CancellableEvent
{
    private double x;
    private double y;
    private double z;
    
    public MoveEntityEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
}
