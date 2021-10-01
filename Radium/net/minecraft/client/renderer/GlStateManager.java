// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.nio.IntBuffer;
import net.optifine.SmartAnimations;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL14;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import org.lwjgl.opengl.GL11;
import net.optifine.render.GlBlendState;
import net.optifine.render.GlAlphaState;
import net.optifine.util.LockCounter;

public class GlStateManager
{
    public static boolean clearEnabled;
    private static final AlphaState alphaState;
    private static final BooleanState lightingState;
    private static final BooleanState[] lightState;
    private static final ColorMaterialState colorMaterialState;
    private static final BlendState blendState;
    private static final DepthState depthState;
    private static final FogState fogState;
    private static final CullState cullState;
    private static final PolygonOffsetState polygonOffsetState;
    private static final ColorLogicState colorLogicState;
    private static final TexGenState texGenState;
    private static final ClearState clearState;
    private static final StencilState stencilState;
    private static final BooleanState normalizeState;
    private static int activeTextureUnit;
    private static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;
    private static final LockCounter alphaLock;
    private static final GlAlphaState alphaLockState;
    private static final LockCounter blendLock;
    private static final GlBlendState blendLockState;
    private static boolean creatingDisplayList;
    
    static {
        GlStateManager.clearEnabled = true;
        alphaState = new AlphaState(null);
        lightingState = new BooleanState(2896);
        lightState = new BooleanState[8];
        colorMaterialState = new ColorMaterialState(null);
        blendState = new BlendState(null);
        depthState = new DepthState(null);
        fogState = new FogState(null);
        cullState = new CullState(null);
        polygonOffsetState = new PolygonOffsetState(null);
        colorLogicState = new ColorLogicState(null);
        texGenState = new TexGenState(null);
        clearState = new ClearState(null);
        stencilState = new StencilState(null);
        normalizeState = new BooleanState(2977);
        GlStateManager.activeTextureUnit = 0;
        textureState = new TextureState[32];
        GlStateManager.activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask(null);
        colorState = new Color();
        alphaLock = new LockCounter();
        alphaLockState = new GlAlphaState();
        blendLock = new LockCounter();
        blendLockState = new GlBlendState();
        GlStateManager.creatingDisplayList = false;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        for (int j = 0; j < GlStateManager.textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState(null);
        }
    }
    
