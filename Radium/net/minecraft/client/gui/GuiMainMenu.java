// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import vip.radium.utils.render.Colors;
import org.lwjgl.opengl.GL11;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.RenderingUtils;
import net.optifine.reflect.Reflector;
import vip.radium.gui.alt.impl.GuiAltManager;
import net.minecraft.client.resources.I18n;
import java.io.IOException;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private GuiButton buttonResetDemo;
    private DynamicTexture viewportTexture;
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        final int i = 24;
        final int j = this.height / 4 + 48;
        this.addSingleplayerMultiplayerButtons(j, 24);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));
        this.mc.func_181537_a(false);
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(100, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alt Manager"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 100) {
            this.mc.displayGuiScreen(new GuiAltManager(this));
        }
        else if (button.id == 4) {
            this.mc.shutdown();
        }
        else if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, this));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderingUtils.drawGuiBackground(this.width, this.height);
        final String firstChar = String.valueOf("Radium".charAt(0));
        final String restOfName = "Radium".substring(1);
        final float scale = 4.0f;
        final float firstCharWidth = Wrapper.getFontRenderer().getWidth(firstChar);
        final float restOfNameWidth = Wrapper.getFontRenderer().getWidth(restOfName);
        float textX = this.width / 2.0f - (firstCharWidth + restOfNameWidth) * scale / 2.0f;
        int textHeight = this.height / 4 - 24;
        textX /= scale;
        textHeight /= (int)scale;
        GL11.glScaled((double)scale, (double)scale, 1.0);
        Wrapper.getMinecraftFontRenderer().drawStringWithShadow("Radium", textX, (float)textHeight, Colors.DEEP_PURPLE);
        GL11.glScaled((double)(1.0f / scale), (double)(1.0f / scale), 1.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
