// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.world;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import vip.radium.event.CancellableEvent;

public final class BlockCollisionEvent extends CancellableEvent
{
    private final Block block;
    private final BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    
    public BlockCollisionEvent(final Block block, final BlockPos blockPos, final AxisAlignedBB boundingBox) {
        this.block = block;
        this.blockPos = blockPos;
        this.boundingBox = boundingBox;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
