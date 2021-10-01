// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.projectile.EntityFireball;

public class RenderFireball extends Render<EntityFireball>
{
    private float scale;
    
    public RenderFireball(final RenderManager renderManagerIn, final float scaleIn) {
        super(renderManagerIn);
        this.scale = scaleIn;
    }
    
    @Override
    public void doRender(final EntityFireball entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GL11.glScalef(this.scale, this.scale, this.scale);
        final TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMaxU();
        final float f3 = textureatlassprite.getMinV();
        final float f4 = textureatlassprite.getMaxV();
        final float f5 = 1.0f;
        final float f6 = 0.5f;
        final float f7 = 0.25f;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5, -0.25, 0.0).tex(f, f4).func_181663_c(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, -0.25, 0.0).tex(f2, f4).func_181663_c(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, 0.75, 0.0).tex(f2, f3).func_181663_c(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(-0.5, 0.75, 0.0).tex(f, f3).func_181663_c(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GL11.glPopMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityFireball entity) {
        return TextureMap.locationBlocksTexture;
    }
}
