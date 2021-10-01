// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.font;

import vip.radium.utils.render.OGLUtils;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import java.awt.Font;
import java.util.Random;

public class TrueTypeFontRenderer implements FontRenderer
{
    private static final char COLOR_INVOKER = '§';
    private static final Random RANDOM;
    private final Font font;
    private final CharacterData[] charData;
    private final int[] colorCodes;
    private final int margin;
    private final boolean antiAlias;
    private final boolean fracMetrics;
    
    static {
        RANDOM = new Random();
    }
    
    public TrueTypeFontRenderer(final Font font, final boolean antiAlias, final boolean fracMetrics) {
        this.charData = new CharacterData[256];
        this.colorCodes = new int[32];
        this.generateColors();
        this.font = font;
        this.margin = 6;
        this.antiAlias = antiAlias;
        this.fracMetrics = fracMetrics;
    }
    
    @Override
    public int drawString(final String text, final float x, final float y, final int color) {
        this.renderString(text, x, y, color, false);
        return 0;
    }
    
    @Override
    public int drawStringWithShadow(final String text, final float x, final float y, final int color) {
        final double s = 0.5;
        GL11.glTranslated(s, s, 0.0);
        this.renderString(text, x, y, color, true);
        GL11.glTranslated(-s, -s, 0.0);
        this.renderString(text, x, y, color, false);
        return 0;
    }
    
