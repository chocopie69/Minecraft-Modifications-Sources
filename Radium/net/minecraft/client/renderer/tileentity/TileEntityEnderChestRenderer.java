// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEnderChest;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest>
{
    private static final ResourceLocation ENDER_CHEST_TEXTURE;
    private ModelChest field_147521_c;
    
    static {
        ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    }
    
    public TileEntityEnderChestRenderer() {
        this.field_147521_c = new ModelChest();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityEnderChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        int i = 0;
        if (te.hasWorldObj()) {
            i = te.getBlockMetadata();
        }
        if (destroyStage >= 0) {
            this.bindTexture(TileEntityEnderChestRenderer.DESTROY_STAGES[destroyStage]);
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glScalef(4.0f, 4.0f, 1.0f);
            GL11.glTranslatef(0.0625f, 0.0625f, 0.0625f);
            GL11.glMatrixMode(5888);
        }
        else {
            this.bindTexture(TileEntityEnderChestRenderer.ENDER_CHEST_TEXTURE);
        }
        GL11.glPushMatrix();
        GlStateManager.enableRescaleNormal();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef((float)x, (float)y + 1.0f, (float)z + 1.0f);
        GL11.glScalef(1.0f, -1.0f, -1.0f);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        int j = 0;
        if (i == 2) {
            j = 180;
        }
        if (i == 3) {
            j = 0;
        }
        if (i == 4) {
            j = 90;
        }
        if (i == 5) {
            j = -90;
        }
        GL11.glRotatef((float)j, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0f - f;
        f = 1.0f - f * f * f;
        this.field_147521_c.chestLid.rotateAngleX = -(f * 3.1415927f / 2.0f);
        this.field_147521_c.renderAll();
        GlStateManager.disableRescaleNormal();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (destroyStage >= 0) {
            GL11.glMatrixMode(5890);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
        }
    }
}
