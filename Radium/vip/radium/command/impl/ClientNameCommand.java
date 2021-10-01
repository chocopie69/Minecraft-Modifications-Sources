// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import vip.radium.command.CommandExecutionException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.render.hud.Hud;
import vip.radium.command.Command;

public final class ClientNameCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "clientName", "rename" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        if (arguments.length >= 2) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 1; i < arguments.length; ++i) {
                sb.append(arguments[i]).append(' ');
            }
            ModuleManager.getInstance(Hud.class).watermarkTextProperty.setValue(sb.toString());
            RadiumClient.getInstance().getNotificationManager().add(new Notification("Updated clientName", 1500L, NotificationType.SUCCESS));
            return;
        }
        throw new CommandExecutionException(this.getUsage());
    }
    
    @Override
    public String getUsage() {
        return "clientName/rename <name>";
    }
}
