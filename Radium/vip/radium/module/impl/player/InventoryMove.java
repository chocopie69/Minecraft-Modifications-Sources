// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketSendEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Inventory Move", category = ModuleCategory.PLAYER)
public final class InventoryMove extends Module
{
    private final Property<Boolean> cancelPacketProperty;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    
    public InventoryMove() {
        this.cancelPacketProperty = new Property<Boolean>("Cancel", false);
        this.onPacketSendEvent = (event -> {
            if (this.cancelPacketProperty.getValue() && (event.getPacket() instanceof C16PacketClientStatus || event.getPacket() instanceof C0DPacketCloseWindow)) {
                event.setCancelled();
            }
        });
    }
}
