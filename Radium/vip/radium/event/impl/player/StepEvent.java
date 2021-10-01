// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.player;

import vip.radium.event.Event;

public final class StepEvent implements Event
{
    private float stepHeight;
    private double heightStepped;
    private boolean pre;
    
    public StepEvent(final float stepHeight) {
        this.stepHeight = stepHeight;
        this.pre = true;
    }
    
    public double getHeightStepped() {
        return this.heightStepped;
    }
    
    public void setHeightStepped(final double heightStepped) {
        this.heightStepped = heightStepped;
    }
    
    public boolean isPre() {
        return this.pre;
    }
    
    public void setPost() {
        this.pre = false;
    }
    
    public float getStepHeight() {
        return this.stepHeight;
    }
    
    public void setStepHeight(final float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
