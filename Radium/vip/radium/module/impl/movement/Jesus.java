// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import vip.radium.utils.Wrapper;
import vip.radium.event.impl.world.BlockCollisionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Jesus", category = ModuleCategory.MOVEMENT)
public final class Jesus extends Module
{
    private boolean onLiquid;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition;
    @EventLink
    private final Listener<BlockCollisionEvent> onBlockCollision;
    
    public Jesus() {
        this.onUpdatePosition = (event -> {
            if (event.isPre() && this.onLiquid && Wrapper.getPlayer().ticksExisted % 2 == 0) {
                event.setPosY(event.getPosY() + 9.999999974752427E-7);
                this.onLiquid = false;
            }
            return;
        });
        BlockPos blockPos;
        double x;
        double y;
        double z;
        this.onBlockCollision = (event -> {
            if (event.getBlock() instanceof BlockLiquid && !Wrapper.getPlayer().isSneaking()) {
                blockPos = event.getBlockPos();
                x = blockPos.getX();
                y = blockPos.getY();
                z = blockPos.getZ();
                this.onLiquid = true;
                event.setBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0 - 9.999999974752427E-7, z + 1.0));
            }
        });
    }
}
