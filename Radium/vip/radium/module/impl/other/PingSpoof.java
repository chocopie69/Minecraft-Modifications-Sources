// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.ServerUtils;
import java.util.ArrayList;
import vip.radium.event.impl.packet.PacketSendEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import net.minecraft.network.Packet;
import java.util.List;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Ping Spoof", category = ModuleCategory.OTHER)
public final class PingSpoof extends Module
{
    private final EnumProperty<PingSpoofMode> modeProperty;
    private final List<Packet<?>> packets;
    private final TimerUtil timer;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    
    public PingSpoof() {
        this.modeProperty = new EnumProperty<PingSpoofMode>("Mode", PingSpoofMode.WATCHDOG);
        this.packets = new ArrayList<Packet<?>>();
        this.timer = new TimerUtil();
        this.onUpdatePositionEvent = (event -> {
            if (this.modeProperty.getValue() == PingSpoofMode.WATCHDOG && ServerUtils.isOnHypixel()) {
                if (Wrapper.getPlayer().ticksExisted < 5) {
                    this.packets.clear();
                }
                else if (this.timer.hasElapsed(RandomUtils.nextInt(5000, 9000))) {
                    while (!this.packets.isEmpty()) {
                        Wrapper.sendPacketDirect(this.packets.remove(0));
                    }
                    this.timer.reset();
                }
            }
            return;
        });
        Packet<?> packet;
        C0FPacketConfirmTransaction transaction;
        this.onPacketSendEvent = (packetSendEvent -> {
            if (this.modeProperty.getValue() == PingSpoofMode.WATCHDOG) {
                packet = packetSendEvent.getPacket();
                if (packet instanceof C0FPacketConfirmTransaction) {
                    transaction = (C0FPacketConfirmTransaction)packet;
                    if (transaction.getUid() < 0) {
                        this.packets.add(packet);
                        packetSendEvent.setCancelled();
                    }
                }
                else if (packet instanceof C00PacketKeepAlive) {
                    this.packets.add(packet);
                    packetSendEvent.setCancelled();
                }
            }
            return;
        });
        this.setSuffixListener(this.modeProperty);
    }
    
    private enum PingSpoofMode
    {
        WATCHDOG("WATCHDOG", 0);
        
        private PingSpoofMode(final String name, final int ordinal) {
        }
    }
}
