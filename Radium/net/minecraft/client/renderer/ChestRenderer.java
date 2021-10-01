// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;

public class ChestRenderer
{
    public void renderChestBrightness(final Block p_178175_1_, final float color) {
        GL11.glColor4f(color, color, color, 1.0f);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}