    @Override
    public float getWidth(final String text) {
        if (text == null || text.length() == 0) {
            return 0.0f;
        }
        float width = 0.0f;
        final CharacterData[] characterData = this.charData;
        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            if (character != '§' && ((i > 0) ? text.charAt(i - 1) : '.') != '§') {
                if (this.isValid(character)) {
                    final CharacterData charData = characterData[character];
                    width += (charData.width - 2 * this.margin) / 2.0f;
                }
            }
        }
        return width;
    }
    
    @Override
    public float getHeight(final String text) {
        float height = 0.0f;
        final CharacterData[] characterData = this.charData;
        for (int length = text.length(), i = 0; i < length; ++i) {
            final char character = text.charAt(i);
            if (((i > 0) ? text.charAt(i - 1) : '.') != '§' && character != '§') {
                if (this.isValid(character)) {
                    final CharacterData charData = characterData[character];
                    height = Math.max(height, charData.height);
                }
            }
        }
        return (height - this.margin) / 2.0f;
    }
    
    public void generateTextures() {
        for (int i = 0; i < 256; ++i) {
            final char c = (char)i;
            if (this.isValid(c)) {
                this.setup(c);
            }
        }
    }
    
    private void setup(final char character) {
        final BufferedImage utilityImage = new BufferedImage(1, 1, 2);
        final Graphics2D utilityGraphics = (Graphics2D)utilityImage.getGraphics();
        utilityGraphics.setFont(this.font);
        final FontMetrics fontMetrics = utilityGraphics.getFontMetrics();
        final Rectangle2D characterBounds = fontMetrics.getStringBounds(String.valueOf(character), utilityGraphics);
        final BufferedImage characterImage = new BufferedImage((int)StrictMath.ceil(characterBounds.getWidth() + 2 * this.margin), (int)StrictMath.ceil(characterBounds.getHeight()), 2);
        final Graphics2D graphics = (Graphics2D)characterImage.getGraphics();
        graphics.setFont(this.font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
        graphics.setColor(Color.WHITE);
        if (this.antiAlias) {
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        if (this.fracMetrics) {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        graphics.drawString(String.valueOf(character), this.margin, fontMetrics.getAscent());
        final int textureId = GL11.glGenTextures();
        this.createTexture(textureId, characterImage);
        this.charData[character] = new CharacterData((float)characterImage.getWidth(), (float)characterImage.getHeight(), textureId);
    }
    
    private void createTexture(final int textureId, final BufferedImage image) {
        final int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                final int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte)(pixel >> 16 & 0xFF));
                buffer.put((byte)(pixel >> 8 & 0xFF));
                buffer.put((byte)(pixel & 0xFF));
                buffer.put((byte)(pixel >> 24 & 0xFF));
            }
        }
        buffer.flip();
        GL11.glBindTexture(3553, textureId);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
    }
    
    private void renderString(final CharSequence text, float x, float y, int color, final boolean shadow) {
        if (text == null || text.length() == 0) {
            return;
        }
        GL11.glPushMatrix();
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (color == 553648127) {
            color = -5263441;
        }
        GL11.glScaled(0.5, 0.5, 1.0);
        x -= this.margin / 2;
        y -= 2.0f;
        x *= 2.0f;
        y *= 2.0f;
        CharacterData[] characterData = this.charData;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;
        final int length = text.length();
        final float multiplier = (float)(shadow ? 4 : 1);
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        OGLUtils.enableBlending();
        GL11.glColor4f(r / multiplier, g / multiplier, b / multiplier, a);
        for (int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            final char previous = (i > 0) ? text.charAt(i - 1) : '.';
            if (previous != '§') {
                if (character == '§') {
                    int index = "0123456789ABCDEFKLMNOR".indexOf(text.charAt(i + 1));
                    if (index < 16) {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.charData;
                        if (index < 0) {
                            index = 15;
                        }
                        if (shadow) {
                            index += 16;
                        }
                        final int textColor = this.colorCodes[index];
                        GL11.glColor4f((textColor >> 16) / 255.0f, (textColor >> 8 & 0xFF) / 255.0f, (textColor & 0xFF) / 255.0f, a);
                    }
                    else if (index == 16) {
                        obfuscated = true;
                    }
                    else if (index == 18) {
                        strikethrough = true;
                    }
                    else if (index == 19) {
                        underlined = true;
                    }
                    else {
                        obfuscated = false;
                        strikethrough = false;
                        underlined = false;
                        characterData = this.charData;
                        GL11.glColor4d((double)(1.0f / multiplier), (double)(1.0f / multiplier), (double)(1.0f / multiplier), (double)a);
                    }
                }
                else if (this.isValid(character)) {
                    if (obfuscated) {
                        character += (char)TrueTypeFontRenderer.RANDOM.nextInt(Math.max(0, '\u0100' - character));
                    }
                    this.drawChar(characterData[character], x, y);
                    final CharacterData charData = characterData[character];
                    if (strikethrough) {
                        this.drawLine(0.0f, charData.height / 2.0f, charData.width, charData.height / 2.0f, 3.0f);
                    }
                    if (underlined) {
                        this.drawLine(0.0f, charData.height - 15.0f, charData.width, charData.height - 15.0f, 3.0f);
                    }
                    x += charData.width - 2 * this.margin;
                }
            }
        }
        OGLUtils.disableBlending();
        GL11.glPopMatrix();
    }
    
    private boolean isValid(final char c) {
        return c > '\n' && c < '\u0100' && c != '\u007f';
    }
    
    private void drawChar(final CharacterData characterData, final float x, final float y) {
        characterData.bind();
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2d((double)x, (double)(y + characterData.height));
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2d((double)(x + characterData.width), (double)(y + characterData.height));
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2d((double)(x + characterData.width), (double)y);
        GL11.glEnd();
    }
    
    private void drawLine(final float x, final float y, final float x2, final float y2, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
        GL11.glEnable(3553);
    }
    
    private void generateColors() {
        for (int i = 0; i < 32; ++i) {
            final int thingy = (i >> 3 & 0x1) * 85;
            int red = (i >> 2 & 0x1) * 170 + thingy;
            int green = (i >> 1 & 0x1) * 170 + thingy;
            int blue = (i & 0x1) * 170 + thingy;
            if (i == 6) {
                red += 85;
            }
            if (i >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCodes[i] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }
    
    private static class CharacterData
    {
        private final int textureId;
        public float width;
        public float height;
        
        public CharacterData(final float width, final float height, final int textureId) {
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }
        
        public void bind() {
            GL11.glBindTexture(3553, this.textureId);
        }
    }
}
