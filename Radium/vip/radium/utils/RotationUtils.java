// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public final class RotationUtils
{
    public static final double TO_RADS = 57.29577951308232;
    
    private RotationUtils() {
    }
    
    public static float getOldYaw(final Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return getYawBetween(player.prevRotationYaw, player.prevPosX, player.prevPosZ, entity.prevPosX, entity.prevPosZ);
    }
    
    public static float getYawToEntity(final Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return getYawBetween(player.rotationYaw, player.posX, player.posZ, entity.posX, entity.posZ);
    }
    
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }
}
