// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.MathUtils;
import net.minecraft.client.Minecraft;
import java.awt.Color;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.utils.Wrapper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.culling.Frustum;

public final class RenderingUtils
{
    private static final double DOUBLE_PI = 6.283185307179586;
    private static final Frustum FRUSTUM;
    private static int lastScaledWidth;
    private static int lastScaledHeight;
    private static int lastGuiScale;
    private static ScaledResolution scaledResolution;
    private static int lastWidth;
    private static int lastHeight;
    private static LockedResolution lockedResolution;
    
    static {
        FRUSTUM = new Frustum();
    }
    
    private RenderingUtils() {
    }
    
    public static boolean isBBInFrustum(final AxisAlignedBB aabb) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        RenderingUtils.FRUSTUM.setPosition(player.posX, player.posY, player.posZ);
        return RenderingUtils.FRUSTUM.isBoundingBoxInFrustum(aabb);
    }
    
    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final boolean sideways, final int startColor, final int endColor) {
        GL11.glDisable(3553);
        OGLUtils.enableBlending();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        OGLUtils.color(startColor);
        if (sideways) {
            GL11.glVertex2d(left, top);
            GL11.glVertex2d(left, bottom);
            OGLUtils.color(endColor);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
        }
        else {
            GL11.glVertex2d(left, top);
            OGLUtils.color(endColor);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            OGLUtils.color(startColor);
            GL11.glVertex2d(right, top);
        }
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
    }
    
    public static void drawLoop(float x, float y, final double radius, final int points, final float width, final int color, final boolean filled) {
        x += (float)radius;
        y += (float)radius;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        OGLUtils.color(color);
        final int smooth = filled ? 2881 : 2848;
        GL11.glEnable(smooth);
        GL11.glHint(filled ? 3155 : 3154, 4354);
        GL11.glBegin(filled ? 6 : 2);
        for (int i = 0; i < points; ++i) {
            if (filled) {
                final double cs = i * 3.141592653589793 / 180.0;
                final double ps = (i - 1) * 3.141592653589793 / 180.0;
                final double[] outer = { Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius };
                GL11.glVertex2d(x + outer[2], y + outer[3]);
                GL11.glVertex2d(x + outer[0], y + outer[1]);
                GL11.glVertex2d((double)x, (double)y);
            }
            else {
                GL11.glVertex2d(x + radius * Math.cos(i * 6.283185307179586 / points), y + radius * Math.sin(i * 6.283185307179586 / points));
            }
        }
        GL11.glEnd();
        GL11.glDisable(smooth);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static LockedResolution getLockedResolution() {
        final int width = Display.getWidth();
        final int height = Display.getHeight();
        if (width != RenderingUtils.lastWidth || height != RenderingUtils.lastHeight) {
            RenderingUtils.lastWidth = width;
            RenderingUtils.lastHeight = height;
            return RenderingUtils.lockedResolution = new LockedResolution(width / 2, height / 2);
        }
        return RenderingUtils.lockedResolution;
    }
    
    public static ScaledResolution getScaledResolution() {
        final int displayWidth = Display.getWidth();
        final int displayHeight = Display.getHeight();
        final int guiScale = Wrapper.getGameSettings().guiScale;
        if (displayWidth != RenderingUtils.lastScaledWidth || displayHeight != RenderingUtils.lastScaledHeight || guiScale != RenderingUtils.lastGuiScale) {
            RenderingUtils.lastScaledWidth = displayWidth;
            RenderingUtils.lastScaledHeight = displayHeight;
            RenderingUtils.lastGuiScale = guiScale;
            return RenderingUtils.scaledResolution = new ScaledResolution(Wrapper.getMinecraft());
        }
        return RenderingUtils.scaledResolution;
    }
    
    public static int getColorFromPercentage(final float percentage) {
        return Color.HSBtoRGB(Math.min(1.0f, Math.max(0.0f, percentage)) / 3.0f, 0.9f, 0.9f);
    }
    
    public static int getRainbowFromEntity(final long currentMillis, final int speed, final int offset, final boolean invert, final float alpha) {
        final float time = (currentMillis + offset * 300L) % speed / (float)speed;
        final int rainbow = Color.HSBtoRGB(invert ? (1.0f - time) : time, 0.9f, 0.9f);
        final int r = rainbow >> 16 & 0xFF;
        final int g = rainbow >> 8 & 0xFF;
        final int b = rainbow & 0xFF;
        final int a = (int)(alpha * 255.0f);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static int getRainbow(final long currentMillis, final int speed, final int offset) {
        return getRainbow(currentMillis, speed, offset, 1.0f);
    }
    
    public static int getRainbow(final long currentMillis, final int speed, final int offset, final float alpha) {
        final int rainbow = Color.HSBtoRGB(1.0f - (currentMillis + offset * 100) % speed / (float)speed, 0.9f, 0.9f);
        final int r = rainbow >> 16 & 0xFF;
        final int g = rainbow >> 8 & 0xFF;
        final int b = rainbow & 0xFF;
        final int a = (int)(alpha * 255.0f);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static void drawAndRotateArrow(final float x, final float y, final float size, final boolean rotate) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 1.0f);
        OGLUtils.enableBlending();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glBegin(4);
        if (rotate) {
            GL11.glVertex2f(size, size / 2.0f);
            GL11.glVertex2f(size / 2.0f, 0.0f);
            GL11.glVertex2f(0.0f, size / 2.0f);
        }
        else {
            GL11.glVertex2f(0.0f, 0.0f);
            GL11.glVertex2f(size / 2.0f, size / 2.0f);
            GL11.glVertex2f(size, 0.0f);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        OGLUtils.disableBlending();
        GL11.glPopMatrix();
    }
    
    public static double progressiveAnimation(final double now, final double desired, final double speed) {
        final double dif = Math.abs(now - desired);
        final int fps = Minecraft.getDebugFPS();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.05, 144.0 / fps * (dif / 10.0) * speed)), 0.05);
            if (dif != 0.0 && dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }
    
    public static double linearAnimation(final double now, final double desired, final double speed) {
        final double dif = Math.abs(now - desired);
        final int fps = Minecraft.getDebugFPS();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.005, 144.0 / fps * speed)), 0.005);
            if (dif != 0.0 && dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }
    
    public static int darker(final int color, final float factor) {
        final int r = (int)((color >> 16 & 0xFF) * factor);
        final int g = (int)((color >> 8 & 0xFF) * factor);
        final int b = (int)((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }
    
    public static void drawOutlinedString(final FontRenderer fr, final String s, final float x, final float y, final int color, final int outlineColor) {
        fr.drawString(s, x - 0.5f, y, outlineColor);
        fr.drawString(s, x, y - 0.5f, outlineColor);
        fr.drawString(s, x + 0.5f, y, outlineColor);
        fr.drawString(s, x, y + 0.5f, outlineColor);
        fr.drawString(s, x, y, color);
    }
    
    public static void drawImage(final float x, final float y, final float width, final float height, final float r, final float g, final float b, final ResourceLocation image) {
        Wrapper.getMinecraft().getTextureManager().bindTexture(image);
        final float f = 1.0f / width;
        final float f2 = 1.0f / height;
        GL11.glColor4f(r, g, b, 1.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(0.0, height * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex(width * f, height * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex(width * f, 0.0).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
    }
    
    public static int fadeBetween(final int startColor, final int endColor, float progress) {
        if (progress > 1.0f) {
            progress = 1.0f - progress % 1.0f;
        }
        return fadeTo(startColor, endColor, progress);
    }
    
    public static int fadeTo(final int startColor, final int endColor, final float progress) {
        final float invert = 1.0f - progress;
        final int r = (int)((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        final int g = (int)((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        final int b = (int)((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        final int a = (int)((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static double interpolate(final double old, final double now, final float partialTicks) {
        return old + (now - old) * partialTicks;
    }
    
    public static float interpolate(final float old, final float now, final float partialTicks) {
        return old + (now - old) * partialTicks;
    }
    
    public static void drawGuiBackground(final int width, final int height) {
        Gui.drawRect(0.0f, 0.0f, (float)width, (float)height, -14144460);
    }
}
