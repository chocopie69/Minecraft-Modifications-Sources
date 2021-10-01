// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.utils.Wrapper;
import vip.radium.utils.PlayerInfoCache;
import vip.radium.utils.MovementUtils;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.MoveEntityEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Long Jump", category = ModuleCategory.MOVEMENT)
public final class LongJump extends Module
{
    private final EnumProperty<LongJumpMode> longJumpModeProperty;
    private double moveSpeed;
    @EventLink
    public final Listener<MoveEntityEvent> onMoveEntityEvent;
    private int groundTicks;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public LongJump() {
        this.longJumpModeProperty = new EnumProperty<LongJumpMode>("Mode", LongJumpMode.WATCHDOG);
        double baseMoveSpeed;
        double lastDist;
        final EntityPlayerSP entityPlayerSP;
        final double motionY;
        this.onMoveEntityEvent = (e -> {
            if (MovementUtils.isMoving()) {
                baseMoveSpeed = PlayerInfoCache.getBaseMoveSpeed();
                if (MovementUtils.isOnGround()) {
                    lastDist = PlayerInfoCache.getLastDist();
                    if (MovementUtils.isOnIce()) {
                        baseMoveSpeed *= 2.5;
                    }
                    this.moveSpeed = Math.min(2.6 * baseMoveSpeed, Math.max(baseMoveSpeed * 2.149000095319934, lastDist * 2.149000095319934));
                    Wrapper.getPlayer();
                    MovementUtils.getJumpHeight();
                    e.setY(entityPlayerSP.motionY = motionY);
                }
                else {
                    this.moveSpeed = PlayerInfoCache.getFriction(this.moveSpeed);
                }
                MovementUtils.setSpeed(e, Math.max(this.moveSpeed, baseMoveSpeed));
            }
            return;
        });
        EntityPlayerSP player;
        final EntityPlayerSP entityPlayerSP2;
        this.onUpdatePositionEvent = (e -> {
            if (e.isPre()) {
                if (MovementUtils.isMoving() && MovementUtils.isOnGround() && ++this.groundTicks > 1) {
                    this.toggle();
                }
                player = Wrapper.getPlayer();
                if (player.fallDistance < 1.0f) {
                    entityPlayerSP2.motionY += 0.005;
                }
            }
            return;
        });
        this.setSuffixListener(this.longJumpModeProperty);
    }
    
    @Override
    public void onEnable() {
        Step.cancelStep = true;
        this.groundTicks = 0;
    }
    
    @Override
    public void onDisable() {
        Step.cancelStep = false;
    }
    
    private boolean isWatchdog() {
        return this.longJumpModeProperty.getValue() == LongJumpMode.WATCHDOG;
    }
    
    private enum LongJumpMode
    {
        NCP("NCP", 0), 
        WATCHDOG("WATCHDOG", 1);
        
        private LongJumpMode(final String name, final int ordinal) {
        }
    }
}
