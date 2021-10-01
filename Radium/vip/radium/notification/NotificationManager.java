// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.notification;

import vip.radium.utils.render.LockedResolution;
import net.minecraft.client.gui.ScaledResolution;
import vip.radium.RadiumClient;
import java.util.List;
import java.util.ArrayList;
import vip.radium.utils.handler.Manager;

public final class NotificationManager extends Manager<Notification>
{
    public NotificationManager() {
        super(new ArrayList());
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }
    
    public void render(final ScaledResolution scaledResolution, final LockedResolution lockedResolution, final boolean inGame, final int yOffset) {
        final List<Notification> notifications = this.getElements();
        Notification remove = null;
        for (int i = 0; i < notifications.size(); ++i) {
            final Notification notification = notifications.get(i);
            if (notification.isDead()) {
                remove = notification;
            }
            else {
                notification.render(lockedResolution, scaledResolution, i + 1, yOffset);
            }
        }
        if (remove != null) {
            this.getElements().remove(remove);
        }
    }
    
    public void add(final Notification notification) {
        this.getElements().add(notification);
    }
}
