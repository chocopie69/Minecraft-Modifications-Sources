// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BreakingFour extends BakedQuad
{
    private final TextureAtlasSprite texture;
    
    public BreakingFour(final BakedQuad p_i46217_1_, final TextureAtlasSprite textureIn) {
        super(Arrays.copyOf(p_i46217_1_.getVertexData(), p_i46217_1_.getVertexData().length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()));
        this.texture = textureIn;
        this.func_178217_e();
        this.fixVertexData();
    }
    
    private void func_178217_e() {
        for (int i = 0; i < 4; ++i) {
            this.func_178216_a(i);
        }
    }
    
    private void func_178216_a(final int p_178216_1_) {
        final int i = this.vertexData.length / 4;
        final int j = i * p_178216_1_;
        final float f = Float.intBitsToFloat(this.vertexData[j]);
        final float f2 = Float.intBitsToFloat(this.vertexData[j + 1]);
        final float f3 = Float.intBitsToFloat(this.vertexData[j + 2]);
        float f4 = 0.0f;
        float f5 = 0.0f;
        switch (this.face) {
            case DOWN: {
                f4 = f * 16.0f;
                f5 = (1.0f - f3) * 16.0f;
                break;
            }
            case UP: {
                f4 = f * 16.0f;
                f5 = f3 * 16.0f;
                break;
            }
            case NORTH: {
                f4 = (1.0f - f) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case SOUTH: {
                f4 = f * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case WEST: {
                f4 = f3 * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
            case EAST: {
                f4 = (1.0f - f3) * 16.0f;
                f5 = (1.0f - f2) * 16.0f;
                break;
            }
        }
        this.vertexData[j + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(f4));
        this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(f5));
    }
}
