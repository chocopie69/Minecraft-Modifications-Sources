// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;
import vip.radium.module.ModuleManager;
import vip.radium.property.impl.Representation;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.TimerUtil;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Anti Fall", category = ModuleCategory.MOVEMENT)
public final class AntiFall extends Module
{
    private final TimerUtil timer;
    private final EnumProperty<NoVoidMode> noVoidModeProperty;
    private final DoubleProperty distProperty;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public AntiFall() {
        this.timer = new TimerUtil();
        this.noVoidModeProperty = new EnumProperty<NoVoidMode>("Mode", NoVoidMode.PACKET);
        this.distProperty = new DoubleProperty("Distance", 5.0, 3.0, 10.0, 0.5, Representation.DISTANCE);
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre() && !ModuleManager.getInstance(Flight.class).isEnabled() && Wrapper.getPlayer().fallDistance > this.distProperty.getValue().floatValue() && !MovementUtils.isOnGround() && this.timer.hasElapsed(500L) && MovementUtils.isOverVoid()) {
                switch (this.noVoidModeProperty.getValue()) {
                    case PACKET: {
                        Wrapper.sendPacketDirect(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY() + 9.0 + StrictMath.random(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
                        break;
                    }
                    case MOTION: {
                        if (Wrapper.getPlayer().motionY < 0.0) {
                            Wrapper.getPlayer().motionY = 2.200000047683716;
                            break;
                        }
                        else {
                            break;
                        }
                        break;
                    }
                }
                Wrapper.getPlayer().fallDistance = 0.0f;
                this.timer.reset();
            }
            return;
        });
        this.setSuffixListener(this.noVoidModeProperty);
    }
    
    private enum NoVoidMode
    {
        PACKET("PACKET", 0), 
        MOTION("MOTION", 1);
        
        private NoVoidMode(final String name, final int ordinal) {
        }
    }
}
