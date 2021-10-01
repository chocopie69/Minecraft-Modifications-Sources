// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import vip.radium.command.CommandExecutionException;
import vip.radium.command.Command;

public final class FriendCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "friend", "f" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        if (arguments.length > 1) {
            String username;
            final String usernameOrAction = username = arguments[1].toUpperCase();
            Label_0225: {
                final String s;
                switch (s = usernameOrAction) {
                    case "REMOVE": {
                        break Label_0225;
                    }
                    case "ADD": {
                        if (arguments.length <= 2) {
                            throw new CommandExecutionException(this.getUsage());
                        }
                        username = arguments[2];
                        break;
                    }
                    case "DEL": {
                        break Label_0225;
                    }
                    default:
                        break;
                }
                String alias;
                if (arguments.length >= 4) {
                    alias = arguments[3];
                }
                else {
                    alias = null;
                }
                final String friendAdded = RadiumClient.getInstance().getFriendManager().friend(username, alias);
                if (friendAdded != null) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Friend Added", String.format("%s is now friended.", friendAdded), 1500L, NotificationType.SUCCESS));
                }
                else {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification(String.format("%s is not a player.", username), 1000L, NotificationType.ERROR));
                }
                return;
            }
            if (arguments.length == 3) {
                username = arguments[2];
                final String friendRemoved = RadiumClient.getInstance().getFriendManager().unfriend(username);
                if (friendRemoved != null) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Friend Removed", String.format("%s is no longer friended.", friendRemoved), 1500L, NotificationType.SUCCESS));
                }
                else {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification(String.format("%s is not friended.", username), 1000L, NotificationType.ERROR));
                }
            }
        }
        throw new CommandExecutionException(this.getUsage());
    }
    
    @Override
    public String getUsage() {
        return "friend/f <(optional)add/remove/del> <username> <(optional)alias>";
    }
}
