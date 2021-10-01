// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import java.util.function.Supplier;
import vip.radium.module.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import vip.radium.utils.render.OGLUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import vip.radium.utils.render.Colors;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Chams", category = ModuleCategory.RENDER)
public final class Chams extends Module
{
    private static Chams cached;
    public final EnumProperty<Mode> modeProperty;
    private final Property<Boolean> hurtEffectProperty;
    private final EnumProperty<HurtEffect> hurtEffectStyleProperty;
    private final Property<Integer> hurtEffectColorProperty;
    private final Property<Boolean> handsProperty;
    private final Property<Integer> handsColorProperty;
    public final Property<Boolean> occludedFlatProperty;
    public final Property<Boolean> visibleFlatProperty;
    public final EnumProperty<ColorMode> visibleColorModeProperty;
    public final EnumProperty<ColorMode> occludedColorModeProperty;
    public final DoubleProperty visibleAlphaProperty;
    public final DoubleProperty occludedAlphaProperty;
    public final Property<Integer> visibleColorProperty;
    public final Property<Integer> occludedColorProperty;
    public final Property<Integer> secondVisibleColorProperty;
    public final Property<Integer> secondOccludedColorProperty;
    private final float[] hurtEffectColor;
    
    public Chams() {
        this.modeProperty = new EnumProperty<Mode>("Mode", Mode.COLOR);
        this.hurtEffectProperty = new Property<Boolean>("Hurt Effect", true);
        this.hurtEffectStyleProperty = new EnumProperty<HurtEffect>("Hurt Style", HurtEffect.COLOR, this.hurtEffectProperty::getValue);
        this.hurtEffectColorProperty = new Property<Integer>("Hurt Color", Colors.PURPLE, () -> this.hurtEffectProperty.getValue() && this.hurtEffectStyleProperty.getValue() == HurtEffect.COLOR);
        this.handsProperty = new Property<Boolean>("Hands", true);
        this.handsColorProperty = new Property<Integer>("Hands Color", Colors.RED, this.handsProperty::getValue);
        this.occludedFlatProperty = new Property<Boolean>("Occluded Flat", true, this::isColorChams);
        this.visibleFlatProperty = new Property<Boolean>("Visible Flat", true, this::isColorChams);
        this.visibleColorModeProperty = new EnumProperty<ColorMode>("V-Color Mode", ColorMode.COLOR, this::isColorChams);
        this.occludedColorModeProperty = new EnumProperty<ColorMode>("O-Color Mode", ColorMode.COLOR, this::isColorChams);
        this.visibleAlphaProperty = new DoubleProperty("Visible Alpha", 1.0, () -> this.visibleColorModeProperty.isAvailable() && this.visibleColorModeProperty.getValue() == ColorMode.RAINBOW, 0.0, 1.0, 0.1);
        this.occludedAlphaProperty = new DoubleProperty("Occluded Alpha", 0.4, () -> this.occludedColorModeProperty.isAvailable() && this.occludedColorModeProperty.getValue() == ColorMode.RAINBOW, 0.0, 1.0, 0.1);
        this.visibleColorProperty = new Property<Integer>("Visible Color", Colors.RED, () -> this.visibleColorModeProperty.isAvailable() && this.visibleColorModeProperty.getValue() != ColorMode.RAINBOW);
        this.occludedColorProperty = new Property<Integer>("Occluded Color", Colors.GREEN, () -> this.occludedColorModeProperty.isAvailable() && this.occludedColorModeProperty.getValue() != ColorMode.RAINBOW);
        this.secondVisibleColorProperty = new Property<Integer>("Second V-Color", Colors.RED, () -> this.visibleColorModeProperty.isAvailable() && this.visibleColorModeProperty.getValue() == ColorMode.PULSING);
        this.secondOccludedColorProperty = new Property<Integer>("Second O-Color", Colors.GREEN, () -> this.occludedColorModeProperty.isAvailable() && this.occludedColorModeProperty.getValue() == ColorMode.PULSING);
        this.hurtEffectColor = new float[4];
        final float[] rgb;
        this.hurtEffectColorProperty.addValueChangeListener((oldValue, value) -> {
            rgb = getRGB(value);
            this.hurtEffectColor[0] = rgb[0];
            this.hurtEffectColor[1] = rgb[1];
            this.hurtEffectColor[2] = rgb[2];
            this.hurtEffectColor[3] = rgb[3];
        });
    }
    
