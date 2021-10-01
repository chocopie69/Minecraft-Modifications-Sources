// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.alt.impl;

import java.io.IOException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import vip.radium.utils.SessionUtils;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import vip.radium.alt.Alt;
import vip.radium.gui.alt.GuiAltScreen;

public final class GuiAltManager extends GuiAltScreen
{
    private Alt selectedAlt;
    
    public GuiAltManager(final GuiScreen parent) {
        super(parent);
    }
    
    @Override
    public void initGui() {
        final int buttonWidth = 100;
        final int buttonHeight = 20;
        final int margin = 2;
        final int secondRowHeight = this.height - 22;
        final int firstRowHeight = secondRowHeight - 22;
        final int middle = this.width / 2 - 50;
        this.buttonList.add(new GuiButton(0, 2, secondRowHeight, 100, 20, "Back"));
        this.buttonList.add(new GuiButton(1, middle, secondRowHeight, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(2, middle + 100 + 2, secondRowHeight, 100, 20, "Login"));
        this.buttonList.add(new GuiButton(3, middle - 100 - 2, secondRowHeight, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, 2, 2, 100, 20, "TheAltening"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                Wrapper.getMinecraft().displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                Wrapper.getMinecraft().displayGuiScreen(new GuiDirectLogin(this));
                break;
            }
            case 2: {
                if (this.selectedAlt != null) {
                    SessionUtils.logIn(this.selectedAlt);
                    break;
                }
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Please select an account", 1000L, NotificationType.ERROR));
                break;
            }
            case 3: {
                Wrapper.getMinecraft().displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                Wrapper.getMinecraft().displayGuiScreen(new GuiAltening(this));
                break;
            }
        }
    }
}
