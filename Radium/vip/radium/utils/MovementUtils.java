// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.module.impl.combat.TargetStrafe;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.module.impl.movement.NoSlowdown;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockIce;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockAir;
import vip.radium.event.impl.player.UpdatePositionEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.ArrayList;
import java.util.List;

public final class MovementUtils
{
    private static final List<Double> frictionValues;
    private static final double MIN_DIF = 0.0010000000474974513;
    private static final double AIR_FRICTION = 0.9800000190734863;
    private static final double WATER_FRICTION = 0.8899999856948853;
    private static final double LAVA_FRICTION = 0.5350000262260437;
    public static final double BUNNY_SLOPE = 0.6600000262260437;
    public static final double SPRINTING_MOD = 1.2999999523162842;
    public static final double SNEAK_MOD = 0.30000001192092896;
    public static final double ICE_MOD = 2.5;
    public static final double VANILLA_JUMP_HEIGHT = 0.41999998688697815;
    public static final double WALK_SPEED = 0.22100000083446503;
    private static final double SWIM_MOD = 0.5203619984250619;
    private static final double[] DEPTH_STRIDER_VALUES;
    public static final double MAX_DIST = 2.149000095319934;
    public static final double BUNNY_DIV_FRICTION = 159.9999985;
    public static final double GRAVITY_MAX = 0.0834;
    public static final double GRAVITY_MIN = 0.0624;
    public static final double GRAVITY_SPAN = 0.021000000000000005;
    public static final double GRAVITY_ODD = 0.05;
    public static final float GRAVITY_VACC = 0.03744f;
    
    static {
        frictionValues = new ArrayList<Double>();
        DEPTH_STRIDER_VALUES = new double[] { 1.0, 1.4304347400741908, 1.7347825295420374, 1.9217391028296074 };
    }
    
    private MovementUtils() {
    }
    
    public static int getJumpBoostModifier() {
        final PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(Potion.jump.id);
        if (effect != null) {
            return effect.getAmplifier() + 1;
        }
        return 0;
    }
    
    public static int getSpeedModifier() {
        final PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(Potion.moveSpeed.id);
        if (effect != null) {
            return effect.getAmplifier() + 1;
        }
        return 0;
    }
    
    private static boolean isMovingEnoughForSprint() {
        final MovementInput movementInput = Wrapper.getPlayer().movementInput;
        return movementInput.moveForward > 0.8f || movementInput.moveForward < -0.8f || movementInput.moveStrafe > 0.8f || movementInput.moveStrafe < -0.8f;
    }
    
