// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import vip.radium.module.impl.render.EnchantGlint;
import net.optifine.shaders.ShadersRender;
import net.optifine.shaders.Shaders;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.entity.Entity;
import net.optifine.CustomItems;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;
import net.minecraft.item.ItemArmor;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;

public abstract class LayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase>
{
    protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES;
    protected T field_177189_c;
    protected T field_177186_d;
    private final RendererLivingEntity<?> renderer;
    private float alpha;
    private float colorR;
    private float colorG;
    private float colorB;
    private static boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP;
    
    static {
        ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
    }
    
    public LayerArmorBase(final RendererLivingEntity<?> rendererIn) {
        this.alpha = 1.0f;
        this.colorR = 1.0f;
        this.colorG = 1.0f;
        this.colorB = 1.0f;
        this.renderer = rendererIn;
        this.initArmor();
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 4);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 3);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 2);
        this.renderLayer(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale, 1);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private void renderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177182_2_, final float p_177182_3_, final float p_177182_4_, final float p_177182_5_, final float p_177182_6_, final float p_177182_7_, final float p_177182_8_, final int armorSlot) {
        final ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);
        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            T t = this.func_177175_a(armorSlot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_4_);
            if (Reflector.ForgeHooksClient.exists()) {
                t = this.getArmorModelHook(entitylivingbaseIn, itemstack, armorSlot, t);
            }
            this.func_177179_a(t, armorSlot);
            final boolean flag = this.isSlotForLeggings(armorSlot);
            if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, null)) {
                if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                    this.renderer.bindTexture(this.getArmorResource(entitylivingbaseIn, itemstack, flag ? 2 : 1, null));
                }
                else {
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
                }
            }
            if (Reflector.ForgeHooksClient_getArmorTexture.exists()) {
                if (ReflectorForge.armorHasOverlay(itemarmor, itemstack)) {
                    final int j = itemarmor.getColor(itemstack);
                    final float f3 = (j >> 16 & 0xFF) / 255.0f;
                    final float f4 = (j >> 8 & 0xFF) / 255.0f;
                    final float f5 = (j & 0xFF) / 255.0f;
                    GL11.glColor4f(this.colorR * f3, this.colorG * f4, this.colorB * f5, this.alpha);
                    t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(entitylivingbaseIn, itemstack, flag ? 2 : 1, "overlay"));
                    }
                }
                GL11.glColor4f(this.colorR, this.colorG, this.colorB, this.alpha);
                t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                if (!LayerArmorBase.skipRenderGlint && itemstack.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entitylivingbaseIn, itemstack, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
                    this.func_177183_a(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                }
                return;
            }
            switch (itemarmor.getArmorMaterial()) {
                case LEATHER: {
                    final int i = itemarmor.getColor(itemstack);
                    final float f6 = (i >> 16 & 0xFF) / 255.0f;
                    final float f7 = (i >> 8 & 0xFF) / 255.0f;
                    final float f8 = (i & 0xFF) / 255.0f;
                    GL11.glColor4f(this.colorR * f6, this.colorG * f7, this.colorB * f8, this.alpha);
                    t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(itemstack, flag ? 2 : 1, "overlay")) {
                        this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                    }
                }
                case CHAIN:
                case IRON:
                case GOLD:
                case DIAMOND: {
                    GL11.glColor4f(this.colorR, this.colorG, this.colorB, this.alpha);
                    t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    break;
                }
            }
            if (!LayerArmorBase.skipRenderGlint && itemstack.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entitylivingbaseIn, itemstack, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
                this.func_177183_a(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            }
        }
    }
    
    public ItemStack getCurrentArmor(final EntityLivingBase entitylivingbaseIn, final int armorSlot) {
        return entitylivingbaseIn.getCurrentArmor(armorSlot - 1);
    }
    
    public T func_177175_a(final int p_177175_1_) {
        return this.isSlotForLeggings(p_177175_1_) ? this.field_177189_c : this.field_177186_d;
    }
    
    private boolean isSlotForLeggings(final int armorSlot) {
        return armorSlot == 2;
    }
    
    private void func_177183_a(final EntityLivingBase entitylivingbaseIn, final T modelbaseIn, final float p_177183_3_, final float p_177183_4_, final float p_177183_5_, final float p_177183_6_, final float p_177183_7_, final float p_177183_8_, final float p_177183_9_) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            final float f = entitylivingbaseIn.ticksExisted + p_177183_5_;
            this.renderer.bindTexture(LayerArmorBase.ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            final boolean enabled = EnchantGlint.isGlintEnabled();
            final int col = EnchantGlint.getArmorModelColor();
            final float red = (col >> 16 & 0xFF) / 255.0f;
            final float green = (col >> 8 & 0xFF) / 255.0f;
            final float blue = (col & 0xFF) / 255.0f;
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            final float f2 = 0.5f;
            GL11.glColor4f(f2, f2, f2, 1.0f);
            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(768, 1);
                final float f3 = 0.76f;
                if (enabled) {
                    GL11.glColor4f(red, green, blue, 1.0f);
                }
                else {
                    GL11.glColor4f(0.5f * f3, 0.25f * f3, 0.8f * f3, 1.0f);
                }
                GL11.glMatrixMode(5890);
                GL11.glLoadIdentity();
                final float f4 = 0.33333334f;
                GL11.glScalef(f4, f4, f4);
                GL11.glRotatef(30.0f - i * 60.0f, 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef(0.0f, f * (0.001f + i * 0.003f) * 20.0f, 0.0f);
                GL11.glMatrixMode(5888);
                modelbaseIn.render(entitylivingbaseIn, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
            }
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
    
    private ResourceLocation getArmorResource(final ItemArmor p_177181_1_, final boolean p_177181_2_) {
        return this.getArmorResource(p_177181_1_, p_177181_2_, null);
    }
    
    private ResourceLocation getArmorResource(final ItemArmor p_177178_1_, final boolean p_177178_2_, final String p_177178_3_) {
        final String s = String.format("textures/models/armor/%s_layer_%d%s.png", p_177178_1_.getArmorMaterial().getName(), p_177178_2_ ? 2 : 1, (p_177178_3_ == null) ? "" : String.format("_%s", p_177178_3_));
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
        }
        return resourcelocation;
    }
    
    protected abstract void initArmor();
    
    protected abstract void func_177179_a(final T p0, final int p1);
    
    protected T getArmorModelHook(final EntityLivingBase p_getArmorModelHook_1_, final ItemStack p_getArmorModelHook_2_, final int p_getArmorModelHook_3_, final T p_getArmorModelHook_4_) {
        return p_getArmorModelHook_4_;
    }
    
    public ResourceLocation getArmorResource(final Entity p_getArmorResource_1_, final ItemStack p_getArmorResource_2_, final int p_getArmorResource_3_, final String p_getArmorResource_4_) {
        final ItemArmor itemarmor = (ItemArmor)p_getArmorResource_2_.getItem();
        String s = itemarmor.getArmorMaterial().getName();
        String s2 = "minecraft";
        final int i = s.indexOf(58);
        if (i != -1) {
            s2 = s.substring(0, i);
            s = s.substring(i + 1);
        }
        String s3 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", s2, s, this.isSlotForLeggings(p_getArmorResource_3_) ? 2 : 1, (p_getArmorResource_4_ == null) ? "" : String.format("_%s", p_getArmorResource_4_));
        s3 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, p_getArmorResource_1_, p_getArmorResource_2_, s3, p_getArmorResource_3_, p_getArmorResource_4_);
        ResourceLocation resourcelocation = LayerArmorBase.ARMOR_TEXTURE_RES_MAP.get(s3);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s3);
            LayerArmorBase.ARMOR_TEXTURE_RES_MAP.put(s3, resourcelocation);
        }
        return resourcelocation;
    }
    
    public static void shouldRenderArmorEffect(final boolean should) {
        LayerArmorBase.skipRenderGlint = !should;
    }
}
