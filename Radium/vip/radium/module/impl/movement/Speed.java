// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.movement;

import vip.radium.module.ModuleManager;
import net.minecraft.network.Packet;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.utils.PlayerInfoCache;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.radium.utils.Wrapper;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.HypixelGameUtils;
import vip.radium.utils.ServerUtils;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Priority;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Speed", category = ModuleCategory.MOVEMENT)
public final class Speed extends Module
{
    private final EnumProperty<SpeedMode> speedModeProperty;
    private final EnumProperty<FrictionMode> frictionModeProperty;
    private final Property<Boolean> glideProperty;
    private final DoubleProperty customSpeedProperty;
    private final Property<Boolean> customJumpProperty;
    private final Property<Boolean> damageBoostProperty;
    private final Property<Boolean> timerProperty;
    private final DoubleProperty onGroundTimerProperty;
    private float fallDist;
    private double damageBoost;
    private int ticksSinceDamage;
    private int damageBoostCooldown;
    @EventLink
    @Priority(4)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive;
    private double moveSpeed;
    private boolean wasOnGround;
    @EventLink
    @Priority(4)
    public final Listener<MoveEntityEvent> onMoveEntityEvent;
    
    public Speed() {
        this.speedModeProperty = new EnumProperty<SpeedMode>("Mode", SpeedMode.WATCHDOGE);
        this.frictionModeProperty = new EnumProperty<FrictionMode>("Friction Mode", FrictionMode.NCP, this::isWatchdog);
        this.glideProperty = new Property<Boolean>("Glide", false);
        this.customSpeedProperty = new DoubleProperty("Custom Speed", 0.5, this::isCustomSpeed, 0.0, 10.0, 0.1);
        this.customJumpProperty = new Property<Boolean>("Jump On Ground", false, this::isCustomSpeed);
        this.damageBoostProperty = new Property<Boolean>("Damage Boost", false);
        this.timerProperty = new Property<Boolean>("Timer", true, this::isWatchdog);
        this.onGroundTimerProperty = new DoubleProperty("OnGround Timer", 1.3, () -> this.timerProperty.isAvailable() && this.timerProperty.getValue(), 1.0, 1.4, 0.01);
        double posY;
        double yDif;
        final EntityPlayerSP entityPlayerSP;
        this.onUpdatePositionEvent = (e -> {
            if (e.isPre()) {
                if (this.damageBoost > 0.0) {
                    ++this.ticksSinceDamage;
                }
                else {
                    ++this.damageBoostCooldown;
                }
                if (e.isOnGround() && ServerUtils.isOnHypixel()) {
                    posY = e.getPosY();
                    if (HypixelGameUtils.getGameMode() == HypixelGameUtils.GameMode.PIT) {
                        yDif = posY - e.getPrevPosY();
                        if (yDif < 0.0) {
                            this.fallDist -= (float)yDif;
                        }
                        e.setOnGround(false);
                        if (this.fallDist > MovementUtils.getMinFallDist()) {
                            e.setPosY(posY + 0.015625);
                            e.setOnGround(true);
                            this.fallDist = 0.0f;
                        }
                    }
                    else {
                        e.setPosY(posY + 0.015625);
                    }
                }
                if (this.glideProperty.getValue() && Wrapper.getPlayer().fallDistance < 1.0f) {
                    Wrapper.getPlayer();
                    entityPlayerSP.motionY += 0.005;
                }
            }
            return;
        });
        final Packet<?> packet;
        S27PacketExplosion velocityPacket;
        double motionX;
        double motionZ;
        this.onPacketReceive = (event -> {
            packet = event.getPacket();
            if (packet instanceof S27PacketExplosion) {
                velocityPacket = (S27PacketExplosion)packet;
                motionX = velocityPacket.func_149149_c();
                motionZ = velocityPacket.func_149147_e();
                this.damageBoost = Math.sqrt(motionX * motionX + motionZ * motionZ);
                this.ticksSinceDamage = 0;
            }
            return;
        });
        double lastDist;
        double baseMoveSpeed;
        double motionY;
        final EntityPlayerSP entityPlayerSP2;
        double difference;
        final EntityPlayerSP entityPlayerSP3;
        final double motionY2;
        this.onMoveEntityEvent = (e -> {
            switch (this.speedModeProperty.getValue()) {
                case WATCHDOGE: {
                    if (MovementUtils.isMoving()) {
                        lastDist = PlayerInfoCache.getLastDist();
                        baseMoveSpeed = PlayerInfoCache.getBaseMoveSpeed();
                        if (MovementUtils.isOnGround() && !this.wasOnGround) {
                            this.wasOnGround = true;
                            Wrapper.getPlayer();
                            motionY = MovementUtils.getJumpHeight() - 0.005;
                            e.setY(entityPlayerSP2.motionY = motionY);
                            if (MovementUtils.isOnIce()) {
                                baseMoveSpeed *= 2.5;
                            }
                            if (HypixelGameUtils.getGameMode() == HypixelGameUtils.GameMode.PIT) {
                                this.moveSpeed = baseMoveSpeed * 1.78;
                            }
                            else {
                                this.moveSpeed = Math.min(2.14 * baseMoveSpeed, Math.max(baseMoveSpeed * 1.78, lastDist * 1.78));
                            }
                            if (this.timerProperty.getValue()) {
                                Wrapper.getTimer().timerSpeed = this.onGroundTimerProperty.getValue().floatValue();
                            }
                        }
                        else if (this.wasOnGround) {
                            difference = (0.74 + 0.02 * MovementUtils.getJumpBoostModifier()) * (lastDist - baseMoveSpeed);
                            this.moveSpeed = lastDist - difference;
                            this.wasOnGround = false;
                        }
                        else {
                            switch (this.frictionModeProperty.getValue()) {
                                case NCP: {
                                    this.moveSpeed = PlayerInfoCache.getFriction(this.moveSpeed);
                                    break;
                                }
                                case FAST: {
                                    this.moveSpeed = lastDist - lastDist / 159.9999985;
                                    break;
                                }
                            }
                            if (this.timerProperty.getValue()) {
                                Wrapper.getTimer().timerSpeed = 1.085f;
                            }
                        }
                        if (this.damageBoostProperty.getValue() && this.damageBoost > 0.0 && this.damageBoostCooldown >= 10) {
                            if (this.ticksSinceDamage < 5) {
                                this.moveSpeed += this.damageBoost;
                                this.damageBoostCooldown = 0;
                            }
                            this.damageBoost = 0.0;
                        }
                        MovementUtils.setSpeed(e, Math.max(baseMoveSpeed, this.moveSpeed));
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
                case CUSTOM: {
                    if (MovementUtils.isMoving()) {
                        if (this.customJumpProperty.getValue() && MovementUtils.isOnGround()) {
                            Wrapper.getPlayer();
                            e.setY(entityPlayerSP3.motionY = motionY2);
                        }
                        MovementUtils.setSpeed(e, this.customSpeedProperty.getValue());
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
        this.setSuffixListener(this.speedModeProperty);
    }
    
    public static boolean isSpeeding() {
        return ModuleManager.getInstance(Speed.class).isEnabled();
    }
    
    @Override
    public void onEnable() {
        this.moveSpeed = 0.0;
        this.damageBoost = 0.0;
        this.fallDist = Wrapper.getPlayer().fallDistance;
    }
    
    @Override
    public void onDisable() {
        Wrapper.getTimer().timerSpeed = 1.0f;
    }
    
    private boolean isWatchdog() {
        return this.speedModeProperty.getValue() == SpeedMode.WATCHDOGE;
    }
    
    private boolean isCustomSpeed() {
        return this.speedModeProperty.getValue() == SpeedMode.CUSTOM;
    }
    
    private enum FrictionMode
    {
        NCP("NCP", 0), 
        FAST("FAST", 1);
        
        private FrictionMode(final String name, final int ordinal) {
        }
    }
    
    private enum SpeedMode
    {
        WATCHDOGE("WATCHDOGE", 0), 
        CUSTOM("CUSTOM", 1);
        
        private SpeedMode(final String name, final int ordinal) {
        }
    }
}
