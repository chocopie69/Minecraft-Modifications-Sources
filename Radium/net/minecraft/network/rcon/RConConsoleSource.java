// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.rcon;

import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.command.ICommandSender;

public class RConConsoleSource implements ICommandSender
{
    private static final RConConsoleSource field_70010_a;
    private StringBuffer field_70009_b;
    
    static {
        field_70010_a = new RConConsoleSource();
    }
    
    public RConConsoleSource() {
        this.field_70009_b = new StringBuffer();
    }
    
    @Override
    public String getCommandSenderName() {
        return "Rcon";
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getCommandSenderName());
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
        this.field_70009_b.append(component.getUnformattedText());
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return true;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(0, 0, 0);
    }
    
    @Override
    public Vec3 getPositionVector() {
        return new Vec3(0.0, 0.0, 0.0);
    }
    
    @Override
    public World getEntityWorld() {
        return MinecraftServer.getServer().getEntityWorld();
    }
    
    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return true;
    }
    
    @Override
    public void setCommandStat(final CommandResultStats.Type type, final int amount) {
    }
}
