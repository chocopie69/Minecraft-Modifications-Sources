// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import org.lwjgl.opengl.GL11;
import net.optifine.util.TextureUtils;
import net.minecraft.util.MathHelper;
import java.nio.ByteOrder;
import net.minecraft.src.Config;
import net.optifine.SmartAnimations;
import java.util.Arrays;
import com.google.common.primitives.Floats;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.util.BitSet;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class WorldRenderer
{
    public IntBuffer rawIntBuffer;
    public FloatBuffer rawFloatBuffer;
    public int vertexCount;
    public int drawMode;
    public SVertexBuilder sVertexBuilder;
    public RenderEnv renderEnv;
    public BitSet animatedSprites;
    public BitSet animatedSpritesCached;
    private ByteBuffer byteBuffer;
    private ShortBuffer field_181676_c;
    private VertexFormatElement field_181677_f;
    private int field_181678_g;
    private boolean needsUpdate;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private EnumWorldBlockLayer blockLayer;
    private boolean[] drawnIcons;
    private TextureAtlasSprite[] quadSprites;
    private TextureAtlasSprite[] quadSpritesPrev;
    private TextureAtlasSprite quadSprite;
    private boolean modeTriangles;
    private ByteBuffer byteBufferTriangles;
    
    public WorldRenderer(final int bufferSizeIn) {
        this.renderEnv = null;
        this.animatedSprites = null;
        this.animatedSpritesCached = new BitSet();
        this.blockLayer = null;
        this.drawnIcons = new boolean[256];
        this.quadSprites = null;
        this.quadSpritesPrev = null;
        this.quadSprite = null;
        this.modeTriangles = false;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.field_181676_c = this.byteBuffer.asShortBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        SVertexBuilder.initVertexBuilder(this);
    }
    
    private static float func_181665_a(final FloatBuffer p_181665_0_, final float p_181665_1_, final float p_181665_2_, final float p_181665_3_, final int p_181665_4_, final int p_181665_5_) {
        final float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
        final float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
        final float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
        final float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
        final float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
        final float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
        final float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
        final float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
        final float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
        final float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
        final float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
        final float f12 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
        final float f13 = (f + f4 + f7 + f10) * 0.25f - p_181665_1_;
        final float f14 = (f2 + f5 + f8 + f11) * 0.25f - p_181665_2_;
        final float f15 = (f3 + f6 + f9 + f12) * 0.25f - p_181665_3_;
        return f13 * f13 + f14 * f14 + f15 * f15;
    }
    
    private void func_181670_b(final int p_181670_1_) {
        if (p_181670_1_ > this.rawIntBuffer.remaining()) {
            final int i = this.byteBuffer.capacity();
            final int j = i % 2097152;
            final int k = j + (((this.rawIntBuffer.position() + p_181670_1_) * 4 - j) / 2097152 + 1) * 2097152;
            LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + i + " bytes, new size " + k + " bytes.");
            final int l = this.rawIntBuffer.position();
            final ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(k);
            this.byteBuffer.position(0);
            bytebuffer.put(this.byteBuffer);
            bytebuffer.rewind();
            this.byteBuffer = bytebuffer;
            this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
            (this.rawIntBuffer = this.byteBuffer.asIntBuffer()).position(l);
            (this.field_181676_c = this.byteBuffer.asShortBuffer()).position(l << 1);
            if (this.quadSprites != null) {
                final TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
                final int i2 = this.getBufferQuadSize();
                System.arraycopy(atextureatlassprite, 0, this.quadSprites = new TextureAtlasSprite[i2], 0, Math.min(atextureatlassprite.length, this.quadSprites.length));
                this.quadSpritesPrev = null;
            }
        }
    }
    
    public void func_181674_a(final float p_181674_1_, final float p_181674_2_, final float p_181674_3_) {
        final int i = this.vertexCount / 4;
        final float[] afloat = new float[i];
        for (int j = 0; j < i; ++j) {
            afloat[j] = func_181665_a(this.rawFloatBuffer, (float)(p_181674_1_ + this.xOffset), (float)(p_181674_2_ + this.yOffset), (float)(p_181674_3_ + this.zOffset), this.vertexFormat.func_181719_f(), j * this.vertexFormat.getNextOffset());
        }
        final Integer[] ainteger = new Integer[i];
        for (int k = 0; k < ainteger.length; ++k) {
            ainteger[k] = k;
        }
        Arrays.sort(ainteger, new Comparator<Integer>() {
            @Override
            public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
                return Floats.compare(afloat[p_compare_2_], afloat[p_compare_1_]);
            }
        });
        final BitSet bitset = new BitSet();
        final int l = this.vertexFormat.getNextOffset();
        final int[] aint = new int[l];
        for (int l2 = 0; (l2 = bitset.nextClearBit(l2)) < ainteger.length; ++l2) {
            final int i2 = ainteger[l2];
            if (i2 != l2) {
                this.rawIntBuffer.limit(i2 * l + l);
                this.rawIntBuffer.position(i2 * l);
                this.rawIntBuffer.get(aint);
                for (int j2 = i2, k2 = ainteger[i2]; j2 != l2; j2 = k2, k2 = ainteger[k2]) {
                    this.rawIntBuffer.limit(k2 * l + l);
                    this.rawIntBuffer.position(k2 * l);
                    final IntBuffer intbuffer = this.rawIntBuffer.slice();
                    this.rawIntBuffer.limit(j2 * l + l);
                    this.rawIntBuffer.position(j2 * l);
                    this.rawIntBuffer.put(intbuffer);
                    bitset.set(j2);
                }
                this.rawIntBuffer.limit(l2 * l + l);
                this.rawIntBuffer.position(l2 * l);
                this.rawIntBuffer.put(aint);
            }
            bitset.set(l2);
        }
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(this.func_181664_j());
        if (this.quadSprites != null) {
            final TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.vertexCount / 4];
            final int i3 = this.vertexFormat.getNextOffset() / 4 * 4;
            for (int j3 = 0; j3 < ainteger.length; ++j3) {
                final int k3 = ainteger[j3];
                atextureatlassprite[j3] = this.quadSprites[k3];
            }
            System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
        }
    }
    
    public State func_181672_a() {
        this.rawIntBuffer.rewind();
        final int i = this.func_181664_j();
        this.rawIntBuffer.limit(i);
        final int[] aint = new int[i];
        this.rawIntBuffer.get(aint);
        this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
        this.rawIntBuffer.position(i);
        TextureAtlasSprite[] atextureatlassprite = null;
        if (this.quadSprites != null) {
            final int j = this.vertexCount / 4;
            atextureatlassprite = new TextureAtlasSprite[j];
            System.arraycopy(this.quadSprites, 0, atextureatlassprite, 0, j);
        }
        return new State(aint, new VertexFormat(this.vertexFormat), atextureatlassprite);
    }
    
    public int func_181664_j() {
        return this.vertexCount * this.vertexFormat.func_181719_f();
    }
    
    public void setVertexState(final State state) {
        this.rawIntBuffer.clear();
        this.func_181670_b(state.getRawBuffer().length);
        this.rawIntBuffer.put(state.getRawBuffer());
        this.vertexCount = state.getVertexCount();
        this.vertexFormat = new VertexFormat(state.getVertexFormat());
        if (state.stateQuadSprites != null) {
            if (this.quadSprites == null) {
                this.quadSprites = this.quadSpritesPrev;
            }
            if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
            }
            final TextureAtlasSprite[] atextureatlassprite = state.stateQuadSprites;
            System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
        }
        else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }
    
    public void reset() {
        this.vertexCount = 0;
        this.field_181677_f = null;
        this.field_181678_g = 0;
        this.quadSprite = null;
        if (SmartAnimations.isActive()) {
            if (this.animatedSprites == null) {
                this.animatedSprites = this.animatedSpritesCached;
            }
            this.animatedSprites.clear();
        }
        else if (this.animatedSprites != null) {
            this.animatedSprites = null;
        }
        this.modeTriangles = false;
    }
    
    public void begin(final int glMode, final VertexFormat vertexFormat) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = glMode;
        this.vertexFormat = vertexFormat;
        this.field_181677_f = vertexFormat.getElement(this.field_181678_g);
        this.needsUpdate = false;
        this.byteBuffer.limit(this.byteBuffer.capacity());
        if (Config.isShaders()) {
            SVertexBuilder.endSetVertexFormat(this);
        }
        if (Config.isMultiTexture()) {
            if (this.blockLayer != null) {
                if (this.quadSprites == null) {
                    this.quadSprites = this.quadSpritesPrev;
                }
                if (this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                    this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
                }
            }
        }
        else {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
        }
    }
    
    public WorldRenderer tex(double p_181673_1_, double p_181673_3_) {
        if (this.quadSprite != null && this.quadSprites != null) {
            p_181673_1_ = this.quadSprite.toSingleU((float)p_181673_1_);
            p_181673_3_ = this.quadSprite.toSingleV((float)p_181673_3_);
            this.quadSprites[this.vertexCount / 4] = this.quadSprite;
        }
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)p_181673_1_);
                this.byteBuffer.putFloat(i + 4, (float)p_181673_3_);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)p_181673_1_);
                this.byteBuffer.putInt(i + 4, (int)p_181673_3_);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)p_181673_3_);
                this.byteBuffer.putShort(i + 2, (short)p_181673_1_);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)p_181673_3_);
                this.byteBuffer.put(i + 1, (byte)p_181673_1_);
                break;
            }
        }
        this.func_181667_k();
        return this;
    }
    
    public WorldRenderer func_181671_a(final int p_181671_1_, final int p_181671_2_) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)p_181671_1_);
                this.byteBuffer.putFloat(i + 4, (float)p_181671_2_);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, p_181671_1_);
                this.byteBuffer.putInt(i + 4, p_181671_2_);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)p_181671_2_);
                this.byteBuffer.putShort(i + 2, (short)p_181671_1_);
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)p_181671_2_);
                this.byteBuffer.put(i + 1, (byte)p_181671_1_);
                break;
            }
        }
        this.func_181667_k();
        return this;
    }
    
    public void putBrightness4(final int p_178962_1_, final int p_178962_2_, final int p_178962_3_, final int p_178962_4_) {
        final int i = (this.vertexCount - 4) * this.vertexFormat.func_181719_f() + this.vertexFormat.getElementOffsetById(1) / 4;
        final int j = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(i, p_178962_1_);
        this.rawIntBuffer.put(i + j, p_178962_2_);
        this.rawIntBuffer.put(i + j * 2, p_178962_3_);
        this.rawIntBuffer.put(i + j * 3, p_178962_4_);
    }
    
    public void putPosition(final double x, final double y, final double z) {
        final int i = this.vertexFormat.func_181719_f();
        final int j = (this.vertexCount - 4) * i;
        for (int k = 0; k < 4; ++k) {
            final int l = j + k * i;
            final int i2 = l + 1;
            final int j2 = i2 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(x + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
            this.rawIntBuffer.put(i2, Float.floatToRawIntBits((float)(y + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i2))));
            this.rawIntBuffer.put(j2, Float.floatToRawIntBits((float)(z + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j2))));
        }
    }
    
    public int getColorIndex(final int p_78909_1_) {
        return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }
    
    public void putColorMultiplier(final float red, final float green, final float blue, final int p_178978_4_) {
        final int i = this.getColorIndex(p_178978_4_);
        int j = -1;
        if (!this.needsUpdate) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                final int k = (int)((j & 0xFF) * red);
                final int l = (int)((j >> 8 & 0xFF) * green);
                final int i2 = (int)((j >> 16 & 0xFF) * blue);
                j &= 0xFF000000;
                j = (j | i2 << 16 | l << 8 | k);
            }
            else {
                final int j2 = (int)((j >> 24 & 0xFF) * red);
                final int k2 = (int)((j >> 16 & 0xFF) * green);
                final int l2 = (int)((j >> 8 & 0xFF) * blue);
                j &= 0xFF;
                j = (j | j2 << 24 | k2 << 16 | l2 << 8);
            }
        }
        this.rawIntBuffer.put(i, j);
    }
    
    private void putColor(final int argb, final int p_178988_2_) {
        final int i = this.getColorIndex(p_178988_2_);
        final int j = argb >> 16 & 0xFF;
        final int k = argb >> 8 & 0xFF;
        final int l = argb & 0xFF;
        final int i2 = argb >> 24 & 0xFF;
        this.putColorRGBA(i, j, k, l, i2);
    }
    
    public void putColorRGB_F(final float red, final float green, final float blue, final int p_178994_4_) {
        final int i = this.getColorIndex(p_178994_4_);
        final int j = MathHelper.clamp_int((int)(red * 255.0f), 0, 255);
        final int k = MathHelper.clamp_int((int)(green * 255.0f), 0, 255);
        final int l = MathHelper.clamp_int((int)(blue * 255.0f), 0, 255);
        this.putColorRGBA(i, j, k, l, 255);
    }
    
    public void putColorRGBA(final int index, final int red, final int p_178972_3_, final int p_178972_4_, final int p_178972_5_) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(index, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | red);
        }
        else {
            this.rawIntBuffer.put(index, red << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
        }
    }
    
    public void markDirty() {
        this.needsUpdate = true;
    }
    
    public WorldRenderer color(final int hex) {
        return this.color4i(hex >> 16 & 0xFF, hex >> 8 & 0xFF, hex & 0xFF, hex >> 24 & 0xFF);
    }
    
    public WorldRenderer color4f(final float p_181666_1_, final float p_181666_2_, final float p_181666_3_, final float p_181666_4_) {
        return this.color4i((int)(p_181666_1_ * 255.0f), (int)(p_181666_2_ * 255.0f), (int)(p_181666_3_ * 255.0f), (int)(p_181666_4_ * 255.0f));
    }
    
    public WorldRenderer color4i(final int p_181669_1_, final int p_181669_2_, final int p_181669_3_, final int p_181669_4_) {
        if (this.needsUpdate) {
            return this;
        }
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, p_181669_1_ / 255.0f);
                this.byteBuffer.putFloat(i + 4, p_181669_2_ / 255.0f);
                this.byteBuffer.putFloat(i + 8, p_181669_3_ / 255.0f);
                this.byteBuffer.putFloat(i + 12, p_181669_4_ / 255.0f);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putFloat(i, (float)p_181669_1_);
                this.byteBuffer.putFloat(i + 4, (float)p_181669_2_);
                this.byteBuffer.putFloat(i + 8, (float)p_181669_3_);
                this.byteBuffer.putFloat(i + 12, (float)p_181669_4_);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)p_181669_1_);
                this.byteBuffer.putShort(i + 2, (short)p_181669_2_);
                this.byteBuffer.putShort(i + 4, (short)p_181669_3_);
                this.byteBuffer.putShort(i + 6, (short)p_181669_4_);
                break;
            }
            case UBYTE:
            case BYTE: {
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    this.byteBuffer.put(i, (byte)p_181669_1_);
                    this.byteBuffer.put(i + 1, (byte)p_181669_2_);
                    this.byteBuffer.put(i + 2, (byte)p_181669_3_);
                    this.byteBuffer.put(i + 3, (byte)p_181669_4_);
                    break;
                }
                this.byteBuffer.put(i, (byte)p_181669_4_);
                this.byteBuffer.put(i + 1, (byte)p_181669_3_);
                this.byteBuffer.put(i + 2, (byte)p_181669_2_);
                this.byteBuffer.put(i + 3, (byte)p_181669_1_);
                break;
            }
        }
        this.func_181667_k();
        return this;
    }
    
    public void addVertexData(final int[] vertexData) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertexData(this, vertexData);
        }
        this.func_181670_b(vertexData.length);
        this.rawIntBuffer.position(this.func_181664_j());
        this.rawIntBuffer.put(vertexData);
        this.vertexCount += vertexData.length / this.vertexFormat.func_181719_f();
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertexData(this);
        }
    }
    
    public void endVertex() {
        ++this.vertexCount;
        this.func_181670_b(this.vertexFormat.func_181719_f());
        this.field_181678_g = 0;
        this.field_181677_f = this.vertexFormat.getElement(this.field_181678_g);
        if (Config.isShaders()) {
            SVertexBuilder.endAddVertex(this);
        }
    }
    
    public WorldRenderer pos(final double p_181662_1_, final double p_181662_3_, final double p_181662_5_) {
        if (Config.isShaders()) {
            SVertexBuilder.beginAddVertex(this);
        }
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, (float)(p_181662_1_ + this.xOffset));
                this.byteBuffer.putFloat(i + 4, (float)(p_181662_3_ + this.yOffset));
                this.byteBuffer.putFloat(i + 8, (float)(p_181662_5_ + this.zOffset));
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, Float.floatToRawIntBits((float)(p_181662_1_ + this.xOffset)));
                this.byteBuffer.putInt(i + 4, Float.floatToRawIntBits((float)(p_181662_3_ + this.yOffset)));
                this.byteBuffer.putInt(i + 8, Float.floatToRawIntBits((float)(p_181662_5_ + this.zOffset)));
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)(p_181662_1_ + this.xOffset));
                this.byteBuffer.putShort(i + 2, (short)(p_181662_3_ + this.yOffset));
                this.byteBuffer.putShort(i + 4, (short)(p_181662_5_ + this.zOffset));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)(p_181662_1_ + this.xOffset));
                this.byteBuffer.put(i + 1, (byte)(p_181662_3_ + this.yOffset));
                this.byteBuffer.put(i + 2, (byte)(p_181662_5_ + this.zOffset));
                break;
            }
        }
        this.func_181667_k();
        return this;
    }
    
    public void putNormal(final float x, final float y, final float z) {
        final int i = (byte)(x * 127.0f) & 0xFF;
        final int j = (byte)(y * 127.0f) & 0xFF;
        final int k = (byte)(z * 127.0f) & 0xFF;
        final int l = i | j << 8 | k << 16;
        final int i2 = this.vertexFormat.getNextOffset() >> 2;
        final int j2 = (this.vertexCount - 4) * i2 + this.vertexFormat.getNormalOffset() / 4;
        this.rawIntBuffer.put(j2, l);
        this.rawIntBuffer.put(j2 + i2, l);
        this.rawIntBuffer.put(j2 + i2 * 2, l);
        this.rawIntBuffer.put(j2 + i2 * 3, l);
    }
    
    private void func_181667_k() {
        ++this.field_181678_g;
        this.field_181678_g %= this.vertexFormat.getElementCount();
        this.field_181677_f = this.vertexFormat.getElement(this.field_181678_g);
        if (this.field_181677_f.getUsage() == VertexFormatElement.EnumUsage.PADDING) {
            this.func_181667_k();
        }
    }
    
    public WorldRenderer func_181663_c(final float p_181663_1_, final float p_181663_2_, final float p_181663_3_) {
        final int i = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.func_181720_d(this.field_181678_g);
        switch (this.field_181677_f.getType()) {
            case FLOAT: {
                this.byteBuffer.putFloat(i, p_181663_1_);
                this.byteBuffer.putFloat(i + 4, p_181663_2_);
                this.byteBuffer.putFloat(i + 8, p_181663_3_);
                break;
            }
            case UINT:
            case INT: {
                this.byteBuffer.putInt(i, (int)p_181663_1_);
                this.byteBuffer.putInt(i + 4, (int)p_181663_2_);
                this.byteBuffer.putInt(i + 8, (int)p_181663_3_);
                break;
            }
            case USHORT:
            case SHORT: {
                this.byteBuffer.putShort(i, (short)((int)(p_181663_1_ * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 2, (short)((int)(p_181663_2_ * 32767.0f) & 0xFFFF));
                this.byteBuffer.putShort(i + 4, (short)((int)(p_181663_3_ * 32767.0f) & 0xFFFF));
                break;
            }
            case UBYTE:
            case BYTE: {
                this.byteBuffer.put(i, (byte)((int)(p_181663_1_ * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 1, (byte)((int)(p_181663_2_ * 127.0f) & 0xFF));
                this.byteBuffer.put(i + 2, (byte)((int)(p_181663_3_ * 127.0f) & 0xFF));
                break;
            }
        }
        this.func_181667_k();
        return this;
    }
    
    public void setTranslation(final double x, final double y, final double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
    }
    
    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        }
        this.isDrawing = false;
        this.byteBuffer.position(0);
        this.byteBuffer.limit(this.func_181664_j() * 4);
    }
    
    public ByteBuffer getByteBuffer() {
        return this.modeTriangles ? this.byteBufferTriangles : this.byteBuffer;
    }
    
    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }
    
    public int getVertexCount() {
        return this.modeTriangles ? (this.vertexCount / 4 * 6) : this.vertexCount;
    }
    
    public int getDrawMode() {
        return this.modeTriangles ? 4 : this.drawMode;
    }
    
    public void putColor4(final int argb) {
        for (int i = 0; i < 4; ++i) {
            this.putColor(argb, i + 1);
        }
    }
    
    public void putColorRGB_F4(final float red, final float green, final float blue) {
        for (int i = 0; i < 4; ++i) {
            this.putColorRGB_F(red, green, blue, i + 1);
        }
    }
    
    public void putSprite(final TextureAtlasSprite p_putSprite_1_) {
        if (this.animatedSprites != null && p_putSprite_1_ != null && p_putSprite_1_.getAnimationIndex() >= 0) {
            this.animatedSprites.set(p_putSprite_1_.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            final int i = this.vertexCount / 4;
            this.quadSprites[i - 1] = p_putSprite_1_;
        }
    }
    
    public void setSprite(final TextureAtlasSprite p_setSprite_1_) {
        if (this.animatedSprites != null && p_setSprite_1_ != null && p_setSprite_1_.getAnimationIndex() >= 0) {
            this.animatedSprites.set(p_setSprite_1_.getAnimationIndex());
        }
        if (this.quadSprites != null) {
            this.quadSprite = p_setSprite_1_;
        }
    }
    
    public boolean isMultiTexture() {
        return this.quadSprites != null;
    }
    
    public void drawMultiTexture() {
        if (this.quadSprites != null) {
            final int i = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
            if (this.drawnIcons.length <= i) {
                this.drawnIcons = new boolean[i + 1];
            }
            Arrays.fill(this.drawnIcons, false);
            int j = 0;
            int k = -1;
            for (int l = this.vertexCount / 4, i2 = 0; i2 < l; ++i2) {
                final TextureAtlasSprite textureatlassprite = this.quadSprites[i2];
                if (textureatlassprite != null) {
                    final int j2 = textureatlassprite.getIndexInMap();
                    if (!this.drawnIcons[j2]) {
                        if (textureatlassprite == TextureUtils.iconGrassSideOverlay) {
                            if (k < 0) {
                                k = i2;
                            }
                        }
                        else {
                            i2 = this.drawForIcon(textureatlassprite, i2) - 1;
                            ++j;
                            if (this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT) {
                                this.drawnIcons[j2] = true;
                            }
                        }
                    }
                }
            }
            if (k >= 0) {
                this.drawForIcon(TextureUtils.iconGrassSideOverlay, k);
                ++j;
            }
        }
    }
    
    private int drawForIcon(final TextureAtlasSprite p_drawForIcon_1_, final int p_drawForIcon_2_) {
        GL11.glBindTexture(3553, p_drawForIcon_1_.glSpriteTextureId);
        int i = -1;
        int j = -1;
        final int k = this.vertexCount / 4;
        for (int l = p_drawForIcon_2_; l < k; ++l) {
            final TextureAtlasSprite textureatlassprite = this.quadSprites[l];
            if (textureatlassprite == p_drawForIcon_1_) {
                if (j < 0) {
                    j = l;
                }
            }
            else if (j >= 0) {
                this.draw(j, l);
                if (this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT) {
                    return l;
                }
                j = -1;
                if (i < 0) {
                    i = l;
                }
            }
        }
        if (j >= 0) {
            this.draw(j, k);
        }
        if (i < 0) {
            i = k;
        }
        return i;
    }
    
    private void draw(final int p_draw_1_, final int p_draw_2_) {
        final int i = p_draw_2_ - p_draw_1_;
        if (i > 0) {
            final int j = p_draw_1_ * 4;
            final int k = i * 4;
            GL11.glDrawArrays(this.drawMode, j, k);
        }
    }
    
    private int getBufferQuadSize() {
        final int i = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.func_181719_f() * 4);
        return i;
    }
    
    public RenderEnv getRenderEnv(final IBlockState p_getRenderEnv_1_, final BlockPos p_getRenderEnv_2_) {
        if (this.renderEnv == null) {
            return this.renderEnv = new RenderEnv(p_getRenderEnv_1_, p_getRenderEnv_2_);
        }
        this.renderEnv.reset(p_getRenderEnv_1_, p_getRenderEnv_2_);
        return this.renderEnv;
    }
    
    public boolean isDrawing() {
        return this.isDrawing;
    }
    
    public double getXOffset() {
        return this.xOffset;
    }
    
    public double getYOffset() {
        return this.yOffset;
    }
    
    public double getZOffset() {
        return this.zOffset;
    }
    
    public EnumWorldBlockLayer getBlockLayer() {
        return this.blockLayer;
    }
    
    public void setBlockLayer(final EnumWorldBlockLayer p_setBlockLayer_1_) {
        this.blockLayer = p_setBlockLayer_1_;
        if (p_setBlockLayer_1_ == null) {
            if (this.quadSprites != null) {
                this.quadSpritesPrev = this.quadSprites;
            }
            this.quadSprites = null;
            this.quadSprite = null;
        }
    }
    
    public void putColorMultiplierRgba(final float p_putColorMultiplierRgba_1_, final float p_putColorMultiplierRgba_2_, final float p_putColorMultiplierRgba_3_, final float p_putColorMultiplierRgba_4_, final int p_putColorMultiplierRgba_5_) {
        final int i = this.getColorIndex(p_putColorMultiplierRgba_5_);
        int j = -1;
        if (!this.needsUpdate) {
            j = this.rawIntBuffer.get(i);
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                final int k = (int)((j & 0xFF) * p_putColorMultiplierRgba_1_);
                final int l = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_2_);
                final int i2 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_3_);
                final int j2 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_4_);
                j = (j2 << 24 | i2 << 16 | l << 8 | k);
            }
            else {
                final int k2 = (int)((j >> 24 & 0xFF) * p_putColorMultiplierRgba_1_);
                final int l2 = (int)((j >> 16 & 0xFF) * p_putColorMultiplierRgba_2_);
                final int i3 = (int)((j >> 8 & 0xFF) * p_putColorMultiplierRgba_3_);
                final int j3 = (int)((j & 0xFF) * p_putColorMultiplierRgba_4_);
                j = (k2 << 24 | l2 << 16 | i3 << 8 | j3);
            }
        }
        this.rawIntBuffer.put(i, j);
    }
    
    public void quadsToTriangles() {
        if (this.drawMode == 7) {
            if (this.byteBufferTriangles == null) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }
            if (this.byteBufferTriangles.capacity() < this.byteBuffer.capacity() * 2) {
                this.byteBufferTriangles = GLAllocation.createDirectByteBuffer(this.byteBuffer.capacity() * 2);
            }
            final int i = this.vertexFormat.getNextOffset();
            final int j = this.byteBuffer.limit();
            this.byteBuffer.rewind();
            this.byteBufferTriangles.clear();
            for (int k = 0; k < this.vertexCount; k += 4) {
                this.byteBuffer.limit((k + 3) * i);
                this.byteBuffer.position(k * i);
                this.byteBufferTriangles.put(this.byteBuffer);
                this.byteBuffer.limit((k + 1) * i);
                this.byteBuffer.position(k * i);
                this.byteBufferTriangles.put(this.byteBuffer);
                this.byteBuffer.limit((k + 2 + 2) * i);
                this.byteBuffer.position((k + 2) * i);
                this.byteBufferTriangles.put(this.byteBuffer);
            }
            this.byteBuffer.limit(j);
            this.byteBuffer.rewind();
            this.byteBufferTriangles.flip();
            this.modeTriangles = true;
        }
    }
    
    public boolean isColorDisabled() {
        return this.needsUpdate;
    }
    
    public class State
    {
        private final int[] stateRawBuffer;
        private final VertexFormat stateVertexFormat;
        private TextureAtlasSprite[] stateQuadSprites;
        
        public State(final int[] p_i1_2_, final VertexFormat p_i1_3_, final TextureAtlasSprite[] p_i1_4_) {
            this.stateRawBuffer = p_i1_2_;
            this.stateVertexFormat = p_i1_3_;
            this.stateQuadSprites = p_i1_4_;
        }
        
        public State(final int[] p_i46453_2_, final VertexFormat p_i46453_3_) {
            this.stateRawBuffer = p_i46453_2_;
            this.stateVertexFormat = p_i46453_3_;
        }
        
        public int[] getRawBuffer() {
            return this.stateRawBuffer;
        }
        
        public int getVertexCount() {
            return this.stateRawBuffer.length / this.stateVertexFormat.func_181719_f();
        }
        
        public VertexFormat getVertexFormat() {
            return this.stateVertexFormat;
        }
    }
}
