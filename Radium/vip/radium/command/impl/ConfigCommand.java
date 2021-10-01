// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import java.util.Iterator;
import vip.radium.command.CommandExecutionException;
import vip.radium.config.Config;
import vip.radium.utils.Wrapper;
import vip.radium.RadiumClient;
import vip.radium.command.Command;

public final class ConfigCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "config", "c", "preset" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        if (arguments.length >= 2) {
            final String upperCaseFunction = arguments[1].toUpperCase();
            if (arguments.length == 3) {
                final String s;
                switch (s = upperCaseFunction) {
                    case "LOAD": {
                        if (RadiumClient.getInstance().getConfigManager().loadConfig(arguments[2])) {
                            this.success("loaded", arguments[2]);
                        }
                        else {
                            this.fail("load", arguments[2]);
                        }
                        return;
                    }
                    case "SAVE": {
                        if (RadiumClient.getInstance().getConfigManager().saveConfig(arguments[2])) {
                            this.success("saved", arguments[2]);
                        }
                        else {
                            this.fail("save", arguments[2]);
                        }
                        return;
                    }
                    case "DELETE": {
                        if (RadiumClient.getInstance().getConfigManager().deleteConfig(arguments[2])) {
                            this.success("deleted", arguments[2]);
                        }
                        else {
                            this.fail("delete", arguments[2]);
                        }
                        return;
                    }
                    default:
                        break;
                }
            }
            else if (arguments.length == 2 && upperCaseFunction.equalsIgnoreCase("LIST")) {
                Wrapper.addChatMessage("Available Configs:");
                for (final Config config : RadiumClient.getInstance().getConfigManager().getElements()) {
                    Wrapper.addChatMessage(config.getName());
                }
                return;
            }
        }
        throw new CommandExecutionException(this.getUsage());
    }
    
    private void success(final String type, final String configName) {
        RadiumClient.getInstance().getNotificationManager().add(new Notification(String.format("Successfully %s config: '%s'", type, configName), NotificationType.SUCCESS));
    }
    
    private void fail(final String type, final String configName) {
        RadiumClient.getInstance().getNotificationManager().add(new Notification(String.format("Failed to %s config: '%s'", type, configName), NotificationType.ERROR));
    }
    
    @Override
    public String getUsage() {
        return "config/c/preset <load/save/delete/list> <(optional)config>";
    }
}
