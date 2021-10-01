// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.color;

import java.awt.Color;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.ButtonComponent;

public abstract class ColorPickerComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent
{
    private boolean expanded;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    
    public ColorPickerComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    public abstract int getColor();
    
    public abstract void setColor(final int p0);
    
    public void onValueChange(final int value) {
        final float[] hsb = this.getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = (value >> 24 & 0xFF) / 255.0f;
    }
    
    private float[] getHSBFromColor(final int hex) {
        final int r = hex >> 16 & 0xFF;
        final int g = hex >> 8 & 0xFF;
        final int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }
    
    @Override
    public boolean isExpanded() {
        return this.expanded;
    }
    
    @Override
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
}
