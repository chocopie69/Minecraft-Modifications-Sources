// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLeashKnot;

public class RenderLeashKnot extends Render<EntityLeashKnot>
{
    private static final ResourceLocation leashKnotTextures;
    private ModelLeashKnot leashKnotModel;
    
    static {
        leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
    }
    
    public RenderLeashKnot(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.leashKnotModel = new ModelLeashKnot();
    }
    
    @Override
    public void doRender(final EntityLeashKnot entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GL11.glPushMatrix();
        GlStateManager.disableCull();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        final float f = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        this.leashKnotModel.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, f);
        GL11.glPopMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLeashKnot entity) {
        return RenderLeashKnot.leashKnotTextures;
    }
}
