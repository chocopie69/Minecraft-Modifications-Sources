// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import vip.radium.utils.Wrapper;
import vip.radium.module.ModuleManager;
import vip.radium.utils.MovementUtils;
import vip.radium.event.impl.player.SprintEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.MoveEntityEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Sprint", category = ModuleCategory.MOVEMENT)
public final class Sprint extends Module
{
    private final Property<Boolean> omniProperty;
    private int groundTicks;
    @EventLink
    public final Listener<MoveEntityEvent> onMoveEntityEvent;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    public final Listener<SprintEvent> onSprintEvent;
    
    public Sprint() {
        this.omniProperty = new Property<Boolean>("Omni", true);
        this.onMoveEntityEvent = (event -> {
            if (this.groundTicks > 3 && MovementUtils.isMoving() && !Speed.isSpeeding() && !ModuleManager.getInstance(Flight.class).isEnabled() && this.omniProperty.getValue()) {
                MovementUtils.setSpeed(event, MovementUtils.getBaseMoveSpeed());
            }
            return;
        });
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                if (MovementUtils.isOnGround()) {
                    ++this.groundTicks;
                }
                else {
                    this.groundTicks = 0;
                }
            }
            return;
        });
        boolean canSprint;
        this.onSprintEvent = (event -> {
            if (!event.isSprinting()) {
                canSprint = MovementUtils.canSprint(this.omniProperty.getValue());
                Wrapper.getPlayer().setSprinting(canSprint);
                event.setSprinting(canSprint);
            }
            return;
        });
        this.toggle();
    }
}
