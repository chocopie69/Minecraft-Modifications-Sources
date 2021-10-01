// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import vip.radium.utils.Wrapper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "No Rotate", category = ModuleCategory.PLAYER)
public final class NoRotate extends Module
{
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    
    public NoRotate() {
        S08PacketPlayerPosLook packet;
        this.onPacketReceiveEvent = (e -> {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                packet = (S08PacketPlayerPosLook)e.getPacket();
                packet.yaw = Wrapper.getPlayer().rotationYaw;
                packet.pitch = Wrapper.getPlayer().rotationPitch;
            }
        });
    }
}
