// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.alt.impl;

import java.io.IOException;
import com.thealtening.api.response.Account;
import com.google.gson.JsonSyntaxException;
import com.thealtening.api.TheAlteningException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.alt.Alt;
import vip.radium.utils.SessionUtils;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.gui.GuiButton;
import vip.radium.RadiumClient;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import vip.radium.gui.alt.GuiAltScreen;

public final class GuiAltening extends GuiAltScreen
{
    private GuiTextField tokenField;
    
    public GuiAltening(final GuiScreen parent) {
        super(parent);
    }
    
    @Override
    public void initGui() {
        final int buttonWidth = 200;
        final int buttonHeight = 20;
        final int margin = 2;
        final int offset = 22;
        final int middleX = this.width / 2 - 100;
        final int middleY = this.height / 4 + 48;
        this.tokenField = new GuiTextField(3, Wrapper.getMinecraftFontRenderer(), "API Key", middleX, middleY, 200, 18);
        this.textFields.add(this.tokenField);
        this.tokenField.setText(RadiumClient.getInstance().getAltManager().getAPIKey());
        this.buttonList.add(new GuiButton(2, middleX, middleY + 22, "Update API Key"));
        this.buttonList.add(new GuiButton(1, middleX, middleY + 44, "Generate"));
        this.buttonList.add(new GuiButton(0, middleX, middleY + 66, "Back"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                Wrapper.getMinecraft().displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                SessionUtils.switchService(AlteningServiceType.THEALTENING);
                try {
                    final Account account = RadiumClient.getInstance().getAltManager().getAlteningAltFetcher().getAccount();
                    SessionUtils.logIn(new Alt(account.getToken(), "A"));
                }
                catch (TheAlteningException | JsonSyntaxException ex2) {
                    final RuntimeException ex;
                    final RuntimeException ignored = ex;
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("TheAltening Exception", "Failed to generate account", 1000L, NotificationType.ERROR));
                }
                break;
            }
            case 2: {
                RadiumClient.getInstance().getAltManager().saveAPIKey(this.tokenField.getText());
                break;
            }
        }
    }
}
