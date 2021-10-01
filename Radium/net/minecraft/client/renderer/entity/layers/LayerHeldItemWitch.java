// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWitch;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;

public class LayerHeldItemWitch implements LayerRenderer<EntityWitch>
{
    private final RenderWitch witchRenderer;
    
    public LayerHeldItemWitch(final RenderWitch witchRendererIn) {
        this.witchRenderer = witchRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityWitch entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        final ItemStack itemstack = entitylivingbaseIn.getHeldItem();
        if (itemstack != null) {
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            if (this.witchRenderer.getMainModel().isChild) {
                GL11.glTranslatef(0.0f, 0.625f, 0.0f);
                GL11.glRotatef(-20.0f, -1.0f, 0.0f, 0.0f);
                final float f = 0.5f;
                GL11.glScalef(f, f, f);
            }
            ((ModelWitch)this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625f);
            GL11.glTranslatef(-0.0625f, 0.53125f, 0.21875f);
            final Item item = itemstack.getItem();
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && minecraft.getBlockRendererDispatcher().isRenderTypeChest(Block.getBlockFromItem(item), itemstack.getMetadata())) {
                GL11.glTranslatef(0.0f, 0.0625f, -0.25f);
                GL11.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(-5.0f, 0.0f, 1.0f, 0.0f);
                final float f2 = 0.375f;
                GL11.glScalef(f2, -f2, f2);
            }
            else if (item == Items.bow) {
                GL11.glTranslatef(0.0f, 0.125f, -0.125f);
                GL11.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
                final float f3 = 0.625f;
                GL11.glScalef(f3, -f3, f3);
                GL11.glRotatef(-100.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(-20.0f, 0.0f, 1.0f, 0.0f);
            }
            else if (item.isFull3D()) {
                if (item.shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef(0.0f, -0.0625f, 0.0f);
                }
                this.witchRenderer.transformHeldFull3DItemLayer();
                GL11.glTranslatef(0.0625f, -0.125f, 0.0f);
                final float f4 = 0.625f;
                GL11.glScalef(f4, -f4, f4);
                GL11.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
            }
            else {
                GL11.glTranslatef(0.1875f, 0.1875f, 0.0f);
                final float f5 = 0.875f;
                GL11.glScalef(f5, f5, f5);
                GL11.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(-60.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(-30.0f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glRotatef(-15.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(40.0f, 0.0f, 0.0f, 1.0f);
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
