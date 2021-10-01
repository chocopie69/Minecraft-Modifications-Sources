// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.alt;

import vip.radium.utils.Wrapper;
import vip.radium.utils.render.RenderingUtils;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltScreen extends GuiScreen
{
    public static String status;
    protected final GuiScreen parent;
    protected final List<GuiTextField> textFields;
    
    public GuiAltScreen(final GuiScreen parent) {
        this.textFields = new ArrayList<GuiTextField>();
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        if (!this.textFields.isEmpty()) {
            Keyboard.enableRepeatEvents(true);
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (!this.textFields.isEmpty()) {
            Keyboard.enableRepeatEvents(false);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.textFields.forEach(textField -> textField.textboxKeyTyped(typedChar, keyCode));
    }
    
    @Override
    public void updateScreen() {
        this.textFields.forEach(GuiTextField::updateCursorCounter);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFields.forEach(textField -> textField.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderingUtils.drawGuiBackground(this.width, this.height);
        Wrapper.getMinecraftFontRenderer().drawStringWithShadow(GuiAltScreen.status, this.width / 2.0f - Wrapper.getMinecraftFontRenderer().getWidth(GuiAltScreen.status) / 2.0f, 2.0f, -1);
        this.textFields.forEach(GuiTextField::drawTextBox);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
