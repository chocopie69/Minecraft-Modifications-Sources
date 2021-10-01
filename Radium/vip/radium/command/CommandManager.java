// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command;

import java.util.Iterator;
import vip.radium.RadiumClient;
import vip.radium.utils.Wrapper;
import java.util.Arrays;
import vip.radium.command.impl.FriendCommand;
import vip.radium.command.impl.NameCommand;
import vip.radium.command.impl.VisibleCommand;
import vip.radium.command.impl.ClientNameCommand;
import vip.radium.command.impl.ConfigCommand;
import vip.radium.command.impl.ToggleCommand;
import vip.radium.command.impl.HelpCommand;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.SendMessageEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.handler.Manager;

public final class CommandManager extends Manager<Command>
{
    private static final String PREFIX = ".";
    private static final String HELP_MESSAGE = "Try '.help'";
    @EventLink
    public final Listener<SendMessageEvent> onSendMessageEvent;
    
    public CommandManager() {
        super(Arrays.asList(new HelpCommand(), new ToggleCommand(), new ConfigCommand(), new ClientNameCommand(), new VisibleCommand(), new NameCommand(), new FriendCommand()));
        final String message;
        final String s;
        String removedPrefix;
        String[] arguments;
        final Iterator<Command> iterator;
        Command command;
        final Object o;
        int length;
        int i = 0;
        final String[] array;
        String alias;
        this.onSendMessageEvent = (event -> {
            message = event.getMessage();
            if (s.startsWith(".")) {
                event.setCancelled();
                removedPrefix = message.substring(1);
                arguments = removedPrefix.split(" ");
                if (!removedPrefix.isEmpty() && arguments.length > 0) {
                    this.getElements().iterator();
                    while (iterator.hasNext()) {
                        command = iterator.next();
                        command.getAliases();
                        length = o.length;
                        while (i < length) {
                            alias = array[i];
                            if (alias.equalsIgnoreCase(arguments[0])) {
                                try {
                                    command.execute(arguments);
                                }
                                catch (CommandExecutionException e) {
                                    Wrapper.addChatMessage("Invalid command syntax. Hint: " + e.getMessage());
                                }
                                return;
                            }
                            else {
                                ++i;
                            }
                        }
                    }
                    Wrapper.addChatMessage("'" + arguments[0] + "' is not a command. " + "Try '.help'");
                }
                else {
                    Wrapper.addChatMessage("No arguments were supplied. Try '.help'");
                }
            }
            return;
        });
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }
}
