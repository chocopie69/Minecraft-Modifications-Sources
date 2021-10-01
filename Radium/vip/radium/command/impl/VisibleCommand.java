// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import java.util.Optional;
import vip.radium.command.CommandExecutionException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.module.Module;
import vip.radium.RadiumClient;
import vip.radium.command.Command;

public final class VisibleCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "visible", "v" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        if (arguments.length == 2) {
            final String moduleName = arguments[1];
            final Optional<Module> module = RadiumClient.getInstance().getModuleManager().getModule(moduleName);
            if (module.isPresent()) {
                final Module m = module.get();
                m.setHidden(!m.isHidden());
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Set '" + m.getLabel() + "' to " + (m.isHidden() ? "hidden" : "visible"), NotificationType.SUCCESS));
                return;
            }
        }
        throw new CommandExecutionException(this.getUsage());
    }
    
    @Override
    public String getUsage() {
        return "visible/v <module>";
    }
}
