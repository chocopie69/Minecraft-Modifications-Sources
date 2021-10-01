// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import vip.radium.module.impl.render.ChestESP;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
{
    private static final ResourceLocation textureTrappedDouble;
    private static final ResourceLocation textureChristmasDouble;
    private static final ResourceLocation textureNormalDouble;
    private static final ResourceLocation textureTrapped;
    private static final ResourceLocation textureChristmas;
    private static final ResourceLocation textureNormal;
    private final ModelChest simpleChest;
    private final ModelChest largeChest;
    private boolean isChristams;
    
    static {
        textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
        textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
        textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
        textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
        textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
        textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    }
    
    public TileEntityChestRenderer() {
        this.simpleChest = new ModelChest();
        this.largeChest = new ModelLargeChest();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        int i;
        if (!te.hasWorldObj()) {
            i = 0;
        }
        else {
            final Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    GL11.glScalef(4.0f, 4.0f, 1.0f);
                    GL11.glTranslatef(0.0625f, 0.0625f, 0.0625f);
                    GL11.glMatrixMode(5888);
                }
                else if (this.isChristams) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmas);
                }
                else if (te.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrapped);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormal);
                }
            }
            else {
                modelchest = this.largeChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    GL11.glScalef(8.0f, 4.0f, 1.0f);
                    GL11.glTranslatef(0.0625f, 0.0625f, 0.0625f);
                    GL11.glMatrixMode(5888);
                }
                else if (this.isChristams) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmasDouble);
                }
                else if (te.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrappedDouble);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormalDouble);
                }
            }
            GL11.glPushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
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
            if (i == 2 && te.adjacentChestXPos != null) {
                GL11.glTranslatef(1.0f, 0.0f, 0.0f);
            }
            if (i == 5 && te.adjacentChestZPos != null) {
                GL11.glTranslatef(0.0f, 0.0f, -1.0f);
            }
            GL11.glRotatef((float)j, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null) {
                final float f2 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (f2 > f) {
                    f = f2;
                }
            }
            if (te.adjacentChestXNeg != null) {
                final float f3 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (f3 > f) {
                    f = f3;
                }
            }
            f = 1.0f - f;
            f = 1.0f - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * 3.1415927f / 2.0f);
            final ChestESP instance = ChestESP.getInstance();
            if (instance.isEnabled() && instance.isChams()) {
                final boolean visibleFlat = instance.visibleFlatProperty.getValue();
                final boolean occludedFlat = instance.occludedFlatProperty.getValue();
                final int visibleColor = instance.visibleColorProperty.getValue();
                final int occludedColor = instance.occludedColorProperty.getValue();
                ChestESP.preOccludedRender(occludedColor, occludedFlat);
                modelchest.renderAll();
                ChestESP.preVisibleRender(visibleColor, visibleFlat, occludedFlat);
                modelchest.renderAll();
                ChestESP.postRender(visibleFlat);
            }
            else {
                modelchest.renderAll();
            }
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
}
