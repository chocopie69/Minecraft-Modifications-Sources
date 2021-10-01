// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.key;

import org.lwjgl.input.Keyboard;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.render.LockedResolution;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.SkeetUI;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vip.radium.gui.font.FontRenderer;
import vip.radium.gui.csgo.component.ButtonComponent;

public final class KeyBindComponent extends ButtonComponent
{
    private static final FontRenderer FONT_RENDERER;
    private final Supplier<Integer> getBind;
    private final Consumer<Integer> onSetBind;
    private boolean binding;
    
    static {
        FONT_RENDERER = SkeetUI.KEYBIND_FONT_RENDERER;
    }
    
    public KeyBindComponent(final Component parent, final Supplier<Integer> getBind, final Consumer<Integer> onSetBind, final float x, final float y) {
        super(parent, x, y, KeyBindComponent.FONT_RENDERER.getWidth("[") * 2.0f, KeyBindComponent.FONT_RENDERER.getHeight("[]"));
        this.getBind = getBind;
        this.onSetBind = onSetBind;
    }
    
    @Override
    public float getWidth() {
        return super.getWidth() + KeyBindComponent.FONT_RENDERER.getWidth(this.getBind());
    }
    
    @Override
    public void drawComponent(final LockedResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        RenderingUtils.drawOutlinedString(KeyBindComponent.FONT_RENDERER, "[" + this.getBind() + "]", x + 40.166668f - width, y, SkeetUI.getColor(7895160), SkeetUI.getColor(0));
    }
    
    @Override
    public boolean isHovered(final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        return mouseX >= x + 40.166668f - this.getWidth() && mouseY >= y && mouseX <= x + 40.166668f && mouseY <= y + this.getHeight();
    }
    
    @Override
    public void onKeyPress(int keyCode) {
        if (this.binding) {
            if (keyCode == 211) {
                keyCode = 0;
            }
            this.onChangeBind(keyCode);
            this.binding = false;
        }
    }
    
    private String getBind() {
        final int bind = this.getBind.get();
        return this.binding ? "..." : ((bind == 0) ? "-" : Keyboard.getKeyName(bind));
    }
    
    private void onChangeBind(final int bind) {
        this.onSetBind.accept(bind);
    }
    
    @Override
    public void onPress(final int mouseButton) {
        this.binding = !this.binding;
    }
}
