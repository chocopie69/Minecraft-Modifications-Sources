// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import vip.radium.command.CommandExecutionException;
import java.util.Iterator;
import java.util.Arrays;
import vip.radium.RadiumClient;
import vip.radium.utils.Wrapper;
import vip.radium.command.Command;

public final class HelpCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "help", "h" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        Wrapper.addChatMessage("Available Commands:");
        for (final Command command : RadiumClient.getInstance().getCommandHandler().getElements()) {
            Wrapper.addChatMessage(String.valueOf(Arrays.toString(command.getAliases())) + ": " + command.getUsage());
        }
    }
    
    @Override
    public String getUsage() {
        return "help/h";
    }
}
