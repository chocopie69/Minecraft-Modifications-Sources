// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.utils.Wrapper;
import vip.radium.utils.MovementUtils;
import vip.radium.event.impl.packet.PacketSendEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.StepEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Step", category = ModuleCategory.MOVEMENT)
public final class Step extends Module
{
    public static boolean cancelStep;
    private final Property<Boolean> cancelExtraPackets;
    private final double[] offsets;
    private float timerWhenStepping;
    private boolean cancelMorePackets;
    private byte cancelledPackets;
    @EventLink
    public final Listener<StepEvent> onStepEvent;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    
    public Step() {
        this.cancelExtraPackets = new Property<Boolean>("Less Packets", true);
        this.offsets = new double[] { 0.41999998688697815, 0.7531999945640564 };
        double steppedHeight;
        double[] offsets;
        final Object o;
        int length;
        int i = 0;
        double offset;
        this.onStepEvent = (e -> {
            if (!MovementUtils.isInLiquid() && MovementUtils.isOnGround()) {
                if (e.isPre()) {
                    e.setStepHeight(Step.cancelStep ? 0.0f : 1.0f);
                }
                else {
                    steppedHeight = e.getHeightStepped();
                    offsets = this.offsets;
                    for (length = o.length; i < length; ++i) {
                        offset = offsets[i];
                        Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + offset * steppedHeight, Wrapper.getPlayer().posZ, false));
                    }
                    this.timerWhenStepping = 1.0f / (this.offsets.length + 1);
                    this.cancelMorePackets = true;
                }
            }
            return;
        });
        this.onPacketSendEvent = (e -> {
            if (this.cancelExtraPackets.getValue() && e.getPacket() instanceof C03PacketPlayer) {
                if (this.cancelledPackets > 0) {
                    this.cancelMorePackets = false;
                    this.cancelledPackets = 0;
                    Wrapper.getTimer().timerSpeed = 1.0f;
                }
                if (this.cancelMorePackets) {
                    Wrapper.getTimer().timerSpeed = this.timerWhenStepping;
                    ++this.cancelledPackets;
                }
            }
        });
    }
    
    @Override
    public void onDisable() {
        Wrapper.getTimer().timerSpeed = 1.0f;
    }
}
