// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import org.lwjgl.opengl.GL11;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner>
{
    @Override
    public void renderTileEntityAt(final TileEntityMobSpawner te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5f, (float)y, (float)z + 0.5f);
        renderMob(te.getSpawnerBaseLogic(), x, y, z, partialTicks);
        GL11.glPopMatrix();
    }
    
    public static void renderMob(final MobSpawnerBaseLogic mobSpawnerLogic, final double posX, final double posY, final double posZ, final float partialTicks) {
        final Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());
        if (entity != null) {
            final float f = 0.4375f;
            GL11.glTranslatef(0.0f, 0.4f, 0.0f);
            GL11.glRotatef((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * partialTicks) * 10.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-30.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(0.0f, -0.4f, 0.0f);
            GL11.glScalef(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0f, 0.0f);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
    }
}
