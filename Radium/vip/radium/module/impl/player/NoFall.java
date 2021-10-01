// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.utils.Wrapper;
import vip.radium.utils.MovementUtils;
import java.util.Arrays;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.EnumProperty;
import java.util.List;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "No Fall", category = ModuleCategory.PLAYER)
public final class NoFall extends Module
{
    private static final List<Double> BLOCK_HEIGHTS;
    private final EnumProperty<NoFallMode> noFallModeProperty;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    static {
        BLOCK_HEIGHTS = Arrays.asList(0.015625, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875, 1.0);
    }
    
    public NoFall() {
        this.noFallModeProperty = new EnumProperty<NoFallMode>("Mode", NoFallMode.EDIT);
        double minFallDist;
        NoFallMode mode;
        double currentYOffset;
        final double n;
        double yPos;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                minFallDist = MovementUtils.getMinFallDist();
                if (Wrapper.getPlayer().fallDistance > minFallDist) {
                    mode = this.noFallModeProperty.getValue();
                    if (mode == NoFallMode.PACKET) {
                        Wrapper.sendPacketDirect(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), true));
                    }
                    else if ((int)Wrapper.getPlayer().fallDistance % minFallDist == 0.0) {
                        switch (mode) {
                            case ROUNDED: {
                                currentYOffset = MovementUtils.getBlockHeight();
                                NoFall.BLOCK_HEIGHTS.sort((h, h1) -> (int)((Math.abs(n - h) - Math.abs(n - h1)) * 10.0));
                                yPos = (int)event.getPosY() + NoFall.BLOCK_HEIGHTS.get(0);
                                event.setPosY(yPos);
                            }
                            case EDIT: {
                                event.setOnGround(true);
                                break;
                            }
                        }
                    }
                }
            }
            return;
        });
        this.setSuffixListener(this.noFallModeProperty);
    }
    
    private enum NoFallMode
    {
        EDIT("EDIT", 0), 
        PACKET("PACKET", 1), 
        ROUNDED("ROUNDED", 2);
        
        private NoFallMode(final String name, final int ordinal) {
        }
    }
}
