// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.network.Packet;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.gui.MinecraftFontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import java.awt.Font;
import vip.radium.utils.render.TTFUtils;
import vip.radium.gui.font.TrueTypeFontRenderer;

public final class Wrapper
{
    private static final TrueTypeFontRenderer fontRenderer;
    private static final TrueTypeFontRenderer mediumFontRenderer;
    private static final TrueTypeFontRenderer smallFontRenderer;
    private static final TrueTypeFontRenderer csgoFontRenderer;
    
    static {
        fontRenderer = new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 21), true, true);
        mediumFontRenderer = new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 20), true, true);
        smallFontRenderer = new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 18), true, true);
        csgoFontRenderer = new TrueTypeFontRenderer(new Font("Tahoma", 1, 11), true, false);
    }
    
    public static TrueTypeFontRenderer getCSGOFontRenderer() {
        return Wrapper.csgoFontRenderer;
    }
    
    public static TrueTypeFontRenderer getSmallFontRenderer() {
        return Wrapper.smallFontRenderer;
    }
    
    public static EntityRenderer getEntityRenderer() {
        return getMinecraft().entityRenderer;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }
    
    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }
    
    public static TrueTypeFontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
    
    public static TrueTypeFontRenderer getMediumFontRenderer() {
        return Wrapper.mediumFontRenderer;
    }
    
    public static MinecraftFontRenderer getMinecraftFontRenderer() {
        return getMinecraft().fontRendererObj;
    }
    
    public static PlayerControllerMP getPlayerController() {
        return getMinecraft().playerController;
    }
    
    public static NetHandlerPlayClient getNetHandler() {
        return getMinecraft().getNetHandler();
    }
    
    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }
    
    public static boolean isInThirdPerson() {
        return getGameSettings().thirdPersonView != 0;
    }
    
    public static ItemStack getStackInSlot(final int index) {
        return getPlayer().inventoryContainer.getSlot(index).getStack();
    }
    
    public static Timer getTimer() {
        return getMinecraft().getTimer();
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getWorld().getBlockState(pos).getBlock();
    }
    
    public static void addChatMessage(final String message) {
        getPlayer().addChatMessage(new ChatComponentText("§8[§CR§8]§7 " + message));
    }
    
    public static GuiScreen getCurrentScreen() {
        return getMinecraft().currentScreen;
    }
    
    public static List<EntityPlayer> getLoadedPlayers() {
        return getWorld().playerEntities;
    }
    
    public static List<EntityLivingBase> getLivingEntities(final Predicate<EntityLivingBase> validator) {
        final List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
        for (final Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase e = (EntityLivingBase)entity;
                if (!validator.test(e)) {
                    continue;
                }
                entities.add(e);
            }
        }
        return entities;
    }
    
    public static void sendPacket(final Packet<?> packet) {
        getNetHandler().sendPacket(packet);
    }
    
    public static void sendPacketDirect(final Packet<?> packet) {
        getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
