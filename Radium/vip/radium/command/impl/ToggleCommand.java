// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command.impl;

import java.util.Iterator;
import vip.radium.command.CommandExecutionException;
import vip.radium.utils.Wrapper;
import vip.radium.module.Module;
import vip.radium.RadiumClient;
import vip.radium.command.Command;

public final class ToggleCommand implements Command
{
    @Override
    public String[] getAliases() {
        return new String[] { "toggle", "t" };
    }
    
    @Override
    public void execute(final String[] arguments) throws CommandExecutionException {
        if (arguments.length == 2) {
            final String moduleName = arguments[1];
            for (final Module module : RadiumClient.getInstance().getModuleManager().getModules()) {
                if (module.getLabel().replaceAll(" ", "").equalsIgnoreCase(moduleName)) {
                    module.toggle();
                    Wrapper.addChatMessage("'" + module.getLabel() + "' has been " + (module.isEnabled() ? "§Aenabled§7." : "§Cdisabled§7."));
                    return;
                }
            }
        }
        throw new CommandExecutionException(this.getUsage());
    }
    
    @Override
    public String getUsage() {
        return "toggle/t <module>";
    }
}
