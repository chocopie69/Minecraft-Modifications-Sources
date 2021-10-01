// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import vip.radium.module.ModuleManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import vip.radium.utils.PlayerInfoCache;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.radium.utils.ServerUtils;
import vip.radium.property.impl.Representation;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.event.impl.player.MoveEntityEvent;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.utils.TimerUtil;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.StepEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Flight", category = ModuleCategory.MOVEMENT)
public final class Flight extends Module
{
    @EventLink
    public final Listener<StepEvent> onStepEvent;
    private final EnumProperty<FlightMode> flightModeProperty;
    private final Property<Boolean> viewBobbingProperty;
    private final Property<Boolean> damageProperty;
    private final Property<Boolean> toggleAuraProperty;
    private final EnumProperty<FrictionMode> frictionModeProperty;
    private final Property<Boolean> timerProperty;
    private final DoubleProperty timerSpeedProperty;
    private final DoubleProperty timerDurationProperty;
    private final DoubleProperty speedProperty;
    private final DoubleProperty consistencyProperty;
    private final TimerUtil timer;
    private double moveSpeed;
    private int stage;
    private boolean changeGroundState;
    private double damageBoost;
    private int ticksSinceDamage;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive;
    private int ticksUntilUpdateGroundState;
    @EventLink
    @Priority(0)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    @Priority(0)
    public final Listener<MoveEntityEvent> MoveEntityEvent;
    private boolean killauraWasEnabled;
    private KillAura aura;
    
