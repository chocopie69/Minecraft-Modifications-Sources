// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import vip.radium.module.ModuleManager;
import net.minecraft.network.Packet;
import vip.radium.utils.Wrapper;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.utils.MovementUtils;
import vip.radium.event.CancellableEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UseItemEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "No Slowdown", category = ModuleCategory.MOVEMENT)
public final class NoSlowdown extends Module
{
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    private final Property<Boolean> ncpProperty;
    @EventLink
    public final Listener<UseItemEvent> onUseItemEvent;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    static {
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
    
    public NoSlowdown() {
        this.ncpProperty = new Property<Boolean>("NCP", true);
        this.onUseItemEvent = CancellableEvent::setCancelled;
        this.onUpdatePositionEvent = (e -> {
            if (this.ncpProperty.getValue() && MovementUtils.isMoving() && !KillAura.isBlocking() && Wrapper.getPlayer().isBlocking()) {
                if (e.isPre()) {
                    Wrapper.sendPacketDirect(NoSlowdown.PLAYER_DIGGING);
                }
                else {
                    Wrapper.sendPacketDirect(NoSlowdown.BLOCK_PLACEMENT);
                }
            }
        });
    }
    
    public static boolean isNoSlowdownEnabled() {
        return ModuleManager.getInstance(NoSlowdown.class).isEnabled();
    }
}
