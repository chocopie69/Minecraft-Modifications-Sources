// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public final class PlayerUtils
{
    private PlayerUtils() {
    }
    
    public static boolean isMoving(final EntityPlayer player) {
        final double xDist = player.posX - player.prevPosX;
        final double zDist = player.posZ - player.prevPosZ;
        return StrictMath.sqrt(xDist * xDist + zDist * zDist) > 1.0E-4;
    }
    
    public static float getMovementDirection(final EntityPlayer player) {
        return RotationUtils.getYawBetween(player.rotationYaw, player.posX, player.posZ, player.prevPosX, player.prevPosZ);
    }
    
    public static boolean checkPing(final EntityPlayer entity) {
        final NetworkPlayerInfo info = Wrapper.getNetHandler().getPlayerInfo(entity.getUniqueID());
        return info != null && info.getResponseTime() == 1;
    }
    
    public static int getBounty(final EntityPlayer player) {
        if (!ServerUtils.isOnHypixel() || HypixelGameUtils.getGameMode() != HypixelGameUtils.GameMode.PIT) {
            return -1;
        }
        final String playerName = player.getGameProfile().getName();
        final String tabListName = player.getDisplayName().getFormattedText();
        System.out.println(tabListName);
        return 0;
    }
    
    public static boolean isTeamMate(final EntityPlayer entity) {
        final String entName = entity.getDisplayName().getFormattedText();
        final String playerName = Wrapper.getPlayer().getDisplayName().getFormattedText();
        return entName.length() >= 2 && playerName.length() >= 2 && entName.startsWith("§") && playerName.startsWith("§") && entName.charAt(1) == playerName.charAt(1);
    }
}