    public boolean isColorChams() {
        return this.modeProperty.getValue() == Mode.COLOR;
    }
    
    public static void preRenderOccluded(final int occludedColor, final boolean occludedFlat) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        if (occludedFlat) {
            GL11.glDisable(2896);
        }
        GL11.glEnable(32823);
        GL11.glPolygonOffset(0.0f, -1000000.0f);
        OpenGlHelper.setLightmapTextureCoords(1, 240.0f, 240.0f);
        GL11.glDepthMask(false);
        OGLUtils.color(occludedColor);
    }
    
    public static void preRenderVisible(final int visibleColor, final boolean visibleFlat, final boolean occludedFlat) {
        GL11.glDepthMask(true);
        if (occludedFlat && !visibleFlat) {
            GL11.glEnable(2896);
        }
        else if (!occludedFlat && visibleFlat) {
            GL11.glDisable(2896);
        }
        OGLUtils.color(visibleColor);
        GL11.glDisable(32823);
    }
    
    public static void postRender(final boolean visibleFlat) {
        if (visibleFlat) {
            GL11.glEnable(2896);
        }
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void preHandRender() {
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(2896);
        OGLUtils.color(getInstance().handsColorProperty.getValue());
    }
    
    public static void postHandRender() {
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    private static float[] getRGB(final int hex) {
        return new float[] { (hex >> 16 & 0xFF) / 255.0f, (hex >> 8 & 0xFF) / 255.0f, (hex & 0xFF) / 255.0f, (hex >> 24 & 0xFF) / 255.0f };
    }
    
    public static boolean shouldRenderHand() {
        return getInstance().isEnabled() && getInstance().handsProperty.getValue();
    }
    
    public static boolean isChamsEnabled() {
        return getInstance().isEnabled();
    }
    
    public static boolean isRenderHurtEffect() {
        return getInstance().hurtEffectProperty.getValue();
    }
    
    public static HurtEffect getHurtEffect() {
        return getInstance().hurtEffectStyleProperty.getValue();
    }
    
    public static boolean isValid(final EntityLivingBase entity) {
        return !entity.isInvisible() && entity.isEntityAlive() && entity instanceof EntityPlayer;
    }
    
    public static Chams getInstance() {
        if (Chams.cached != null) {
            return Chams.cached;
        }
        return Chams.cached = ModuleManager.getInstance(Chams.class);
    }
    
    public enum ColorMode
    {
        COLOR("COLOR", 0), 
        RAINBOW("RAINBOW", 1), 
        PULSING("PULSING", 2);
        
        private ColorMode(final String name, final int ordinal) {
        }
    }
    
    public enum HurtEffect
    {
        OLD(() -> 1.0f, () -> 0.0f, () -> 0.0f, () -> 0.4f), 
        NEW(() -> 1.0f, () -> 0.0f, () -> 0.0f, () -> 0.3f), 
        COLOR(() -> Chams.cached.hurtEffectColor[0], () -> Chams.cached.hurtEffectColor[1], () -> Chams.cached.hurtEffectColor[2], () -> Chams.cached.hurtEffectColor[3]);
        
        private final Supplier<Float> red;
        private final Supplier<Float> green;
        private final Supplier<Float> blue;
        private final Supplier<Float> alpha;
        
        private HurtEffect(final Supplier<Float> red, final Supplier<Float> g, final Supplier<Float> b, final Supplier<Float> a) {
            this.red = red;
            this.green = g;
            this.blue = b;
            this.alpha = a;
        }
        
        public float getRed() {
            return this.red.get();
        }
        
        public float getGreen() {
            return this.green.get();
        }
        
        public float getBlue() {
            return this.blue.get();
        }
        
        public float getAlpha() {
            return this.alpha.get();
        }
    }
    
    private enum Mode
    {
        COLOR("COLOR", 0), 
        NORMAL("NORMAL", 1);
        
        private Mode(final String name, final int ordinal) {
        }
    }
}
