// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.text;

import vip.radium.utils.render.LockedResolution;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.font.FontRenderer;
import vip.radium.gui.csgo.component.Component;

public final class TextComponent extends Component
{
    private static final FontRenderer FONT_RENDERER;
    private final String text;
    
    static {
        FONT_RENDERER = SkeetUI.FONT_RENDERER;
    }
    
    public TextComponent(final Component parent, final String text, final float x, final float y) {
        super(parent, x, y, TextComponent.FONT_RENDERER.getWidth(text), TextComponent.FONT_RENDERER.getHeight(text));
        this.text = text;
    }
    
    @Override
    public void drawComponent(final LockedResolution resolution, final int mouseX, final int mouseY) {
        if (SkeetUI.shouldRenderText()) {
            TextComponent.FONT_RENDERER.drawString(this.text, this.getX(), this.getY(), SkeetUI.getColor(15132390));
        }
    }
}
