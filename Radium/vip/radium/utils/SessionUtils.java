// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import vip.radium.notification.NotificationManager;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.util.Session;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.gui.alt.impl.GuiAltManager;
import vip.radium.alt.AltManager;
import vip.radium.alt.Alt;
import vip.radium.RadiumClient;
import com.thealtening.auth.service.AlteningServiceType;

public final class SessionUtils
{
    public static void switchService(final AlteningServiceType service) {
        RadiumClient.getInstance().getAltManager().getAlteningAuth().updateService(service);
    }
    
    public static Alt importFromClipboard() {
        return AltManager.parseAlt(StringUtils.getTrimmedClipboardContents());
    }
    
    public static void logIn(final Alt alt) {
        YggdrasilUserAuthentication userAuthentication;
        String username;
        final Notification notification;
        final Object o;
        new Thread(() -> {
            try {
                GuiAltManager.status = "§eLogging in...";
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Logging In", 1000L, NotificationType.INFO));
                userAuthentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                userAuthentication.setUsername(alt.getEmail());
                userAuthentication.setPassword(alt.getPassword());
                userAuthentication.logIn();
                username = userAuthentication.getSelectedProfile().getName();
                alt.setUsername(username);
                alt.setWorking(true);
                Wrapper.getMinecraft().session = new Session(username, UUIDTypeAdapter.fromUUID(userAuthentication.getSelectedProfile().getId()), userAuthentication.getAuthenticatedToken(), userAuthentication.getUserType().getName());
                RadiumClient.getInstance().getNotificationManager();
                new Notification("Login Success", "Logged into account " + username, NotificationType.SUCCESS);
                ((NotificationManager)o).add(notification);
                GuiAltManager.status = "§aLogged in (" + username + ").";
            }
            catch (AuthenticationException ignored) {
                GuiAltManager.status = "§cFailed.";
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Login Failed", 1000L, NotificationType.ERROR));
                alt.setWorking(false);
            }
        }).start();
    }
}