    public static void disableAlpha() {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setDisabled();
        }
        else {
            GlStateManager.alphaState.field_179208_a.setDisabled();
        }
    }
    
    public static void enableAlpha() {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setEnabled();
        }
        else {
            GlStateManager.alphaState.field_179208_a.setEnabled();
        }
    }
    
    public static void alphaFunc(final int func, final float ref) {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setFuncRef(func, ref);
        }
        else if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GL11.glAlphaFunc(GlStateManager.alphaState.func = func, GlStateManager.alphaState.ref = ref);
        }
    }
    
    public static void enableLighting() {
        GlStateManager.lightingState.setEnabled();
    }
    
    public static void disableLighting() {
        GlStateManager.lightingState.setDisabled();
    }
    
    public static void enableLight(final int light) {
        GlStateManager.lightState[light].setEnabled();
    }
    
    public static void disableLight(final int light) {
        GlStateManager.lightState[light].setDisabled();
    }
    
    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setEnabled();
    }
    
    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setDisabled();
    }
    
    public static void colorMaterial(final int face, final int mode) {
        if (face != GlStateManager.colorMaterialState.field_179189_b || mode != GlStateManager.colorMaterialState.field_179190_c) {
            GL11.glColorMaterial(GlStateManager.colorMaterialState.field_179189_b = face, GlStateManager.colorMaterialState.field_179190_c = mode);
        }
    }
    
    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }
    
    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }
    
    public static void depthFunc(final int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GL11.glDepthFunc(GlStateManager.depthState.depthFunc = depthFunc);
        }
    }
    
    public static void depthMask(final boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GL11.glDepthMask(GlStateManager.depthState.maskEnabled = flagIn);
        }
    }
    
    public static void disableBlend() {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setDisabled();
        }
        else {
            GlStateManager.blendState.field_179213_a.setDisabled();
        }
    }
    
    public static void enableBlend() {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setEnabled();
        }
        else {
            GlStateManager.blendState.field_179213_a.setEnabled();
        }
    }
    
    public static void blendFunc(final int srcFactor, final int dstFactor) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setFactors(srcFactor, dstFactor);
        }
        else if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactor != GlStateManager.blendState.srcFactorAlpha || dstFactor != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactor;
            GlStateManager.blendState.dstFactorAlpha = dstFactor;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactor, dstFactor);
            }
            GL11.glBlendFunc(srcFactor, dstFactor);
        }
    }
    
    public static void tryBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setFactors(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
        else if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha;
            GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            }
            GL14.glBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
    }
    
    public static void enableFog() {
        GlStateManager.fogState.field_179049_a.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.field_179049_a.setDisabled();
    }
    
    public static void setFog(final int param) {
        if (param != GlStateManager.fogState.field_179047_b) {
            GL11.glFogi(2917, GlStateManager.fogState.field_179047_b = param);
            if (Config.isShaders()) {
                Shaders.setFogMode(param);
            }
        }
    }
    
    public static void setFogDensity(float param) {
        if (param < 0.0f) {
            param = 0.0f;
        }
        if (param != GlStateManager.fogState.field_179048_c) {
            GL11.glFogf(2914, GlStateManager.fogState.field_179048_c = param);
            if (Config.isShaders()) {
                Shaders.setFogDensity(param);
            }
        }
    }
    
    public static void setFogStart(final float param) {
        if (param != GlStateManager.fogState.field_179045_d) {
            GL11.glFogf(2915, GlStateManager.fogState.field_179045_d = param);
        }
    }
    
    public static void setFogEnd(final float param) {
        if (param != GlStateManager.fogState.field_179046_e) {
            GL11.glFogf(2916, GlStateManager.fogState.field_179046_e = param);
        }
    }
    
    public static void enableCull() {
        GlStateManager.cullState.field_179054_a.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.field_179054_a.setDisabled();
    }
    
    public static void cullFace(final int mode) {
        if (mode != GlStateManager.cullState.field_179053_b) {
            GL11.glCullFace(GlStateManager.cullState.field_179053_b = mode);
        }
    }
    
    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setEnabled();
    }
    
    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setDisabled();
    }
    
    public static void doPolygonOffset(final float factor, final float units) {
        if (factor != GlStateManager.polygonOffsetState.field_179043_c || units != GlStateManager.polygonOffsetState.field_179041_d) {
            GL11.glPolygonOffset(GlStateManager.polygonOffsetState.field_179043_c = factor, GlStateManager.polygonOffsetState.field_179041_d = units);
        }
    }
    
    public static void enableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setEnabled();
    }
    
    public static void disableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setDisabled();
    }
    
    public static void colorLogicOp(final int opcode) {
        if (opcode != GlStateManager.colorLogicState.field_179196_b) {
            GL11.glLogicOp(GlStateManager.colorLogicState.field_179196_b = opcode);
        }
    }
    
    public static void enableTexGenCoord(final TexGen p_179087_0_) {
        texGenCoord(p_179087_0_).field_179067_a.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen p_179100_0_) {
        texGenCoord(p_179100_0_).field_179067_a.setDisabled();
    }
    
    public static void texGen(final TexGen p_179149_0_, final int p_179149_1_) {
        final TexGenCoord glstatemanager$texgencoord = texGenCoord(p_179149_0_);
        if (p_179149_1_ != glstatemanager$texgencoord.field_179066_c) {
            glstatemanager$texgencoord.field_179066_c = p_179149_1_;
            GL11.glTexGeni(glstatemanager$texgencoord.field_179065_b, 9472, p_179149_1_);
        }
    }
    
    public static void func_179105_a(final TexGen p_179105_0_, final int pname, final FloatBuffer params) {
        GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, pname, params);
    }
    
    private static TexGenCoord texGenCoord(final TexGen p_179125_0_) {
        switch (p_179125_0_) {
            case S: {
                return GlStateManager.texGenState.field_179064_a;
            }
            case T: {
                return GlStateManager.texGenState.field_179062_b;
            }
            case R: {
                return GlStateManager.texGenState.field_179063_c;
            }
            case Q: {
                return GlStateManager.texGenState.field_179061_d;
            }
            default: {
                return GlStateManager.texGenState.field_179064_a;
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (GlStateManager.activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            GlStateManager.activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }
    
    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }
    
    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }
    
    public static void deleteTexture(final int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
            TextureState[] textureState;
            for (int length = (textureState = GlStateManager.textureState).length, i = 0; i < length; ++i) {
                final TextureState glstatemanager$texturestate = textureState[i];
                if (glstatemanager$texturestate.textureName == texture) {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }
    
    public static void bindTexture(final int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture);
            if (SmartAnimations.isActive()) {
                SmartAnimations.textureRendered(texture);
            }
        }
    }
    
    public static void enableNormalize() {
        GlStateManager.normalizeState.setEnabled();
    }
    
    public static void disableNormalize() {
        GlStateManager.normalizeState.setDisabled();
    }
    
    public static void shadeModel(final int mode) {
        if (mode != GlStateManager.activeShadeModel) {
            GL11.glShadeModel(GlStateManager.activeShadeModel = mode);
        }
    }
    
    public static void enableRescaleNormal() {
        GlStateManager.rescaleNormalState.setEnabled();
    }
    
    public static void disableRescaleNormal() {
        GlStateManager.rescaleNormalState.setDisabled();
    }
    
    public static void viewport(final int x, final int y, final int width, final int height) {
        GL11.glViewport(x, y, width, height);
    }
    
    public static void colorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GL11.glColorMask(GlStateManager.colorMaskState.red = red, GlStateManager.colorMaskState.green = green, GlStateManager.colorMaskState.blue = blue, GlStateManager.colorMaskState.alpha = alpha);
        }
    }
    
    public static void clearDepth(final double depth) {
        if (depth != GlStateManager.clearState.field_179205_a) {
            GL11.glClearDepth(GlStateManager.clearState.field_179205_a = depth);
        }
    }
    
    public static void clearColor(final float red, final float green, final float blue, final float alpha) {
        if (red != GlStateManager.clearState.field_179203_b.red || green != GlStateManager.clearState.field_179203_b.green || blue != GlStateManager.clearState.field_179203_b.blue || alpha != GlStateManager.clearState.field_179203_b.alpha) {
            GL11.glClearColor(GlStateManager.clearState.field_179203_b.red = red, GlStateManager.clearState.field_179203_b.green = green, GlStateManager.clearState.field_179203_b.blue = blue, GlStateManager.clearState.field_179203_b.alpha = alpha);
        }
    }
    
    public static void clear(final int mask) {
        if (GlStateManager.clearEnabled) {
            GL11.glClear(mask);
        }
    }
    
    public static void getFloat(final int pname, final FloatBuffer params) {
        GL11.glGetFloat(pname, params);
    }
    
    public static void resetColor() {
        final Color colorState = GlStateManager.colorState;
        final Color colorState2 = GlStateManager.colorState;
        final Color colorState3 = GlStateManager.colorState;
        final Color colorState4 = GlStateManager.colorState;
        final float n = -1.0f;
        colorState4.alpha = n;
        colorState3.blue = n;
        colorState2.green = n;
        colorState.red = n;
    }
    
    public static void glDrawArrays(final int p_glDrawArrays_0_, final int p_glDrawArrays_1_, final int p_glDrawArrays_2_) {
        GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void callList(final int list) {
        GL11.glCallList(list);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glCallList(list);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void callLists(final IntBuffer p_callLists_0_) {
        GL11.glCallLists(p_callLists_0_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glCallLists(p_callLists_0_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void glNewList(final int p_glNewList_0_, final int p_glNewList_1_) {
        GL11.glNewList(p_glNewList_0_, p_glNewList_1_);
        GlStateManager.creatingDisplayList = true;
    }
    
    public static void glEndList() {
        GL11.glEndList();
        GlStateManager.creatingDisplayList = false;
    }
    
    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + GlStateManager.activeTextureUnit;
    }
    
    public static void bindCurrentTexture() {
        GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName);
    }
    
    public static int getBoundTexture() {
        return GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName;
    }
    
    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            final int i = GL11.glGetInteger(34016);
            final int j = GL11.glGetInteger(32873);
            final int k = getActiveTextureUnit();
            final int l = getBoundTexture();
            if (l > 0 && (i != k || j != l)) {
                Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
            }
        }
    }
    
    public static void deleteTextures(final IntBuffer p_deleteTextures_0_) {
        p_deleteTextures_0_.rewind();
        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
            final int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }
        p_deleteTextures_0_.rewind();
    }
    
    public static boolean isFogEnabled() {
        return GlStateManager.fogState.field_179049_a.currentState;
    }
    
    public static void setFogEnabled(final boolean p_setFogEnabled_0_) {
        GlStateManager.fogState.field_179049_a.setState(p_setFogEnabled_0_);
    }
    
    public static void lockAlpha(final GlAlphaState p_lockAlpha_0_) {
        if (!GlStateManager.alphaLock.isLocked()) {
            getAlphaState(GlStateManager.alphaLockState);
            setAlphaState(p_lockAlpha_0_);
            GlStateManager.alphaLock.lock();
        }
    }
    
    public static void unlockAlpha() {
        if (GlStateManager.alphaLock.unlock()) {
            setAlphaState(GlStateManager.alphaLockState);
        }
    }
    
    public static void getAlphaState(final GlAlphaState p_getAlphaState_0_) {
        if (GlStateManager.alphaLock.isLocked()) {
            p_getAlphaState_0_.setState(GlStateManager.alphaLockState);
        }
        else {
            p_getAlphaState_0_.setState(GlStateManager.alphaState.field_179208_a.currentState, GlStateManager.alphaState.func, GlStateManager.alphaState.ref);
        }
    }
    
    public static void setAlphaState(final GlAlphaState p_setAlphaState_0_) {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setState(p_setAlphaState_0_);
        }
        else {
            GlStateManager.alphaState.field_179208_a.setState(p_setAlphaState_0_.isEnabled());
            alphaFunc(p_setAlphaState_0_.getFunc(), p_setAlphaState_0_.getRef());
        }
    }
    
    public static void lockBlend(final GlBlendState p_lockBlend_0_) {
        if (!GlStateManager.blendLock.isLocked()) {
            getBlendState(GlStateManager.blendLockState);
            setBlendState(p_lockBlend_0_);
            GlStateManager.blendLock.lock();
        }
    }
    
    public static void unlockBlend() {
        if (GlStateManager.blendLock.unlock()) {
            setBlendState(GlStateManager.blendLockState);
        }
    }
    
    public static void getBlendState(final GlBlendState p_getBlendState_0_) {
        if (GlStateManager.blendLock.isLocked()) {
            p_getBlendState_0_.setState(GlStateManager.blendLockState);
        }
        else {
            p_getBlendState_0_.setState(GlStateManager.blendState.field_179213_a.currentState, GlStateManager.blendState.srcFactor, GlStateManager.blendState.dstFactor, GlStateManager.blendState.srcFactorAlpha, GlStateManager.blendState.dstFactorAlpha);
        }
    }
    
    public static void setBlendState(final GlBlendState p_setBlendState_0_) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setState(p_setBlendState_0_);
        }
        else {
            GlStateManager.blendState.field_179213_a.setState(p_setBlendState_0_.isEnabled());
            if (!p_setBlendState_0_.isSeparate()) {
                blendFunc(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor());
            }
            else {
                tryBlendFuncSeparate(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor(), p_setBlendState_0_.getSrcFactorAlpha(), p_setBlendState_0_.getDstFactorAlpha());
            }
        }
    }
    
    public static void glMultiDrawArrays(final int p_glMultiDrawArrays_0_, final IntBuffer p_glMultiDrawArrays_1_, final IntBuffer p_glMultiDrawArrays_2_) {
        GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public enum TexGen
    {
        S("S", 0), 
        T("T", 1), 
        R("R", 2), 
        Q("Q", 3);
        
        private TexGen(final String name, final int ordinal) {
        }
    }
    
    static class AlphaState
    {
        public BooleanState field_179208_a;
        public int func;
        public float ref;
        
        private AlphaState() {
            this.field_179208_a = new BooleanState(3008);
            this.func = 519;
            this.ref = -1.0f;
        }
    }
    
    static class BlendState
    {
        public BooleanState field_179213_a;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;
        
        private BlendState() {
            this.field_179213_a = new BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
    }
    
    static class BooleanState
    {
        private final int capability;
        private boolean currentState;
        
        public BooleanState(final int capabilityIn) {
            this.currentState = false;
            this.capability = capabilityIn;
        }
        
        public void setDisabled() {
            this.setState(false);
        }
        
        public void setEnabled() {
            this.setState(true);
        }
        
        public void setState(final boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
                    GL11.glEnable(this.capability);
                }
                else {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }
    
    static class ClearState
    {
        public double field_179205_a;
        public Color field_179203_b;
        public int field_179204_c;
        
        private ClearState() {
            this.field_179205_a = 1.0;
            this.field_179203_b = new Color(0.0f, 0.0f, 0.0f, 0.0f);
            this.field_179204_c = 0;
        }
    }
    
    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;
        
        public Color() {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
        }
        
        public Color(final float redIn, final float greenIn, final float blueIn, final float alphaIn) {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }
    
    static class ColorLogicState
    {
        public BooleanState field_179197_a;
        public int field_179196_b;
        
        private ColorLogicState() {
            this.field_179197_a = new BooleanState(3058);
            this.field_179196_b = 5379;
        }
    }
    
    static class ColorMask
    {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;
        
        private ColorMask() {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
    }
    
    static class ColorMaterialState
    {
        public BooleanState field_179191_a;
        public int field_179189_b;
        public int field_179190_c;
        
        private ColorMaterialState() {
            this.field_179191_a = new BooleanState(2903);
            this.field_179189_b = 1032;
            this.field_179190_c = 5634;
        }
    }
    
    static class CullState
    {
        public BooleanState field_179054_a;
        public int field_179053_b;
        
        private CullState() {
            this.field_179054_a = new BooleanState(2884);
            this.field_179053_b = 1029;
        }
    }
    
    static class DepthState
    {
        public BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;
        
        private DepthState() {
            this.depthTest = new BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
    }
    
    static class FogState
    {
        public BooleanState field_179049_a;
        public int field_179047_b;
        public float field_179048_c;
        public float field_179045_d;
        public float field_179046_e;
        
        private FogState() {
            this.field_179049_a = new BooleanState(2912);
            this.field_179047_b = 2048;
            this.field_179048_c = 1.0f;
            this.field_179045_d = 0.0f;
            this.field_179046_e = 1.0f;
        }
    }
    
    static class PolygonOffsetState
    {
        public BooleanState field_179044_a;
        public BooleanState field_179042_b;
        public float field_179043_c;
        public float field_179041_d;
        
        private PolygonOffsetState() {
            this.field_179044_a = new BooleanState(32823);
            this.field_179042_b = new BooleanState(10754);
            this.field_179043_c = 0.0f;
            this.field_179041_d = 0.0f;
        }
    }
    
    static class StencilFunc
    {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;
        
        private StencilFunc() {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }
    }
    
    static class StencilState
    {
        public StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;
        
        private StencilState() {
            this.field_179078_a = new StencilFunc(null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }
    }
    
    static class TexGenCoord
    {
        public BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c;
        
        public TexGenCoord(final int p_i46254_1_, final int p_i46254_2_) {
            this.field_179066_c = -1;
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new BooleanState(p_i46254_2_);
        }
    }
    
    static class TexGenState
    {
        public TexGenCoord field_179064_a;
        public TexGenCoord field_179062_b;
        public TexGenCoord field_179063_c;
        public TexGenCoord field_179061_d;
        
        private TexGenState() {
            this.field_179064_a = new TexGenCoord(8192, 3168);
            this.field_179062_b = new TexGenCoord(8193, 3169);
            this.field_179063_c = new TexGenCoord(8194, 3170);
            this.field_179061_d = new TexGenCoord(8195, 3171);
        }
    }
    
    static class TextureState
    {
        public BooleanState texture2DState;
        public int textureName;
        
        private TextureState() {
            this.texture2DState = new BooleanState(3553);
            this.textureName = 0;
        }
    }
}