    public static float getMovementDirection() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final float forward = player.moveForward;
        final float strafe = player.moveStrafing;
        float direction = 0.0f;
        if (forward < 0.0f) {
            direction += 180.0f;
            if (strafe > 0.0f) {
                direction += 45.0f;
            }
            else if (strafe < 0.0f) {
                direction -= 45.0f;
            }
        }
        else if (forward > 0.0f) {
            if (strafe > 0.0f) {
                direction -= 45.0f;
            }
            else if (strafe < 0.0f) {
                direction += 45.0f;
            }
        }
        else if (strafe > 0.0f) {
            direction -= 90.0f;
        }
        else if (strafe < 0.0f) {
            direction += 90.0f;
        }
        direction += player.rotationYaw;
        return MathHelper.wrapAngleTo180_float(direction);
    }
    
    public static boolean isBlockAbove() {
        return Wrapper.getWorld().checkBlockCollision(Wrapper.getPlayer().getEntityBoundingBox().addCoord(0.0, 1.0, 0.0));
    }
    
    public static boolean fallDistDamage() {
        if (isBlockAbove() || !ServerUtils.isOnHypixel() || !HypixelGameUtils.hasGameStarted()) {
            return false;
        }
        final UpdatePositionEvent e = Wrapper.getPlayer().currentEvent;
        final double x = e.getPosX();
        final double y = e.getPosY();
        final double z = e.getPosZ();
        final double smallOffset = 0.0013000000035390258;
        final double offset = 0.06109999865293503;
        final double packets = Math.ceil(getMinFallDist() / 0.059799998649396);
        for (int i = 0; i < packets; ++i) {
            Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.06109999865293503, z, false));
            Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0013000000035390258, z, false));
        }
        return true;
    }
    
    public static boolean isInLiquid() {
        return Wrapper.getPlayer().isInWater() || Wrapper.getPlayer().isInLava();
    }
    
    public static boolean isOverVoid() {
        for (double posY = Wrapper.getPlayer().posY; posY > 0.0; --posY) {
            if (!(Wrapper.getWorld().getBlockState(new BlockPos(Wrapper.getPlayer().posX, posY, Wrapper.getPlayer().posZ)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }
    
    public static double getJumpHeight() {
        final double baseJumpHeight = 0.41999998688697815;
        if (isInLiquid()) {
            return 0.13500000163912773;
        }
        if (Wrapper.getPlayer().isPotionActive(Potion.jump)) {
            return baseJumpHeight + (Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f) * 0.1f;
        }
        return baseJumpHeight;
    }
    
    public static double getMinFallDist() {
        final boolean isSg = HypixelGameUtils.getGameMode() == HypixelGameUtils.GameMode.BLITZ_SG;
        double baseFallDist = isSg ? 4.0 : 3.0;
        if (Wrapper.getPlayer().isPotionActive(Potion.jump)) {
            baseFallDist += Wrapper.getPlayer().getActivePotionEffect(Potion.jump).getAmplifier() + 1.0f;
        }
        return baseFallDist;
    }
    
    public static double calculateFriction(final double moveSpeed, final double lastDist, final double baseMoveSpeedRef) {
        MovementUtils.frictionValues.clear();
        MovementUtils.frictionValues.add(lastDist - lastDist / 159.9999985);
        MovementUtils.frictionValues.add(lastDist - (moveSpeed - lastDist) / 33.3);
        final double materialFriction = Wrapper.getPlayer().isInWater() ? 0.8899999856948853 : (Wrapper.getPlayer().isInLava() ? 0.5350000262260437 : 0.9800000190734863);
        MovementUtils.frictionValues.add(lastDist - baseMoveSpeedRef * (1.0 - materialFriction));
        return Collections.min((Collection<? extends Double>)MovementUtils.frictionValues);
    }
    
    public static boolean isOnIce() {
        final Block blockUnder = getBlockUnder();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }
    
    public static Block getBlockUnder() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return Wrapper.getWorld().getBlockState(new BlockPos(player.posX, StrictMath.floor(player.getEntityBoundingBox().minY) - 1.0, player.posZ)).getBlock();
    }
    
    public static double getBlockHeight() {
        return Wrapper.getPlayer().posY - (int)Wrapper.getPlayer().posY;
    }
    
    public static boolean canSprint(final boolean omni) {
        if (omni) {
            if (!isMovingEnoughForSprint()) {
                return false;
            }
        }
        else if (Wrapper.getPlayer().movementInput.moveForward < 0.8f) {
            return false;
        }
        if (!Wrapper.getPlayer().isCollidedHorizontally && (Wrapper.getPlayer().getFoodStats().getFoodLevel() > 6 || Wrapper.getPlayer().capabilities.allowFlying) && !Wrapper.getPlayer().isSneaking() && (!Wrapper.getPlayer().isUsingItem() || NoSlowdown.isNoSlowdownEnabled()) && !Wrapper.getPlayer().isPotionActive(Potion.moveSlowdown.id)) {
            return true;
        }
        return false;
    }
    
    public static double getBaseMoveSpeed() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        double base = player.isSneaking() ? 0.06630000288486482 : (canSprint(true) ? 0.2872999905467033 : 0.22100000083446503);
        final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed.id);
        final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown.id);
        if (moveSpeed != null) {
            base *= 1.0 + 0.2 * (moveSpeed.getAmplifier() + 1);
        }
        if (moveSlowness != null) {
            base *= 1.0 + 0.2 * (moveSlowness.getAmplifier() + 1);
        }
        if (player.isInWater()) {
            base *= 0.5203619984250619;
            final int depthStriderLevel = InventoryUtils.getDepthStriderLevel();
            if (depthStriderLevel > 0) {
                base *= MovementUtils.DEPTH_STRIDER_VALUES[depthStriderLevel];
            }
        }
        else if (player.isInLava()) {
            base *= 0.5203619984250619;
        }
        return base;
    }
    
    public static void setSpeed(final MoveEntityEvent e, double speed) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final TargetStrafe targetStrafe = TargetStrafe.getInstance();
        final KillAura killAura = KillAura.getInstance();
        if (targetStrafe.isEnabled() && (!targetStrafe.holdSpaceProperty.getValue() || Keyboard.isKeyDown(57))) {
            final EntityLivingBase target = killAura.getTarget();
            if (target != null && targetStrafe.shouldStrafe()) {
                if (targetStrafe.shouldAdaptSpeed()) {
                    speed = Math.min(speed, targetStrafe.getAdaptedSpeed());
                }
                targetStrafe.setSpeed(e, speed);
                return;
            }
        }
        setSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
    }
    
    public static void setSpeed(final MoveEntityEvent e, final double speed, final float forward, final float strafing, float yaw) {
        if (forward == 0.0f && strafing == 0.0f) {
            return;
        }
        final boolean reversed = forward < 0.0f;
        final float strafingYaw = 90.0f * ((forward > 0.0f) ? 0.5f : (reversed ? -0.5f : 1.0f));
        if (reversed) {
            yaw += 180.0f;
        }
        if (strafing > 0.0f) {
            yaw -= strafingYaw;
        }
        else if (strafing < 0.0f) {
            yaw += strafingYaw;
        }
        final double x = StrictMath.cos(StrictMath.toRadians(yaw + 90.0f));
        final double z = StrictMath.cos(StrictMath.toRadians(yaw));
        e.setX(x * speed);
        e.setZ(z * speed);
    }
    
    public static boolean isOnGround() {
        return Wrapper.getPlayer().onGround && Wrapper.getPlayer().isCollidedVertically;
    }
    
    public static boolean isMoving() {
        return Wrapper.getPlayer().movementInput.moveForward != 0.0f || Wrapper.getPlayer().movementInput.moveStrafe != 0.0f;
    }
}
