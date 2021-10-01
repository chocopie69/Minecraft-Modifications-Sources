// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.utils.Wrapper;
import vip.radium.utils.MovementUtils;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Regen", category = ModuleCategory.PLAYER)
public final class Regen extends Module
{
    private final DoubleProperty packetsProperty;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public Regen() {
        this.packetsProperty = new DoubleProperty("Packets", 10.0, 0.0, 100.0, 1.0);
        int i;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre() && MovementUtils.isOnGround() && Wrapper.getPlayer().getHealth() < Wrapper.getPlayer().getMaxHealth()) {
                for (i = 0; i < this.packetsProperty.getValue().intValue(); ++i) {
                    Wrapper.sendPacketDirect(new C03PacketPlayer(true));
                }
            }
        });
    }
}
