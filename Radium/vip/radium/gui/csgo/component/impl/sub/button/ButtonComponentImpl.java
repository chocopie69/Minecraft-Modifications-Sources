// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.button;

import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.gui.Gui;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.utils.render.LockedResolution;
import vip.radium.gui.csgo.component.Component;
import java.util.function.Consumer;
import vip.radium.gui.csgo.component.ButtonComponent;

public final class ButtonComponentImpl extends ButtonComponent
{
    private final String text;
    private final Consumer<Integer> onPress;
    
    public ButtonComponentImpl(final Component parent, final String text, final Consumer<Integer> onPress, final float width, final float height) {
        super(parent, 0.0f, 0.0f, width, height);
        this.text = text;
        this.onPress = onPress;
    }
    
    @Override
    public void drawComponent(final LockedResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        final float height = this.getHeight();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(1118481));
        Gui.drawRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, SkeetUI.getColor(2500134));
        RenderingUtils.drawGradientRect(x + 1.0f, y + 1.0f, x + width - 1.0f, y + height - 1.0f, false, SkeetUI.getColor(hovered ? RenderingUtils.darker(2236962, 1.2f) : 2236962), SkeetUI.getColor(hovered ? RenderingUtils.darker(1973790, 1.2f) : 1973790));
        if (SkeetUI.shouldRenderText()) {
            RenderingUtils.drawOutlinedString(SkeetUI.FONT_RENDERER, this.text, x + width / 2.0f - SkeetUI.FONT_RENDERER.getWidth(this.text) / 2.0f, y + height / 2.0f - 1.0f, SkeetUI.getColor(16777215), SkeetUI.getColor(0));
        }
    }
    
    @Override
    public void onPress(final int mouseButton) {
        this.onPress.accept(mouseButton);
    }
}
