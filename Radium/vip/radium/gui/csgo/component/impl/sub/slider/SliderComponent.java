// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.slider;

import java.math.BigDecimal;
import vip.radium.gui.font.FontRenderer;
import org.lwjgl.opengl.GL11;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import java.text.DecimalFormat;
import net.minecraft.util.MathHelper;
import vip.radium.property.impl.Representation;
import vip.radium.utils.render.LockedResolution;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.ButtonComponent;

public abstract class SliderComponent extends ButtonComponent implements PredicateComponent
{
    private boolean sliding;
    
    public SliderComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    @Override
    public void drawComponent(final LockedResolution resolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final double min = this.getMin();
        final double max = this.getMax();
        final double dValue = this.getValue();
        final Representation representation = this.getRepresentation();
        final boolean isInt = representation == Representation.INT || representation == Representation.MILLISECONDS;
        double value;
        if (isInt) {
            value = (int)dValue;
        }
        else {
            value = dValue;
        }
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding) {
            if (mouseX >= x - 0.5f && mouseY >= y - 0.5f && mouseX <= x + width + 0.5f && mouseY <= y + height + 0.5f) {
                this.setValue(MathHelper.clamp_double(this.roundToIncrement((mouseX - x) * (max - min) / (width - 1.0f) + min), min, max));
            }
            else {
                this.sliding = false;
            }
        }
        final double sliderPercentage = (value - min) / (max - min);
        String valueString;
        if (isInt) {
            valueString = Integer.toString((int)value);
        }
        else {
            final DecimalFormat format = new DecimalFormat("####.##");
            valueString = format.format(value);
        }
        switch (representation) {
            case PERCENTAGE: {
                valueString = String.valueOf(valueString) + '%';
                break;
            }
            case MILLISECONDS: {
                valueString = String.valueOf(valueString) + "ms";
                break;
            }
            case DISTANCE: {
                valueString = String.valueOf(valueString) + 'm';
                break;
            }
        }
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(855309));
        RenderingUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, SkeetUI.getColor(hovered ? RenderingUtils.darker(4802889, 1.4f) : 4802889), SkeetUI.getColor(hovered ? RenderingUtils.darker(3158064, 1.4f) : 3158064));
        RenderingUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width * sliderPercentage - 0.5, y + height - 0.5f, false, SkeetUI.getColor(), RenderingUtils.darker(SkeetUI.getColor(), 0.8f));
        if (SkeetUI.shouldRenderText()) {
            final float stringWidth = SkeetUI.GROUP_BOX_HEADER_RENDERER.getWidth(valueString);
            GL11.glTranslatef(0.0f, 0.0f, 1.0f);
            if (SkeetUI.getAlpha() > 120.0) {
                RenderingUtils.drawOutlinedString(SkeetUI.GROUP_BOX_HEADER_RENDERER, valueString, x + width * (float)sliderPercentage - stringWidth / 2.0f, y + height / 2.0f, SkeetUI.getColor(16777215), 2013265920);
            }
            else {
                SkeetUI.GROUP_BOX_HEADER_RENDERER.drawString(valueString, x + width * (float)sliderPercentage - stringWidth / 2.0f, y + height / 2.0f, SkeetUI.getColor(16777215));
            }
            GL11.glTranslatef(0.0f, 0.0f, -1.0f);
        }
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (!this.sliding && mouseButton == 0) {
            this.sliding = true;
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        this.sliding = false;
    }
    
    private double roundToIncrement(final double value) {
        final double inc = this.getIncrement();
        final double halfOfInc = inc / 2.0;
        final double floored = StrictMath.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(StrictMath.ceil(value / inc) * inc).setScale(2, 4).doubleValue();
        }
        return new BigDecimal(floored).setScale(2, 4).doubleValue();
    }
    
    public abstract double getValue();
    
    public abstract void setValue(final double p0);
    
    public abstract Representation getRepresentation();
    
    public abstract double getMin();
    
    public abstract double getMax();
    
    public abstract double getIncrement();
}
