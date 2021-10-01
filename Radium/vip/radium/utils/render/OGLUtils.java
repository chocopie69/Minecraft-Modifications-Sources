// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

public final class OGLUtils
{
    private static final FloatBuffer windowPosition;
    private static final IntBuffer viewport;
    private static final FloatBuffer modelMatrix;
    private static final FloatBuffer projectionMatrix;
    private static final float[] BUFFER;
    
    static {
        windowPosition = GLAllocation.createDirectFloatBuffer(4);
        viewport = GLAllocation.createDirectIntBuffer(16);
        modelMatrix = GLAllocation.createDirectFloatBuffer(16);
        projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
        BUFFER = new float[3];
    }
    
    private OGLUtils() {
    }
    
    public static void enableBlending() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void disableTexture2D() {
        GL11.glDisable(3553);
    }
    
    public static void enableTexture2D() {
        GL11.glEnable(3553);
    }
    
    public static void enableDepth() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
    }
    
    public static void disableDepth() {
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
    }
    
    public static void preDraw(final int color, final int mode) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        color(color);
        GL11.glBegin(mode);
    }
    
    public static void postDraw() {
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
    }
    
    public static void color(final int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }
    
    public static void disableBlending() {
        GL11.glDisable(3042);
    }
    
    public static void startScissorBox(final ScaledResolution sr, final int x, final int y, final int width, final int height) {
        final int sf = sr.getScaleFactor();
        GL11.glScissor(x * sf, (sr.getScaledHeight() - (y + height)) * sf, width * sf, height * sf);
    }
    
    public static void startScissorBox(final LockedResolution lr, final int x, final int y, final int width, final int height) {
        GL11.glScissor(x * 2, (lr.getHeight() - (y + height)) * 2, width * 2, height * 2);
    }
    
    public static float[] project2D(final float x, final float y, final float z, final int scaleFactor) {
        GL11.glGetFloat(2982, OGLUtils.modelMatrix);
        GL11.glGetFloat(2983, OGLUtils.projectionMatrix);
        GL11.glGetInteger(2978, OGLUtils.viewport);
        if (GLU.gluProject(x, y, z, OGLUtils.modelMatrix, OGLUtils.projectionMatrix, OGLUtils.viewport, OGLUtils.windowPosition)) {
            OGLUtils.BUFFER[0] = OGLUtils.windowPosition.get(0) / scaleFactor;
            OGLUtils.BUFFER[1] = (Display.getHeight() - OGLUtils.windowPosition.get(1)) / scaleFactor;
            OGLUtils.BUFFER[2] = OGLUtils.windowPosition.get(2);
            return OGLUtils.BUFFER;
        }
        return null;
    }
}
