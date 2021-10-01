// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.alt.impl;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import com.thealtening.api.TheAlteningException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import vip.radium.alt.Alt;
import vip.radium.utils.SessionUtils;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.gui.GuiButton;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import vip.radium.gui.alt.GuiAltScreen;

public final class GuiDirectLogin extends GuiAltScreen
{
    private GuiTextField emailField;
    private GuiTextField passwordField;
    
    public GuiDirectLogin(final GuiScreen parent) {
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
        this.emailField = new GuiTextField(3, Wrapper.getMinecraftFontRenderer(), "Email", middleX, middleY, 200, 18);
        this.passwordField = new GuiTextField(4, Wrapper.getMinecraftFontRenderer(), "Password", middleX, middleY + 22, 200, 18);
        this.textFields.add(this.emailField);
        this.textFields.add(this.passwordField);
        this.buttonList.add(new GuiButton(1, middleX, middleY + 44, "Login"));
        this.buttonList.add(new GuiButton(2, middleX, middleY + 66, "Login from clipboard"));
        this.buttonList.add(new GuiButton(0, middleX, middleY + 88, "Back"));
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
                final String email = this.emailField.getText();
                final String pw = this.passwordField.getText();
                if (email.contains("@alt.com")) {
                    SessionUtils.switchService(AlteningServiceType.THEALTENING);
                    try {
                        SessionUtils.logIn(new Alt(this.emailField.getText(), "A"));
                    }
                    catch (TheAlteningException | JsonSyntaxException ex2) {
                        final RuntimeException ex;
                        final RuntimeException ignored = ex;
                        RadiumClient.getInstance().getNotificationManager().add(new Notification("TheAltening Exception", "Failed to generate account", 1500L, NotificationType.ERROR));
                    }
                    break;
                }
                if (email.length() >= 5 && pw.length() > 0) {
                    SessionUtils.switchService(AlteningServiceType.MOJANG);
                    SessionUtils.logIn(new Alt(email, pw));
                    break;
                }
                this.loginError();
                break;
            }
            case 2: {
                final Alt alt = SessionUtils.importFromClipboard();
                if (alt != null) {
                    if (alt.getEmail().contains("@alt.com")) {
                        SessionUtils.switchService(AlteningServiceType.THEALTENING);
                    }
                    else {
                        SessionUtils.switchService(AlteningServiceType.MOJANG);
                    }
                    SessionUtils.logIn(alt);
                    break;
                }
                this.loginError();
                break;
            }
        }
    }
    
    private void loginError() {
        RadiumClient.getInstance().getNotificationManager().add(new Notification("Login Error", "Enter email and password", 1000L, NotificationType.ERROR));
    }
}
