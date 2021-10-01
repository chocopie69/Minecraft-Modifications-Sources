// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import net.minecraft.network.Packet;
import vip.radium.utils.ServerUtils;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.radium.utils.Wrapper;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import vip.radium.property.impl.Representation;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Velocity", category = ModuleCategory.PLAYER)
public final class Velocity extends Module
{
    private final DoubleProperty horizontalPercentProperty;
    private final DoubleProperty verticalPercentProperty;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    
    public Velocity() {
        this.horizontalPercentProperty = new DoubleProperty("Horizontal", 0.0, 0.0, 100.0, 0.5, Representation.PERCENTAGE);
        this.verticalPercentProperty = new DoubleProperty("Vertical", 0.0, 0.0, 100.0, 0.5, Representation.PERCENTAGE);
        final Packet<?> packet;
        S12PacketEntityVelocity velocityPacket;
        double verticalPerc;
        double horizontalPerc;
        final S12PacketEntityVelocity s12PacketEntityVelocity;
        final S12PacketEntityVelocity s12PacketEntityVelocity2;
        final S12PacketEntityVelocity s12PacketEntityVelocity3;
        double verticalPerc2;
        double horizontalPerc2;
        S27PacketExplosion s27PacketExplosion;
        S27PacketExplosion packetExplosion;
        final S27PacketExplosion s27PacketExplosion2;
        final S27PacketExplosion s27PacketExplosion3;
        this.onPacketReceiveEvent = (e -> {
            packet = e.getPacket();
            if (packet instanceof S12PacketEntityVelocity) {
                velocityPacket = (S12PacketEntityVelocity)packet;
                if (velocityPacket.getEntityID() == Wrapper.getPlayer().getEntityId()) {
                    verticalPerc = this.verticalPercentProperty.getValue();
                    horizontalPerc = this.horizontalPercentProperty.getValue();
                    if (verticalPerc == 0.0 && horizontalPerc == 0.0) {
                        e.setCancelled();
                    }
                    else {
                        s12PacketEntityVelocity.motionX *= (int)(this.horizontalPercentProperty.getValue() / 100.0);
                        s12PacketEntityVelocity2.motionY *= (int)(this.verticalPercentProperty.getValue() / 100.0);
                        s12PacketEntityVelocity3.motionZ *= (int)(this.horizontalPercentProperty.getValue() / 100.0);
                    }
                }
            }
            else if (packet instanceof S27PacketExplosion && ServerUtils.isOnHypixel()) {
                verticalPerc2 = this.verticalPercentProperty.getValue();
                horizontalPerc2 = this.horizontalPercentProperty.getValue();
                if (verticalPerc2 == 0.0 && horizontalPerc2 == 0.0) {
                    e.setCancelled();
                }
                else {
                    packetExplosion = (s27PacketExplosion = (S27PacketExplosion)packet);
                    s27PacketExplosion.motionX *= (float)(this.horizontalPercentProperty.getValue() / 100.0);
                    s27PacketExplosion2.motionY *= (float)(this.verticalPercentProperty.getValue() / 100.0);
                    s27PacketExplosion3.motionZ *= (float)(this.horizontalPercentProperty.getValue() / 100.0);
                }
            }
        });
    }
}
