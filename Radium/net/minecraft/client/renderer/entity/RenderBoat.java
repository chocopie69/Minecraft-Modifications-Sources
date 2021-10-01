// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityBoat;

public class RenderBoat extends Render<EntityBoat>
{
    private static final ResourceLocation boatTextures;
    protected ModelBase modelBoat;
    
    static {
        boatTextures = new ResourceLocation("textures/entity/boat.png");
    }
    
    public RenderBoat(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelBoat = new ModelBoat();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityBoat entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 0.25f, (float)z);
        GL11.glRotatef(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        final float f = entity.getTimeSinceHit() - partialTicks;
        float f2 = entity.getDamageTaken() - partialTicks;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f > 0.0f) {
            GL11.glRotatef(MathHelper.sin(f) * f * f2 / 10.0f * entity.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        final float f3 = 0.75f;
        GL11.glScalef(f3, f3, f3);
        GL11.glScalef(1.0f / f3, 1.0f / f3, 1.0f / f3);
        this.bindEntityTexture(entity);
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBoat entity) {
        return RenderBoat.boatTextures;
    }
}
