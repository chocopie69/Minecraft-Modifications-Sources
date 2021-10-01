// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.command;

public interface Command
{
    String[] getAliases();
    
    void execute(final String[] p0) throws CommandExecutionException;
    
    String getUsage();
}
