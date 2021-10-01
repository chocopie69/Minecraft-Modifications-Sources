// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation optionsBackground;
    public static final ResourceLocation statIcons;
    public static final ResourceLocation icons;
    public static Tessellator tessellator;
    public static WorldRenderer worldrenderer;
    protected float zLevel;
    private static final float TEXTURE_FACTOR = 0.00390625f;
    
    static {
        optionsBackground = new ResourceLocation("textures/gui/options_background.png");
        statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
        icons = new ResourceLocation("textures/gui/icons.png");
        Gui.tessellator = Tessellator.getInstance();
        Gui.worldrenderer = Gui.tessellator.getWorldRenderer();
    }
    
    public static void drawRect(final float left, final float top, final float right, final float bottom, final int color) {
        final int alpha = color >> 24 & 0xFF;
        final boolean needBlend = alpha < 255;
        GL11.glDisable(3553);
        if (needBlend) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GlStateManager.enableBlend();
            GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)alpha);
        }
        else {
            GL11.glColor3ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF));
        }
        GL11.glBegin(7);
        GL11.glVertex2f(left, top);
        GL11.glVertex2f(left, bottom);
        GL11.glVertex2f(right, bottom);
        GL11.glVertex2f(right, top);
        GL11.glEnd();
        if (needBlend) {
            GL11.glDisable(3042);
            GlStateManager.disableBlend();
        }
        GL11.glEnable(3553);
    }
    
    public static void drawRect(final double left, final double top, final double right, final double bottom, final int color) {
        final int alpha = color >> 24 & 0xFF;
        final boolean needBlend = alpha < 255;
        GL11.glDisable(3553);
        if (needBlend) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)alpha);
        }
        else {
            GL11.glColor3ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF));
        }
        GL11.glBegin(7);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        if (needBlend) {
            GL11.glDisable(3042);
        }
        GL11.glEnable(3553);
    }
    
    public static void drawModalRectWithCustomSizedTexture(final int x, final int y, final float u, final float v, final int width, final int height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        Gui.worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        Gui.worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f2).endVertex();
        Gui.worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f2).endVertex();
        Gui.worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f2).endVertex();
        Gui.worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        Gui.tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        Gui.worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        Gui.worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f2).endVertex();
        Gui.worldrenderer.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f2).endVertex();
        Gui.worldrenderer.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f2).endVertex();
        Gui.worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        Gui.tessellator.draw();
    }
    
    protected void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect((float)startX, (float)y, (float)(endX + 1), (float)(y + 1), color);
    }
    
    protected void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect((float)x, (float)(startY + 1), (float)(x + 1), (float)endY, color);
    }
    
    public static void drawGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GL11.glDisable(3553);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(7425);
        Gui.worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        Gui.worldrenderer.pos(right, top, 0.0).color4f(f2, f3, f4, f).endVertex();
        Gui.worldrenderer.pos(left, top, 0.0).color4f(f2, f3, f4, f).endVertex();
        Gui.worldrenderer.pos(left, bottom, 0.0).color4f(f6, f7, f8, f5).endVertex();
        Gui.worldrenderer.pos(right, bottom, 0.0).color4f(f6, f7, f8, f5).endVertex();
        Gui.tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glEnable(3553);
    }
    
    public void drawCenteredString(final MinecraftFontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
    
    public void drawString(final MinecraftFontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public static void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float tx = textureX * 0.00390625f;
        final float txw = (textureX + width) * 0.00390625f;
        final float ty = textureY * 0.00390625f;
        final float tyh = (textureY + height) * 0.00390625f;
        GL11.glBegin(7);
        GL11.glTexCoord2f(tx, tyh);
        GL11.glVertex2i(x, y + height);
        GL11.glTexCoord2f(txw, tyh);
        GL11.glVertex2i(x + width, y + height);
        GL11.glTexCoord2f(txw, ty);
        GL11.glVertex2i(x + width, y);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex2i(x, y);
        GL11.glEnd();
    }
    
    public static void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
        final float tx = minU * 0.00390625f;
        final float txw = (minU + maxU) * 0.00390625f;
        final float ty = minV * 0.00390625f;
        final float tyh = (minV + maxV) * 0.00390625f;
        GL11.glBegin(7);
        GL11.glTexCoord2f(tx, tyh);
        GL11.glVertex2f(xCoord, yCoord + maxV);
        GL11.glTexCoord2f(txw, tyh);
        GL11.glVertex2f(xCoord + maxU, yCoord + maxV);
        GL11.glTexCoord2f(txw, ty);
        GL11.glVertex2f(xCoord + maxU, yCoord);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex2f(xCoord, yCoord);
        GL11.glEnd();
    }
    
    public static void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
        final float minU = textureSprite.getMinU();
        final float maxU = textureSprite.getMaxU();
        final float minV = textureSprite.getMinV();
        final float maxV = textureSprite.getMaxV();
        GL11.glBegin(7);
        GL11.glTexCoord2f(minU, maxV);
        GL11.glVertex2i(xCoord, yCoord + heightIn);
        GL11.glTexCoord2f(maxU, maxV);
        GL11.glVertex2i(xCoord + widthIn, yCoord + heightIn);
        GL11.glTexCoord2f(maxU, minV);
        GL11.glVertex2i(xCoord + widthIn, yCoord);
        GL11.glTexCoord2f(minU, minV);
        GL11.glVertex2i(xCoord, yCoord);
        GL11.glEnd();
    }
}