    public Flight() {
        this.onStepEvent = (event -> event.setStepHeight(0.0f));
        this.flightModeProperty = new EnumProperty<FlightMode>("Mode", FlightMode.WATCHDOG);
        this.viewBobbingProperty = new Property<Boolean>("View Bobbing", true);
        this.damageProperty = new Property<Boolean>("Damage", true, this::isWatchdog);
        this.toggleAuraProperty = new Property<Boolean>("Toggle Aura", true, this::isWatchdog);
        this.frictionModeProperty = new EnumProperty<FrictionMode>("Friction Mode", FrictionMode.WATCHDOG, this::isWatchdog);
        this.timerProperty = new Property<Boolean>("Timer", true, this::isWatchdog);
        this.timerSpeedProperty = new DoubleProperty("Timer Speed", 1.7, () -> this.isWatchdog() && this.timerProperty.getValue(), 1.0, 3.0, 0.1);
        this.timerDurationProperty = new DoubleProperty("Timer Duration", 600.0, () -> this.isWatchdog() && this.timerProperty.getValue(), 0.0, 1000.0, 10.0, Representation.MILLISECONDS);
        this.speedProperty = new DoubleProperty("Speed", 2.5, 0.1, 5.0, 0.05);
        this.consistencyProperty = new DoubleProperty("Consistency", 0.8, this::isWatchdog, 0.0, 1.0, 0.05);
        this.timer = new TimerUtil();
        final Packet<?> packet;
        S27PacketExplosion velocityPacket;
        double motionX;
        double motionZ;
        this.onPacketReceive = (event -> {
            packet = event.getPacket();
            if (ServerUtils.isOnHypixel() && this.isWatchdog() && packet instanceof S27PacketExplosion) {
                velocityPacket = (S27PacketExplosion)packet;
                motionX = velocityPacket.func_149149_c();
                motionZ = velocityPacket.func_149147_e();
                this.damageBoost = Math.sqrt(motionX * motionX + motionZ * motionZ);
                this.ticksSinceDamage = 0;
            }
            return;
        });
        EntityPlayerSP player;
        this.onUpdatePositionEvent = (e -> {
            if (e.isPre()) {
                player = Wrapper.getPlayer();
                if (this.viewBobbingProperty.getValue()) {
                    player.cameraYaw = 0.105f;
                }
                switch (this.flightModeProperty.getValue()) {
                    case WATCHDOG: {
                        if (this.timerProperty.getValue() && this.timer.hasElapsed(this.timerDurationProperty.getValue().longValue())) {
                            Wrapper.getTimer().timerSpeed = 1.0f;
                        }
                        player.motionY = 0.0;
                        if (ServerUtils.isOnHypixel()) {
                            if (this.changeGroundState && this.ticksUntilUpdateGroundState > 0) {
                                --this.ticksUntilUpdateGroundState;
                                if (this.ticksUntilUpdateGroundState == 0) {
                                    e.setOnGround(true);
                                }
                            }
                            if (Wrapper.getPlayer().ticksExisted % 2 == 0) {
                                e.setPosY(e.getPosY() - 0.003000000026077032);
                                break;
                            }
                            else {
                                break;
                            }
                        }
                        else {
                            break;
                        }
                        break;
                    }
                    case MOTION: {
                        if (Wrapper.getGameSettings().keyBindJump.isKeyDown()) {
                            player.motionY = 1.0;
                            break;
                        }
                        else if (Wrapper.getGameSettings().keyBindSneak.isKeyDown()) {
                            player.motionY = -1.0;
                            break;
                        }
                        else {
                            Wrapper.getPlayer().motionY = 0.0;
                            break;
                        }
                        break;
                    }
                }
            }
            return;
        });
        double lastDist;
        double baseMoveSpeed;
        final EntityPlayerSP entityPlayerSP;
        final double motionY;
        double difference;
        this.MoveEntityEvent = (e -> {
            switch (this.flightModeProperty.getValue()) {
                case WATCHDOG: {
                    if (MovementUtils.isMoving()) {
                        lastDist = PlayerInfoCache.getLastDist();
                        baseMoveSpeed = PlayerInfoCache.getBaseMoveSpeed();
                        Label_0321_1: {
                            switch (this.stage) {
                                case 0: {
                                    if (MovementUtils.isOnGround()) {
                                        this.moveSpeed = baseMoveSpeed * 2.149000095319934;
                                        Wrapper.getPlayer();
                                        MovementUtils.getJumpHeight();
                                        e.setY(entityPlayerSP.motionY = motionY);
                                        break Label_0321_1;
                                    }
                                    else {
                                        this.moveSpeed = baseMoveSpeed;
                                        break Label_0321_1;
                                    }
                                    break;
                                }
                                case 1: {
                                    difference = ((MovementUtils.getJumpBoostModifier() > 0) ? 0.6600000262260437 : (0.6600000262260437 * this.consistencyProperty.getValue())) * (lastDist - baseMoveSpeed);
                                    this.moveSpeed = lastDist - difference;
                                    break Label_0321_1;
                                }
                                case 3: {
                                    if (this.timerProperty.getValue() && !this.timer.hasElapsed(this.timerDurationProperty.getValue().longValue())) {
                                        Wrapper.getTimer().timerSpeed = this.timerSpeedProperty.getValue().floatValue();
                                    }
                                    this.moveSpeed = lastDist - lastDist / 159.9999985;
                                    break Label_0321_1;
                                }
                                case 2: {
                                    if (Wrapper.getPlayer().fallDistance > MovementUtils.getMinFallDist()) {
                                        this.moveSpeed *= 6.0;
                                        break Label_0321_1;
                                    }
                                    else if (this.damageProperty.getValue() && MovementUtils.fallDistDamage()) {
                                        this.changeGroundState = true;
                                        this.ticksUntilUpdateGroundState = 2;
                                        break;
                                    }
                                    else {
                                        break;
                                    }
                                    break;
                                }
                            }
                            this.moveSpeed = lastDist - lastDist / 159.9999985;
                        }
                        if (this.damageProperty.getValue() && this.damageBoost > 0.0) {
                            if (this.ticksSinceDamage < 5) {
                                this.moveSpeed += this.damageBoost;
                            }
                            this.damageBoost = 0.0;
                        }
                        MovementUtils.setSpeed(e, Math.max(baseMoveSpeed, this.moveSpeed));
                        ++this.stage;
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
                case MOTION: {
                    if (MovementUtils.isMoving()) {
                        MovementUtils.setSpeed(e, this.speedProperty.getValue());
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
        this.setSuffixListener(this.flightModeProperty);
    }
    
    @Override
    public void onEnable() {
        Step.cancelStep = true;
        this.changeGroundState = false;
        this.damageBoost = 0.0;
        this.ticksUntilUpdateGroundState = 0;
        this.stage = 0;
        this.timer.reset();
        if (this.aura == null) {
            this.aura = ModuleManager.getInstance(KillAura.class);
        }
        if (this.toggleAuraProperty.getValue() && (this.killauraWasEnabled = this.aura.isEnabled())) {
            this.aura.toggle();
        }
    }
    
    @Override
    public void onDisable() {
        Step.cancelStep = false;
        Wrapper.getTimer().timerSpeed = 1.0f;
        if (this.toggleAuraProperty.getValue() && this.killauraWasEnabled && !this.aura.isEnabled()) {
            this.aura.toggle();
        }
    }
    
    private boolean isWatchdog() {
        return this.flightModeProperty.getValue() == FlightMode.WATCHDOG;
    }
    
    private enum FlightMode
    {
        WATCHDOG("WATCHDOG", 0), 
        MOTION("MOTION", 1);
        
        private FlightMode(final String name, final int ordinal) {
        }
    }
    
    private enum FrictionMode
    {
        NCP("NCP", 0), 
        WATCHDOG("WATCHDOG", 1);
        
        private FrictionMode(final String name, final int ordinal) {
        }
    }
}
