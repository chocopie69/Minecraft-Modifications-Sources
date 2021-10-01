// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.optifine.reflect.Reflector;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import vip.radium.module.impl.render.Animations;
import vip.radium.module.impl.render.Chams;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.optifine.DynamicLights;
import net.minecraft.util.BlockPos;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.item.Item;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND;
    private static final ResourceLocation RES_UNDERWATER_OVERLAY;
    private final Minecraft mc;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    public float angle;
    private ItemStack itemToRender;
    public float equippedProgress;
    public float prevEquippedProgress;
    private int equippedItemSlot;
    
    static {
        RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
        RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    }
    
    public ItemRenderer(final Minecraft mcIn) {
        this.equippedItemSlot = -1;
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }
    
    public void renderItem(final EntityLivingBase entityIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            final Item item = heldStack.getItem();
            final Block block = Block.getBlockFromItem(item);
            GL11.glPushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
                GL11.glScalef(2.0f, 2.0f, 2.0f);
                if (this.isBlockTranslucent(block) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
            if (this.isBlockTranslucent(block)) {
                GlStateManager.depthMask(true);
            }
            GL11.glPopMatrix();
        }
    }
    
    private boolean isBlockTranslucent(final Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    private void func_178101_a(final float angle, final float p_178101_2_) {
        GL11.glPushMatrix();
        GL11.glRotatef(angle, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(p_178101_2_, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }
    
    private void func_178109_a(final AbstractClientPlayer clientPlayer) {
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        final float f = (float)(i & 0xFFFF);
        final float f2 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }
    
    private void func_178110_a(final EntityPlayerSP entityplayerspIn, final float partialTicks) {
        final float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        final float f2 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GL11.glRotatef((entityplayerspIn.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef((entityplayerspIn.rotationYaw - f2) * 0.1f, 0.0f, 1.0f, 0.0f);
    }
    
    private float func_178100_c(final float p_178100_1_) {
        float f = 1.0f - p_178100_1_ / 45.0f + 0.1f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * 3.1415927f) * 0.5f + 0.5f;
        return f;
    }
    
    private void renderRightArm(final RenderPlayer renderPlayerIn) {
        GL11.glPushMatrix();
        GL11.glRotatef(54.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(64.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-62.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(0.25f, -0.85f, 0.75f);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GL11.glPopMatrix();
    }
    
    private void renderLeftArm(final RenderPlayer renderPlayerIn) {
        GL11.glPushMatrix();
        GL11.glRotatef(92.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(41.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.3f, -1.1f, 0.45f);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GL11.glPopMatrix();
    }
    
    private void renderPlayerArms(final AbstractClientPlayer clientPlayer) {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        final RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }
    
    private void renderItemMap(final AbstractClientPlayer clientPlayer, final float p_178097_2_, final float p_178097_3_, final float p_178097_4_) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(p_178097_4_ * 3.1415927f);
        GL11.glTranslatef(f, f2, f3);
        final float f4 = this.func_178100_c(p_178097_2_);
        GL11.glTranslatef(0.0f, 0.04f, -0.72f);
        GL11.glTranslatef(0.0f, p_178097_3_ * -1.2f, 0.0f);
        GL11.glTranslatef(0.0f, f4 * -0.5f, 0.0f);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(f4 * -85.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(clientPlayer);
        final float f5 = MathHelper.sin(p_178097_4_ * p_178097_4_ * 3.1415927f);
        final float f6 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927f);
        GL11.glRotatef(f5 * -20.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(f6 * -20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(f6 * -80.0f, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(0.38f, 0.38f, 0.38f);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);
        GL11.glTranslatef(-1.0f, -1.0f, 0.0f);
        GL11.glScalef(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(ItemRenderer.RES_MAP_BACKGROUND);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        final MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }
    
    private void renderArmInFirstPerson(final AbstractClientPlayer clientPlayer, final float p_178095_2_, final float p_178095_3_) {
        final boolean handChams = Chams.shouldRenderHand();
        final float f = -0.3f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f);
        final float f2 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f * 2.0f);
        final float f3 = -0.4f * MathHelper.sin(p_178095_3_ * 3.1415927f);
        GL11.glTranslatef(f, f2, f3);
        GL11.glTranslatef(0.64000005f, -0.6f, -0.72f);
        GL11.glTranslatef(0.0f, p_178095_2_ * -0.6f, 0.0f);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        final float f4 = MathHelper.sin(p_178095_3_ * p_178095_3_ * 3.1415927f);
        final float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927f);
        GL11.glRotatef(f5 * 70.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(f4 * -20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-1.0f, 3.6f, 3.5f);
        GL11.glRotatef(120.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(200.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(5.6f, 0.0f, 0.0f);
        GlStateManager.disableCull();
        final Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
        final RenderPlayer renderPlayer = (RenderPlayer)render;
        if (handChams) {
            Chams.preHandRender();
        }
        else {
            this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        renderPlayer.renderRightArm(this.mc.thePlayer);
        if (handChams) {
            Chams.postHandRender();
        }
        GlStateManager.enableCull();
    }
    
    private void func_178105_d(final float p_178105_1_) {
        final float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927f);
        final float f2 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927f * 2.0f);
        final float f3 = -0.2f * MathHelper.sin(p_178105_1_ * 3.1415927f);
        GL11.glTranslatef(f, f2, f3);
    }
    
    private void func_178104_a(final AbstractClientPlayer clientPlayer, final float p_178104_2_) {
        final float f = clientPlayer.getItemInUseCount() - p_178104_2_ + 1.0f;
        final float f2 = f / this.itemToRender.getMaxItemUseDuration();
        float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
        if (f2 >= 0.8f) {
            f3 = 0.0f;
        }
        GL11.glTranslatef(0.0f, f3, 0.0f);
        final float f4 = 1.0f - (float)Math.pow(f2, 27.0);
        GL11.glTranslatef(f4 * 0.6f, f4 * -0.5f, f4 * 0.0f);
        GL11.glRotatef(f4 * 90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(f4 * 10.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(f4 * 30.0f, 0.0f, 0.0f, 1.0f);
    }
    
    private void transformFirstPersonItem(final float equipProgress, final float swingProgress) {
        GL11.glTranslatef(0.56f, -0.52f, -0.72f);
        GL11.glTranslatef(0.0f, equipProgress * -0.6f, 0.0f);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        if (swingProgress > 0.0) {
            final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
            final float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
            GL11.glRotatef(f * -20.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        }
        float scale = 0.4f;
        if (Animations.getInstance().isEnabled()) {
            scale *= Animations.getInstance().itemScale.getValue().floatValue();
        }
        GL11.glScalef(scale, scale, scale);
    }
    
    private void func_178098_a(final float p_178098_1_, final AbstractClientPlayer clientPlayer) {
        GL11.glRotatef(-18.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(-12.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-8.0f, 1.0f, 0.0f, 0.0f);
        GL11.glTranslatef(-0.9f, 0.2f, 0.0f);
        final float f = this.itemToRender.getMaxItemUseDuration() - (clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0f);
        float f2 = f / 20.0f;
        f2 = (f2 * f2 + f2 * 2.0f) / 3.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        if (f2 > 0.1f) {
            final float f3 = MathHelper.sin((f - 0.1f) * 1.3f);
            final float f4 = f2 - 0.1f;
            final float f5 = f3 * f4;
            GL11.glTranslatef(f5 * 0.0f, f5 * 0.01f, f5 * 0.0f);
        }
        GL11.glTranslatef(f2 * 0.0f, f2 * 0.0f, f2 * 0.1f);
        GL11.glScalef(1.0f, 1.0f, 1.0f + f2 * 0.2f);
    }
    
    private void func_178103_d() {
        GL11.glTranslatef(-0.5f, 0.2f, 0.0f);
        GL11.glRotatef(30.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-80.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(60.0f, 0.0f, 1.0f, 0.0f);
    }
    
    public void renderItemInFirstPerson(final float partialTicks) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand()) {
            final float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
            final EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
            final float f2 = abstractclientplayer.getSwingProgress(partialTicks);
            final float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            final float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            this.func_178101_a(f3, f4);
            this.func_178109_a(abstractclientplayer);
            this.func_178110_a(abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GL11.glPushMatrix();
            if (this.itemToRender != null) {
                if (this.itemToRender.getItem() instanceof ItemMap) {
                    this.renderItemMap(abstractclientplayer, f3, f, f2);
                }
                else if (abstractclientplayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction = this.itemToRender.getItemUseAction();
                    switch (enumaction) {
                        case NONE: {
                            this.transformFirstPersonItem(f, f2);
                            break;
                        }
                        case EAT:
                        case DRINK: {
                            this.func_178104_a(abstractclientplayer, partialTicks);
                            this.transformFirstPersonItem(f, f2);
                            break;
                        }
                        case BLOCK: {
                            final float var9 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                            final Animations animations = Animations.getInstance();
                            if (animations.isEnabled()) {
                                float eqiupProgress;
                                if (animations.equipProgressProperty.getValue()) {
                                    eqiupProgress = f / animations.equipProgMultProperty.getValue().floatValue();
                                }
                                else {
                                    eqiupProgress = 0.0f;
                                }
                                GL11.glTranslated((double)animations.xPosProperty.getValue(), (double)animations.yPosProperty.getValue(), (double)animations.zPosProperty.getValue());
                                switch (animations.animationModeProperty.getValue()) {
                                    case LOL: {
                                        this.transformFirstPersonItem(eqiupProgress, 0.0f);
                                        GL11.glRotatef(-var9 * 30.0f, 1.0f, 0.0f, 0.0f);
                                        GL11.glRotatef(-var9 * 10.0f, 0.0f, 1.0f, 0.0f);
                                        break;
                                    }
                                    case LUL: {
                                        this.transformFirstPersonItem(eqiupProgress, 0.0f);
                                        GL11.glRotatef(-var9 * 30.0f, 1.0f, 0.0f, 0.0f);
                                        break;
                                    }
                                    case LEL: {
                                        this.transformFirstPersonItem(eqiupProgress, 0.0f);
                                        GL11.glRotatef(-var9 * 10.0f, 1.0f, 0.0f, 0.0f);
                                        GL11.glRotatef(-var9 * 30.0f, 1.0f, 0.0f, 0.0f);
                                        break;
                                    }
                                    case LIL: {
                                        this.transformFirstPersonItem(eqiupProgress, 0.0f);
                                        GL11.glRotatef(-var9 * 35.0f, 1.0f, 0.0f, 0.0f);
                                        break;
                                    }
                                    case EXHIBIBI: {
                                        this.transformFirstPersonItem(eqiupProgress, f2);
                                        GL11.glRotatef(var9 * 30.0f / 2.0f, -var9, -0.0f, 9.0f);
                                        GL11.glRotatef(var9 * 40.0f, 1.0f, -var9 / 2.0f, -0.0f);
                                        break;
                                    }
                                    case EXHIBOBO: {
                                        this.transformFirstPersonItem(eqiupProgress, 0.0f);
                                        GL11.glTranslatef(0.1f, 0.4f, -0.1f);
                                        GL11.glRotated((double)(-var9 * 20.0f), (double)(var9 / 2.0f), 0.0, 9.0);
                                        GL11.glRotated((double)(-var9 * 50.0f), 0.800000011920929, (double)(var9 / 2.0f), 0.0);
                                        break;
                                    }
                                    case OLD: {
                                        this.transformFirstPersonItem(f - 0.3f, f2);
                                        this.func_178103_d();
                                        break;
                                    }
                                    case SWING: {
                                        this.transformFirstPersonItem(f / 2.0f, f2);
                                        break;
                                    }
                                }
                                this.func_178103_d();
                                break;
                            }
                            this.transformFirstPersonItem(f, 0.0f);
                            this.func_178103_d();
                            break;
                        }
                        case BOW: {
                            this.transformFirstPersonItem(f, f2);
                            this.func_178098_a(partialTicks, abstractclientplayer);
                            break;
                        }
                    }
                }
                else {
                    this.angle = 0.0f;
                    this.func_178105_d(f2);
                    this.transformFirstPersonItem(f, f2);
                }
                this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            }
            else {
                this.renderArmInFirstPerson(abstractclientplayer, f, f2);
            }
            GL11.glPopMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    public void renderOverlays(final float partialTicks) {
        GlStateManager.disableAlpha();
        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
            BlockPos blockpos = new BlockPos(this.mc.thePlayer);
            final EntityPlayer entityplayer = this.mc.thePlayer;
            for (int i = 0; i < 8; ++i) {
                final double d0 = entityplayer.posX + ((i >> 0) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final double d2 = entityplayer.posY + ((i >> 1) % 2 - 0.5f) * 0.1f;
                final double d3 = entityplayer.posZ + ((i >> 2) % 2 - 0.5f) * entityplayer.width * 0.8f;
                final BlockPos blockpos2 = new BlockPos(d0, d2 + entityplayer.getEyeHeight(), d3);
                final IBlockState iblockstate2 = this.mc.theWorld.getBlockState(blockpos2);
                if (iblockstate2.getBlock().isVisuallyOpaque()) {
                    iblockstate = iblockstate2;
                    blockpos = blockpos2;
                }
            }
            if (iblockstate.getBlock().getRenderType() != -1) {
                final Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, this.mc.thePlayer, partialTicks, object, iblockstate, blockpos)) {
                    this.func_178108_a(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        if (!this.mc.thePlayer.isSpectator()) {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, this.mc.thePlayer, partialTicks)) {
                this.renderWaterOverlayTexture(partialTicks);
            }
            if (this.mc.thePlayer.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, this.mc.thePlayer, partialTicks)) {
                this.renderFireInFirstPerson(partialTicks);
            }
        }
        GlStateManager.enableAlpha();
    }
    
    private void func_178108_a(final float p_178108_1_, final TextureAtlasSprite p_178108_2_) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        final float f = 0.1f;
        GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.5f);
        GL11.glPushMatrix();
        final float f2 = -1.0f;
        final float f3 = 1.0f;
        final float f4 = -1.0f;
        final float f5 = 1.0f;
        final float f6 = -0.5f;
        final float f7 = p_178108_2_.getMinU();
        final float f8 = p_178108_2_.getMaxU();
        final float f9 = p_178108_2_.getMinV();
        final float f10 = p_178108_2_.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(f8, f10).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(f7, f10).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(f7, f9).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(f8, f9).endVertex();
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderWaterOverlayTexture(final float p_78448_1_) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(ItemRenderer.RES_UNDERWATER_OVERLAY);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            final float f = this.mc.thePlayer.getBrightness(p_78448_1_);
            GL11.glColor4f(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glPushMatrix();
            final float f2 = 4.0f;
            final float f3 = -1.0f;
            final float f4 = 1.0f;
            final float f5 = -1.0f;
            final float f6 = 1.0f;
            final float f7 = -0.5f;
            final float f8 = -this.mc.thePlayer.rotationYaw / 64.0f;
            final float f9 = this.mc.thePlayer.rotationPitch / 64.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f8, 4.0f + f9).endVertex();
            worldrenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f8, 4.0f + f9).endVertex();
            worldrenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f8, 0.0f + f9).endVertex();
            worldrenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f8, 0.0f + f9).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }
    
    private void renderFireInFirstPerson(final float p_78442_1_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GL11.glPushMatrix();
            final TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            final float f2 = textureatlassprite.getMinU();
            final float f3 = textureatlassprite.getMaxU();
            final float f4 = textureatlassprite.getMinV();
            final float f5 = textureatlassprite.getMaxV();
            final float f6 = (0.0f - f) / 2.0f;
            final float f7 = f6 + f;
            final float f8 = 0.0f - f / 2.0f;
            final float f9 = f8 + f;
            final float f10 = -0.5f;
            GL11.glTranslatef(-(i * 2 - 1) * 0.24f, -0.3f, 0.0f);
            GL11.glRotatef((i * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.pos(f6, f8, f10).tex(f3, f5).endVertex();
            worldrenderer.pos(f7, f8, f10).tex(f2, f5).endVertex();
            worldrenderer.pos(f7, f9, f10).tex(f2, f4).endVertex();
            worldrenderer.pos(f6, f9, f10).tex(f3, f4).endVertex();
            tessellator.draw();
            GL11.glPopMatrix();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }
    
    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        final EntityPlayer entityplayer = this.mc.thePlayer;
        final ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists()) {
                    final boolean flag2 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, this.itemToRender, itemstack, this.equippedItemSlot != entityplayer.inventory.currentItem);
                    if (!flag2) {
                        this.itemToRender = itemstack;
                        this.equippedItemSlot = entityplayer.inventory.currentItem;
                        return;
                    }
                }
                flag = true;
            }
        }
        else {
            flag = (this.itemToRender != null || itemstack != null);
        }
        final float f2 = 0.4f;
        final float f3 = flag ? 0.0f : 1.0f;
        final float f4 = MathHelper.clamp_float(f3 - this.equippedProgress, -f2, f2);
        this.equippedProgress += f4;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(itemstack);
            }
        }
    }
    
    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }
    
    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }
}
