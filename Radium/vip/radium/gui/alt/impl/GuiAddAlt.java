// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.alt.impl;

import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import java.io.IOException;
import vip.radium.utils.SessionUtils;
import com.thealtening.auth.service.AlteningServiceType;
import vip.radium.alt.Alt;
import net.minecraft.client.gui.GuiButton;
import vip.radium.utils.Wrapper;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import vip.radium.gui.alt.GuiAltScreen;

public final class GuiAddAlt extends GuiAltScreen
{
    private GuiTextField emailField;
    private GuiTextField passwordField;
    
    public GuiAddAlt(final GuiScreen parent) {
        super(parent);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
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
        this.buttonList.add(new GuiButton(1, middleX, middleY + 44, "Add"));
        this.buttonList.add(new GuiButton(2, middleX, middleY + 66, "Login and Add"));
        this.buttonList.add(new GuiButton(0, middleX, middleY + 88, "Back"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final String email = this.emailField.getText();
        final String pw = this.passwordField.getText();
        final Alt alt = new Alt(email, pw);
        switch (button.id) {
            case 0: {
                Wrapper.getMinecraft().displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                if (email.length() >= 5 && pw.length() > 0) {
                    this.add(alt);
                    break;
                }
                this.loginError();
                break;
            }
            case 2: {
                if (email.length() >= 5 && pw.length() > 0) {
                    this.add(alt);
                    SessionUtils.switchService(AlteningServiceType.MOJANG);
                    SessionUtils.logIn(alt);
                    break;
                }
                this.loginError();
                break;
            }
        }
    }
    
    private void add(final Alt alt) {
        RadiumClient.getInstance().getAltManager().addAlt(alt);
    }
    
    private void loginError() {
        RadiumClient.getInstance().getNotificationManager().add(new Notification("Login Error", "Enter email and password", 1000L, NotificationType.ERROR));
    }
}
