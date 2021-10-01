// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import java.util.Iterator;
import net.optifine.EmissiveTextures;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.MinecraftFontRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import vip.radium.RadiumClient;
import vip.radium.event.impl.render.RenderNameTagEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.module.impl.render.Chams;
import net.optifine.entity.model.CustomEntityModels;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.optifine.reflect.Reflector;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GLAllocation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import java.util.List;
import java.nio.FloatBuffer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.EntityLivingBase;

public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    private static final Logger logger;
    private static final DynamicTexture field_177096_e;
    public static float NAME_TAG_RANGE;
    public static float NAME_TAG_RANGE_SNEAK;
    private static boolean unsetPolyOffset;
    public ModelBase mainModel;
    public EntityLivingBase renderEntity;
    public float renderLimbSwing;
    public float renderLimbSwingAmount;
    public float renderAgeInTicks;
    public float renderHeadYaw;
    public float renderHeadPitch;
    public float renderScaleFactor;
    public float renderPartialTicks;
    protected FloatBuffer brightnessBuffer;
    protected List<LayerRenderer<T>> layerRenderers;
    protected boolean renderOutlines;
    private boolean renderLayersPushMatrix;
    public static final boolean animateModelLiving;
    
    static {
        logger = LogManager.getLogger();
        field_177096_e = new DynamicTexture(16, 16);
        RendererLivingEntity.NAME_TAG_RANGE = 64.0f;
        RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 32.0f;
        final int[] aint = RendererLivingEntity.field_177096_e.getTextureData();
        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }
        RendererLivingEntity.field_177096_e.updateDynamicTexture();
        animateModelLiving = Boolean.getBoolean("animate.model.living");
    }
    
    public RendererLivingEntity(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
        this.brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
        this.layerRenderers = (List<LayerRenderer<T>>)Lists.newArrayList();
        this.renderOutlines = false;
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, x, y, z)) {
            if (RendererLivingEntity.animateModelLiving) {
                entity.limbSwingAmount = 1.0f;
            }
            GL11.glPushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            this.mainModel.isRiding = entity.isRiding();
            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = (entity.isRiding() && entity.ridingEntity != null && Reflector.callBoolean(entity.ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0]));
            }
            this.mainModel.isChild = entity.isChild();
            try {
                EntityPlayerSP player = null;
                final boolean showServerSideRotations = entity instanceof EntityPlayerSP && (player = (EntityPlayerSP)entity).currentEvent.isRotating();
                float headYaw;
                float bodyYaw;
                if (showServerSideRotations) {
                    final UpdatePositionEvent event = player.currentEvent;
                    bodyYaw = (headYaw = RenderingUtils.interpolate(event.getPrevYaw(), event.getYaw(), partialTicks));
                }
                else {
                    bodyYaw = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                    headYaw = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                }
                float yawDif = headYaw - bodyYaw;
                if (this.mainModel.isRiding && entity.ridingEntity instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
                    bodyYaw = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    yawDif = headYaw - bodyYaw;
                    float f3 = MathHelper.wrapAngleTo180_float(yawDif);
                    if (f3 < -85.0f) {
                        f3 = -85.0f;
                    }
                    if (f3 >= 85.0f) {
                        f3 = 85.0f;
                    }
                    bodyYaw = headYaw - f3;
                    if (f3 * f3 > 2500.0f) {
                        bodyYaw += f3 * 0.2f;
                    }
                    yawDif = headYaw - bodyYaw;
                }
                float pitch;
                if (showServerSideRotations) {
                    final UpdatePositionEvent event2 = player.currentEvent;
                    pitch = RenderingUtils.interpolate(event2.getPrevPitch(), event2.getPitch(), partialTicks);
                }
                else {
                    pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                }
                this.renderLivingAt(entity, x, y, z);
                final float f4 = this.handleRotationFloat(entity, partialTicks);
                this.rotateCorpse(entity, f4, bodyYaw, partialTicks);
                GlStateManager.enableRescaleNormal();
                GL11.glScalef(-1.0f, -1.0f, 1.0f);
                this.preRenderCallback(entity, partialTicks);
                final float f5 = 0.0625f;
                GL11.glTranslatef(0.0f, -1.5078125f, 0.0f);
                float f6 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                float f7 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                if (entity.isChild()) {
                    f7 *= 3.0f;
                }
                if (f6 > 1.0f) {
                    f6 = 1.0f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f7, f6, partialTicks);
                this.mainModel.setRotationAngles(f7, f6, f4, yawDif, pitch, 0.0625f, entity);
                if (CustomEntityModels.isActive()) {
                    this.renderEntity = entity;
                    this.renderLimbSwing = f7;
                    this.renderLimbSwingAmount = f6;
                    this.renderAgeInTicks = f4;
                    this.renderHeadYaw = yawDif;
                    this.renderHeadPitch = pitch;
                    this.renderScaleFactor = f5;
                    this.renderPartialTicks = partialTicks;
                }
                if (this.renderOutlines) {
                    GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                    this.renderModel(entity, f7, f6, f4, yawDif, pitch, 0.0625f);
                }
                else {
                    final boolean enabled = Chams.isChamsEnabled();
                    final boolean flag = (!enabled || Chams.isRenderHurtEffect()) && this.setDoRenderBrightness(entity, partialTicks, enabled);
                    this.renderModel(entity, f7, f6, f4, yawDif, pitch, f5);
                    if (RendererLivingEntity.unsetPolyOffset) {
                        GL11.glPolygonOffset(0.0f, 1000000.0f);
                        GL11.glDisable(32823);
                        RendererLivingEntity.unsetPolyOffset = false;
                    }
                    if (flag) {
                        this.unsetBrightness();
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f7, f6, partialTicks, f4, yawDif, pitch, 0.0625f);
                    }
                }
                if (CustomEntityModels.isActive()) {
                    this.renderEntity = null;
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception exception) {
                RendererLivingEntity.logger.error("Couldn't render entity", (Throwable)exception);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GL11.glPopMatrix();
            if (!this.renderOutlines) {
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, x, y, z);
            }
        }
    }
    
    public void renderName(final T entity, final double x, final double y, final double z) {
        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, entity, this, x, y, z)) {
            if (this.canRenderName(entity)) {
                final RenderNameTagEvent event = new RenderNameTagEvent(entity);
                RadiumClient.getInstance().getEventBus().post(event);
                if (!event.isCancelled()) {
                    final double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
                    final float f = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
                    if (d0 < f * f) {
                        final String s = entity.getDisplayName().getFormattedText();
                        final float f2 = 0.02666667f;
                        GlStateManager.alphaFunc(516, 0.1f);
                        if (entity.isSneaking()) {
                            final MinecraftFontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                            GL11.glPushMatrix();
                            GL11.glTranslatef((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                            GL11.glRotatef(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                            GL11.glRotatef(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                            GL11.glScalef(-0.02666667f, -0.02666667f, 0.02666667f);
                            GL11.glTranslatef(0.0f, 9.374999f, 0.0f);
                            GlStateManager.disableLighting();
                            GlStateManager.depthMask(false);
                            GlStateManager.enableBlend();
                            GlStateManager.disableTexture2D();
                            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                            final int i = fontrenderer.getStringWidth(s) / 2;
                            final Tessellator tessellator = Tessellator.getInstance();
                            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                            worldrenderer.pos(-i - 1, -1.0, 0.0).color4f(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                            worldrenderer.pos(-i - 1, 8.0, 0.0).color4f(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                            worldrenderer.pos(i + 1, 8.0, 0.0).color4f(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                            worldrenderer.pos(i + 1, -1.0, 0.0).color4f(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                            tessellator.draw();
                            GlStateManager.enableTexture2D();
                            GlStateManager.depthMask(true);
                            fontrenderer.drawString(s, (float)(-fontrenderer.getStringWidth(s) / 2), 0.0f, 553648127);
                            GlStateManager.enableLighting();
                            GlStateManager.disableBlend();
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            GL11.glPopMatrix();
                        }
                        else {
                            this.renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (entity.height / 2.0f) : 0.0), z, s, 0.02666667f, d0);
                        }
                    }
                }
            }
            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, entity, this, x, y, z);
            }
        }
    }
    
    @Override
    protected boolean canRenderName(final T entity) {
        final EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        if (entity instanceof EntityPlayer && entity != entityplayersp) {
            final Team team = entity.getTeam();
            final Team team2 = entityplayersp.getTeam();
            if (team != null) {
                final Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
                switch (team$enumvisible) {
                    case ALWAYS: {
                        return true;
                    }
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return team2 == null || team.isSameTeam(team2);
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return team2 == null || !team.isSameTeam(team2);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }
    
    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(final U layer) {
        return this.layerRenderers.add((LayerRenderer<T>)layer);
    }
    
    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(final U layer) {
        return this.layerRenderers.remove(layer);
    }
    
    public ModelBase getMainModel() {
        return this.mainModel;
    }
    
    protected float interpolateRotation(final float par1, final float par2, final float par3) {
        float f;
        for (f = par2 - par1; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return par1 + par3 * f;
    }
    
    public void transformHeldFull3DItemLayer() {
    }
    
    protected boolean setScoreTeamColor(final T entityLivingBaseIn) {
        int i = 16777215;
        if (entityLivingBaseIn instanceof EntityPlayer) {
            final ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityLivingBaseIn.getTeam();
            if (scoreplayerteam != null) {
                final String s = MinecraftFontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
                if (s.length() >= 2) {
                    i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
                }
            }
        }
        final float f1 = (i >> 16 & 0xFF) / 255.0f;
        final float f2 = (i >> 8 & 0xFF) / 255.0f;
        final float f3 = (i & 0xFF) / 255.0f;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glColor4f(f1, f2, f3, 1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected void renderModel(final T entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        final boolean flag = !entitylivingbaseIn.isInvisible();
        final boolean flag2 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
        if (flag || flag2) {
            final Chams instance = Chams.getInstance();
            final boolean colorChams = instance.isColorChams();
            final boolean chamsRendering = instance.isEnabled() && Chams.isValid(entitylivingbaseIn);
            if ((!chamsRendering || !colorChams) && !this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }
            if (flag2) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.15f);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(770, 771);
            }
            if (chamsRendering) {
                if (colorChams) {
                    final boolean visibleFlat = instance.visibleFlatProperty.getValue();
                    final boolean occludedFlat = instance.occludedFlatProperty.getValue();
                    final long currentMillis = System.currentTimeMillis();
                    final int entityId = entitylivingbaseIn.getEntityId();
                    int visibleColor = 0;
                    switch (instance.visibleColorModeProperty.getValue()) {
                        case RAINBOW: {
                            visibleColor = RenderingUtils.getRainbowFromEntity(currentMillis, 6000, entityId, true, instance.visibleAlphaProperty.getValue().floatValue());
                            break;
                        }
                        case COLOR: {
                            visibleColor = instance.visibleColorProperty.getValue();
                            break;
                        }
                        case PULSING: {
                            visibleColor = RenderingUtils.fadeBetween(instance.visibleColorProperty.getValue(), instance.secondVisibleColorProperty.getValue(), System.currentTimeMillis() % 3000L / 1500.0f);
                            break;
                        }
                        default: {
                            visibleColor = 0;
                            break;
                        }
                    }
                    int occludedColor = 0;
                    switch (instance.occludedColorModeProperty.getValue()) {
                        case RAINBOW: {
                            occludedColor = RenderingUtils.getRainbowFromEntity(currentMillis, 6000, entityId, false, instance.occludedAlphaProperty.getValue().floatValue());
                            break;
                        }
                        case COLOR: {
                            occludedColor = instance.occludedColorProperty.getValue();
                            break;
                        }
                        case PULSING: {
                            occludedColor = RenderingUtils.fadeBetween(instance.occludedColorProperty.getValue(), instance.secondOccludedColorProperty.getValue(), System.currentTimeMillis() % 3000L / 1500.0f);
                            break;
                        }
                        default: {
                            occludedColor = 0;
                            break;
                        }
                    }
                    Chams.preRenderOccluded(occludedColor, occludedFlat);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                    Chams.preRenderVisible(visibleColor, visibleFlat, occludedFlat);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                    Chams.postRender(visibleFlat);
                }
                else {
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
                }
            }
            else {
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            }
            if (flag2) {
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        }
    }
    
    protected boolean setDoRenderBrightness(final T entityLivingBaseIn, final float partialTicks, final boolean customHitColor) {
        return this.setBrightness(entityLivingBaseIn, partialTicks, true, customHitColor);
    }
    
    protected boolean setBrightness(final T entitylivingbaseIn, final float partialTicks, final boolean combineTextures, final boolean customHitColor) {
        final float f = entitylivingbaseIn.getBrightness(partialTicks);
        final int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        final boolean flag = (i >> 24 & 0xFF) > 0;
        final boolean flag2 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;
        if (!flag && !flag2) {
            return false;
        }
        if (!flag && !combineTextures) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        this.brightnessBuffer.position(0);
        if (flag2) {
            if (customHitColor) {
                final Chams.HurtEffect hurtEffect = Chams.getHurtEffect();
                final float red = hurtEffect.getRed();
                final float green = hurtEffect.getGreen();
                final float blue = hurtEffect.getBlue();
                final float alpha = hurtEffect.getAlpha();
                this.brightnessBuffer.put(red);
                this.brightnessBuffer.put(green);
                this.brightnessBuffer.put(blue);
                this.brightnessBuffer.put(alpha);
                if (Config.isShaders()) {
                    Shaders.setEntityColor(red, green, blue, alpha);
                }
            }
            else {
                this.brightnessBuffer.put(1.0f);
                this.brightnessBuffer.put(0.0f);
                this.brightnessBuffer.put(0.0f);
                this.brightnessBuffer.put(0.3f);
                if (Config.isShaders()) {
                    Shaders.setEntityColor(1.0f, 0.0f, 0.0f, 0.3f);
                }
            }
        }
        else {
            final float f2 = (i >> 24 & 0xFF) / 255.0f;
            final float f3 = (i >> 16 & 0xFF) / 255.0f;
            final float f4 = (i >> 8 & 0xFF) / 255.0f;
            final float f5 = (i & 0xFF) / 255.0f;
            this.brightnessBuffer.put(f3);
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(f5);
            this.brightnessBuffer.put(1.0f - f2);
            if (Config.isShaders()) {
                Shaders.setEntityColor(f3, f4, f5, 1.0f - f2);
            }
        }
        this.brightnessBuffer.flip();
        GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(RendererLivingEntity.field_177096_e.getGlTextureId());
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetBrightness() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
    
    protected void renderLivingAt(final T entityLivingBaseIn, final double x, final double y, final double z) {
        GL11.glTranslatef((float)x, (float)y, (float)z);
    }
    
    protected void rotateCorpse(final T bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        GL11.glRotatef(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        if (bat.deathTime > 0) {
            float f = (bat.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f = MathHelper.sqrt_float(f);
            if (f > 1.0f) {
                f = 1.0f;
            }
            GL11.glRotatef(f * this.getDeathMaxRotation(bat), 0.0f, 0.0f, 1.0f);
        }
        else {
            final String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getCommandSenderName());
            if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer)bat).isWearing(EnumPlayerModelParts.CAPE))) {
                GL11.glTranslatef(0.0f, bat.height + 0.1f, 0.0f);
                GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    protected float getSwingProgress(final T livingBase, final float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }
    
    protected float handleRotationFloat(final T livingBase, final float partialTicks) {
        return livingBase.ticksExisted + partialTicks;
    }
    
    protected void renderLayers(final T entitylivingbaseIn, final float p_177093_2_, final float p_177093_3_, final float partialTicks, final float p_177093_5_, final float p_177093_6_, final float p_177093_7_, final float p_177093_8_) {
        for (final LayerRenderer<T> layerrenderer : this.layerRenderers) {
            final boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures(), false);
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            if (this.renderLayersPushMatrix) {
                GL11.glPushMatrix();
            }
            layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
            if (this.renderLayersPushMatrix) {
                GL11.glPopMatrix();
            }
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    this.renderLayersPushMatrix = true;
                    EmissiveTextures.beginRenderEmissive();
                    GL11.glPushMatrix();
                    layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
                    GL11.glPopMatrix();
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
            if (flag) {
                this.unsetBrightness();
            }
        }
    }
    
    protected float getDeathMaxRotation(final T entityLivingBaseIn) {
        return 90.0f;
    }
    
    protected int getColorMultiplier(final T entitylivingbaseIn, final float lightBrightness, final float partialTickTime) {
        return 0;
    }
    
    protected void preRenderCallback(final T entitylivingbaseIn, final float partialTickTime) {
    }
    
    public void setRenderOutlines(final boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }
    
    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }
}
