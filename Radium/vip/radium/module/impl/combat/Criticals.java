// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.combat;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.utils.Wrapper;
import vip.radium.utils.ServerUtils;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.radium.utils.MovementUtils;
import vip.radium.property.impl.Representation;
import vip.radium.event.impl.packet.PacketSendEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.TimerUtil;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Criticals", category = ModuleCategory.COMBAT)
public final class Criticals extends Module
{
    private final TimerUtil timer;
    private final EnumProperty<CriticalsMode> criticalsModeProperty;
    private final DoubleProperty delayProperty;
    private int groundTicks;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    
    public Criticals() {
        this.timer = new TimerUtil();
        this.criticalsModeProperty = new EnumProperty<CriticalsMode>("Mode", CriticalsMode.WATCHDOG);
        this.delayProperty = new DoubleProperty("Delay", 490.0, 0.0, 1000.0, 10.0, Representation.MILLISECONDS);
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                this.groundTicks = (MovementUtils.isOnGround() ? (this.groundTicks + 1) : 0);
            }
            return;
        });
        final Object o;
        int length;
        int i = 0;
        final double[] array;
        double offset;
        this.onPacketSendEvent = (event -> {
            switch (this.criticalsModeProperty.getValue()) {
                case WATCHDOG:
                case NCP: {
                    if (event.getPacket() instanceof C0APacketAnimation && this.hasTarget() && ServerUtils.isOnHypixel() && this.timer.hasElapsed(this.delayProperty.getValue().longValue()) && this.groundTicks > 1) {
                        this.criticalsModeProperty.getValue().offsets;
                        for (length = o.length; i < length; ++i) {
                            offset = array[i];
                            Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + offset + StrictMath.random() * 3.000000142492354E-4, Wrapper.getPlayer().posZ, false));
                        }
                        this.timer.reset();
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
            }
            return;
        });
        this.setSuffixListener(this.criticalsModeProperty);
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
        this.groundTicks = 0;
    }
    
    private boolean hasTarget() {
        if (KillAura.getInstance().getTarget() != null) {
            return true;
        }
        final MovingObjectPosition target = Wrapper.getMinecraft().objectMouseOver;
        return target != null && target.entityHit != null;
    }
    
    private enum CriticalsMode
    {
        WATCHDOG("WATCHDOG", 0, new double[] { 0.0560000017285347, 0.01600000075995922, 0.003000000026077032 }), 
        NCP("NCP", 1, new double[] { 0.06251999735832214, 0.0 });
        
        private final double[] offsets;
        
        private CriticalsMode(final String name, final int ordinal, final double[] offsets) {
            this.offsets = offsets;
        }
    }
}
