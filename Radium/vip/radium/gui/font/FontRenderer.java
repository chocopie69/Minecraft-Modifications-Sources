// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.font;

public interface FontRenderer
{
    int drawString(final String p0, final float p1, final float p2, final int p3);
    
    int drawStringWithShadow(final String p0, final float p1, final float p2, final int p3);
    
    float getWidth(final String p0);
    
    default float getHeight(final String text) {
        return 11.0f;
    }
}
