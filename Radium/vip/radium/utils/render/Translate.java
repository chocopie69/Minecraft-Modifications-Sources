// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils.render;

public final class Translate
{
    private double x;
    private double y;
    
    public Translate(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public void animate(final double newX, final double newY) {
        this.x = RenderingUtils.progressiveAnimation(this.x, newX, 1.0);
        this.y = RenderingUtils.progressiveAnimation(this.y, newY, 1.0);
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
