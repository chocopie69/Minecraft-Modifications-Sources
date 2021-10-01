// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.comboBox;

import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.utils.render.LockedResolution;
import java.util.List;
import vip.radium.utils.StringUtils;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.ButtonComponent;

public abstract class ComboBoxComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent
{
    private boolean expanded;
    
    public ComboBoxComponent(final Component parent, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
    }
    
    private String getDisplayString() {
        if (!this.isMultiSelectable()) {
            return StringUtils.upperSnakeCaseToPascal(this.getValue().name());
        }
        final List<Enum<?>> values = this.getMultiSelectValues();
        final int len = values.size();
        switch (len) {
            case 0: {
                return "";
            }
            case 1: {
                return StringUtils.upperSnakeCaseToPascal(values.get(0).name());
            }
            default: {
                final StringBuilder sb = new StringBuilder(StringUtils.upperSnakeCaseToPascal(values.get(0).name())).append(", ");
                for (int i = 1; i < len; ++i) {
                    sb.append(StringUtils.upperSnakeCaseToPascal(values.get(i).name()));
                    if (i != len - 1) {
                        sb.append(", ");
                    }
                }
                return sb.toString();
            }
        }
    }
    
    @Override
    public void drawComponent(final LockedResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(855309));
        final boolean hovered = this.isHovered(mouseX, mouseY);
        RenderingUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, SkeetUI.getColor(hovered ? RenderingUtils.darker(1973790, 1.4f) : 1973790), SkeetUI.getColor(hovered ? RenderingUtils.darker(2302755, 1.4f) : 2302755));
        GL11.glColor4f(0.6f, 0.6f, 0.6f, (float)SkeetUI.getAlpha() / 255.0f);
        RenderingUtils.drawAndRotateArrow(x + width - 5.0f, y + height / 2.0f - 0.5f, 3.0f, this.isExpanded());
        if (SkeetUI.shouldRenderText()) {
            GL11.glEnable(3089);
            OGLUtils.startScissorBox(lockedResolution, (int)x + 2, (int)y + 1, (int)width - 8, (int)height - 1);
            SkeetUI.FONT_RENDERER.drawString(this.getDisplayString(), x + 2.0f, y + height / 3.0f, SkeetUI.getColor(9868950));
            GL11.glDisable(3089);
        }
        GL11.glTranslatef(0.0f, 0.0f, 2.0f);
        if (this.expanded) {
            final Enum[] values = this.getValues();
            final float dropDownHeight = values.length * height;
            Gui.drawRect(x, y + height, x + width, y + height + dropDownHeight + 0.5f, SkeetUI.getColor(855309));
            float valueBoxHeight = height;
            final Enum[] enums = this.getValues();
            Enum[] array;
            for (int length = (array = enums).length, i = 0; i < length; ++i) {
                final Enum<?> value = (Enum<?>)array[i];
                final boolean valueBoxHovered = mouseX >= x && mouseY >= y + valueBoxHeight && mouseX <= x + width && mouseY < y + valueBoxHeight + height;
                Gui.drawRect(x + 0.5f, y + valueBoxHeight, x + width - 0.5f, y + valueBoxHeight + height, SkeetUI.getColor(valueBoxHovered ? RenderingUtils.darker(2302755, 0.7f) : 2302755));
                boolean selected;
                if (this.isMultiSelectable()) {
                    selected = this.getMultiSelectValues().contains(value);
                }
                else {
                    selected = (value == this.getValue());
                }
                final int color = selected ? SkeetUI.getColor() : SkeetUI.getColor(14474460);
                FontRenderer fr;
                if (selected || valueBoxHovered) {
                    fr = SkeetUI.GROUP_BOX_HEADER_RENDERER;
                }
                else {
                    fr = SkeetUI.FONT_RENDERER;
                }
                fr.drawString(StringUtils.upperSnakeCaseToPascal(value.name()), x + 2.0f, y + valueBoxHeight + 4.0f, color);
                valueBoxHeight += height;
            }
        }
        GL11.glTranslatef(0.0f, 0.0f, -2.0f);
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY)) {
            this.onPress(button);
        }
        if (this.expanded && button == 0) {
            final float x = this.getX();
            final float y = this.getY();
            final float height = this.getHeight();
            final float width = this.getWidth();
            float valueBoxHeight = height;
            for (int i = 0; i < this.getValues().length; ++i) {
                if (mouseX >= x && mouseY >= y + valueBoxHeight && mouseX <= x + width && mouseY < y + valueBoxHeight + height) {
                    this.setValue(i);
                    if (!this.isMultiSelectable()) {
                        this.expandOrClose();
                    }
                    return;
                }
                valueBoxHeight += height;
            }
        }
    }
    
    private void expandOrClose() {
        this.setExpanded(!this.isExpanded());
    }
    
    @Override
    public void onPress(final int mouseButton) {
        if (mouseButton == 1) {
            this.expandOrClose();
        }
    }
    
    public abstract Enum<?> getValue();
    
    public abstract void setValue(final int p0);
    
    public abstract List<Enum<?>> getMultiSelectValues();
    
    public abstract boolean isMultiSelectable();
    
    public abstract Enum<?>[] getValues();
    
    @Override
    public boolean isExpanded() {
        return this.expanded;
    }
    
    @Override
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public float getExpandedHeight() {
        return this.getValues().length * this.getHeight();
    }
}
