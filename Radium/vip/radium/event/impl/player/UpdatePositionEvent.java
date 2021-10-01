// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.player;

import vip.radium.event.CancellableEvent;

public final class UpdatePositionEvent extends CancellableEvent
{
    private final float prevYaw;
    private final float prevPitch;
    private final double prevPosX;
    private final double prevPosY;
    private final double prevPosZ;
    private double posX;
    private double posY;
    private double posZ;
    private float yaw;
    private float pitch;
    private boolean ground;
    private boolean pre;
    private boolean rotating;
    
    public UpdatePositionEvent(final float prevYaw, final float prevPitch, final double posX, final double posY, final double posZ, final double prevPosX, final double prevPosY, final double prevPosZ, final float yaw, final float pitch, final boolean ground) {
        this.prevYaw = prevYaw;
        this.prevPitch = prevPitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.prevPosX = prevPosX;
        this.prevPosY = prevPosY;
        this.prevPosZ = prevPosZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.pre = true;
    }
    
    public double getPrevPosX() {
        return this.prevPosX;
    }
    
    public double getPrevPosY() {
        return this.prevPosY;
    }
    
    public double getPrevPosZ() {
        return this.prevPosZ;
    }
    
    public boolean isRotating() {
        return this.rotating;
    }
    
    public float getPrevYaw() {
        return this.prevYaw;
    }
    
    public float getPrevPitch() {
        return this.prevPitch;
    }
    
    public boolean isPre() {
        return this.pre;
    }
    
    public void setPost() {
        this.pre = false;
    }
    
    public double getPosX() {
        return this.posX;
    }
    
    public void setPosX(final double posX) {
        this.posX = posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public void setPosY(final double posY) {
        this.posY = posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.rotating = (this.yaw - yaw != 0.0f);
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.rotating = (this.pitch - pitch != 0.0f);
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.ground;
    }
    
    public void setOnGround(final boolean ground) {
        this.ground = ground;
    }
}
