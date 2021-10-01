// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.combat;

import vip.radium.utils.RotationUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import vip.radium.RadiumClient;
import vip.radium.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.item.ItemSword;
import org.apache.commons.lang3.RandomUtils;
import vip.radium.utils.InventoryUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.client.gui.GuiScreen;
import vip.radium.gui.csgo.SkeetUI;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.module.ModuleManager;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import java.util.Comparator;
import vip.radium.utils.Wrapper;
import net.minecraft.network.play.client.C0APacketAnimation;
import java.util.HashMap;
import vip.radium.property.impl.Representation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.impl.world.Scaffold;
import net.minecraft.entity.EntityLivingBase;
import vip.radium.event.impl.entity.EntitySwingEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketSendEvent;
import io.github.nevalackin.homoBus.Listener;
import java.util.Map;
import vip.radium.utils.TimerUtil;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Kill Aura", category = ModuleCategory.COMBAT)
public final class KillAura extends Module
{
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    public static int waitTicks;
    private final EnumProperty<AuraMode> auraModeProperty;
    private final EnumProperty<SortingMethod> sortingMethodProperty;
    private final EnumProperty<AttackMethod> attackMethodProperty;
    private final DoubleProperty minApsProperty;
    private final DoubleProperty maxApsProperty;
    private final DoubleProperty rangeProperty;
    private final Property<Boolean> autoblockProperty;
    private final Property<Boolean> ncpRotationsProperty;
    private final DoubleProperty blockRangeProperty;
    private final DoubleProperty maxAngleChangeProperty;
    private final DoubleProperty fovProperty;
    private final Property<Boolean> lockViewProperty;
    private final Property<Boolean> keepSprintProperty;
    private final Property<Boolean> rayTraceProperty;
    private final Property<Boolean> forceUpdateProperty;
    private final MultiSelectEnumProperty<Targets> targetsProperty;
    private final TimerUtil attackTimer;
    private final Map<Integer, Long> playerSwingDelayMap;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    @EventLink
    private final Listener<EntitySwingEvent> onEntitySwing;
    private EntityLivingBase target;
    private boolean blocking;
    private boolean entityInBlockRange;
    private Scaffold scaffold;
    @EventLink
    @Priority(4)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    static {
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
    
    public KillAura() {
        this.auraModeProperty = new EnumProperty<AuraMode>("Mode", AuraMode.PRIORITY);
        this.sortingMethodProperty = new EnumProperty<SortingMethod>("Sorting Method", SortingMethod.HEALTH);
        this.attackMethodProperty = new EnumProperty<AttackMethod>("Attack Method", AttackMethod.POST);
        this.minApsProperty = new DoubleProperty("Min APS", 8.0, () -> this.auraModeProperty.getValue() != AuraMode.TICK, 1.0, 20.0, 1.0);
        this.maxApsProperty = new DoubleProperty("Max APS", 10.0, () -> this.auraModeProperty.getValue() != AuraMode.TICK, 1.0, 20.0, 1.0);
        this.rangeProperty = new DoubleProperty("Range", 4.3, 3.0, 6.0, 0.1, Representation.DISTANCE);
        this.autoblockProperty = new Property<Boolean>("Autoblock", true);
        this.ncpRotationsProperty = new Property<Boolean>("NCP Rots", true);
        this.blockRangeProperty = new DoubleProperty("Block Range", 8.0, this.autoblockProperty::getValue, 3.0, 8.0, 0.1, Representation.DISTANCE);
        this.maxAngleChangeProperty = new DoubleProperty("Angle Step", 45.0, () -> !this.ncpRotationsProperty.getValue(), 1.0, 180.0, 1.0);
        this.fovProperty = new DoubleProperty("FOV", 180.0, () -> !this.ncpRotationsProperty.getValue(), 10.0, 180.0, 1.0);
        this.lockViewProperty = new Property<Boolean>("Lock View", false);
        this.keepSprintProperty = new Property<Boolean>("Keep Sprint", true);
        this.rayTraceProperty = new Property<Boolean>("Ray Trace", false);
        this.forceUpdateProperty = new Property<Boolean>("Force Update", false);
        this.targetsProperty = new MultiSelectEnumProperty<Targets>("Targets", new Targets[] { Targets.PLAYERS });
        this.attackTimer = new TimerUtil();
        this.playerSwingDelayMap = new HashMap<Integer, Long>();
        this.onPacketSendEvent = (event -> {
            if (event.getPacket() instanceof C0APacketAnimation) {
                this.attackTimer.reset();
            }
            return;
        });
        this.onEntitySwing = (entitySwingEvent -> this.playerSwingDelayMap.put(entitySwingEvent.getEntityId(), System.currentTimeMillis()));
        EntityLivingBase optimalTarget;
        List<EntityLivingBase> entities;
        final Iterator<EntityLivingBase> iterator;
        EntityLivingBase entity;
        float dist;
        float[] rotations;
        float yaw;
        float pitch;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                this.entityInBlockRange = false;
                optimalTarget = null;
                entities = Wrapper.getLivingEntities(this::isValid);
                if (entities.size() > 1) {
                    entities.sort(this.sortingMethodProperty.getValue().getSorter());
                }
                entities.iterator();
                while (iterator.hasNext()) {
                    entity = iterator.next();
                    dist = Wrapper.getPlayer().getDistanceToEntity(entity);
                    if (!this.entityInBlockRange && dist < this.blockRangeProperty.getValue()) {
                        this.entityInBlockRange = true;
                    }
                    if (dist < this.rangeProperty.getValue()) {
                        optimalTarget = entity;
                        break;
                    }
                }
                this.target = optimalTarget;
                if (this.blocking) {
                    this.blocking = false;
                    if (this.isHoldingSword()) {
                        Wrapper.sendPacketDirect(KillAura.PLAYER_DIGGING);
                    }
                }
                if (!this.isOccupied()) {
                    if (optimalTarget != null) {
                        if (Wrapper.getTimer().timerSpeed > 1.0f) {
                            Wrapper.getTimer().timerSpeed = 1.0f;
                        }
                        if (this.ncpRotationsProperty.getValue()) {
                            rotations = getRotations(this.target);
                        }
                        else {
                            rotations = getRotations(optimalTarget, event.getPrevYaw(), event.getPrevPitch(), this.maxAngleChangeProperty.getValue().floatValue());
                        }
                        yaw = rotations[0];
                        pitch = rotations[1];
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                        if (this.lockViewProperty.getValue()) {
                            Wrapper.getPlayer().rotationYaw = yaw;
                            Wrapper.getPlayer().rotationPitch = pitch;
                        }
                        if (this.forceUpdateProperty.getValue()) {
                            Wrapper.sendPacketDirect(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
                        }
                        if (this.attackMethodProperty.getValue() == AttackMethod.PRE && this.checkWaitTicks()) {
                            this.tryAttack(event);
                        }
                    }
                }
            }
            else if (!this.isOccupied()) {
                if (this.target != null && this.attackMethodProperty.getValue() == AttackMethod.POST && this.checkWaitTicks()) {
                    this.tryAttack(event);
                }
                if (this.entityInBlockRange && this.autoblockProperty.getValue() && this.isHoldingSword()) {
                    Wrapper.getPlayer().setItemInUse(Wrapper.getPlayer().getCurrentEquippedItem(), Wrapper.getPlayer().getCurrentEquippedItem().getMaxItemUseDuration());
                    if (!this.blocking) {
                        Wrapper.sendPacketDirect(KillAura.BLOCK_PLACEMENT);
                        this.blocking = true;
                    }
                }
            }
            return;
        });
        this.setSuffixListener(this.auraModeProperty);
    }
    
    public static boolean isBlocking() {
        return getInstance().isEnabled() && getInstance().autoblockProperty.getValue() && getInstance().entityInBlockRange;
    }
    
    public static KillAura getInstance() {
        return ModuleManager.getInstance(KillAura.class);
    }
    
    public static double getEffectiveHealth(final EntityLivingBase entity) {
        return entity.getHealth() * (20.0 / entity.getTotalArmorValue());
    }
    
    private static float[] getRotations(final Entity entity) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        double yDist = entity.posY - player.posY;
        final double dist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final boolean close = dist < 2.0 && Math.abs(yDist) < 2.0;
        float pitch;
        if (close && playerEyePos > entityBB.minY) {
            pitch = 60.0f;
        }
        else {
            yDist = ((playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0));
            pitch = (float)(-(StrictMath.atan2(yDist, dist) * 57.29577951308232));
        }
        float yaw = (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f;
        if (close) {
            final int inc = (dist < 1.0) ? 180 : 90;
            yaw = (float)(Math.round(yaw / inc) * inc);
        }
        return new float[] { yaw, pitch };
    }
    
    @Override
    public void onEnable() {
        if (this.scaffold == null) {
            this.scaffold = ModuleManager.getInstance(Scaffold.class);
        }
    }
    
    private boolean isInMenu() {
        final GuiScreen currentScreen = Wrapper.getCurrentScreen();
        return currentScreen != null && !(currentScreen instanceof SkeetUI);
    }
    
    private boolean isOccupied() {
        return this.isInMenu() || this.scaffold.isRotating();
    }
    
    public EntityLivingBase getTarget() {
        return this.target;
    }
    
    @Override
    public void onDisable() {
        if (this.blocking) {
            this.blocking = false;
            Wrapper.sendPacketDirect(KillAura.PLAYER_DIGGING);
        }
        this.target = null;
        this.entityInBlockRange = false;
    }
    
    private boolean checkWaitTicks() {
        if (KillAura.waitTicks > 0) {
            --KillAura.waitTicks;
            return false;
        }
        return true;
    }
    
    private void tryAttack(final UpdatePositionEvent event) {
        if (this.isUsingItem()) {
            return;
        }
        switch (this.auraModeProperty.getValue()) {
            case TICK: {
                if (!this.attackTimer.hasElapsed(480L) || (!this.ncpRotationsProperty.getValue() && !isLookingAtEntity(event.getYaw(), event.getPitch(), this.target, this.rangeProperty.getValue(), this.rayTraceProperty.getValue()))) {
                    break;
                }
                Wrapper.getPlayer().swingItem();
                Wrapper.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                InventoryUtils.windowClick(36, 8, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                Wrapper.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                InventoryUtils.windowClick(44, 0, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                if (!this.keepSprintProperty.getValue() && Wrapper.getPlayer().isSprinting()) {
                    final EntityPlayerSP player = Wrapper.getPlayer();
                    player.motionX *= 0.6;
                    final EntityPlayerSP player2 = Wrapper.getPlayer();
                    player2.motionZ *= 0.6;
                    Wrapper.getPlayer().setSprinting(false);
                    break;
                }
                break;
            }
            case PRIORITY: {
                final int min = this.minApsProperty.getValue().intValue();
                final int max = this.maxApsProperty.getValue().intValue();
                int cps;
                if (min == max) {
                    cps = min;
                }
                else {
                    cps = RandomUtils.nextInt(min, max);
                }
                if (!this.attackTimer.hasElapsed(1000L / cps) || (!this.ncpRotationsProperty.getValue() && !isLookingAtEntity(event.getYaw(), event.getPitch(), this.target, this.rangeProperty.getValue(), this.rayTraceProperty.getValue()))) {
                    break;
                }
                Wrapper.getPlayer().swingItem();
                Wrapper.sendPacket(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                if (!this.keepSprintProperty.getValue() && Wrapper.getPlayer().isSprinting()) {
                    final EntityPlayerSP player3 = Wrapper.getPlayer();
                    player3.motionX *= 0.6;
                    final EntityPlayerSP player4 = Wrapper.getPlayer();
                    player4.motionZ *= 0.6;
                    Wrapper.getPlayer().setSprinting(false);
                    break;
                }
                break;
            }
        }
    }
    
    private boolean isUsingItem() {
        return Wrapper.getPlayer().isUsingItem() && !this.isHoldingSword();
    }
    
    private boolean isHoldingSword() {
        return Wrapper.getPlayer().getCurrentEquippedItem() != null && Wrapper.getPlayer().getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    private boolean isValid(final EntityLivingBase entity) {
        if (!entity.isEntityAlive()) {
            return false;
        }
        if (entity.isInvisible() && !this.targetsProperty.isSelected(Targets.INVISIBLES)) {
            return false;
        }
        if (entity instanceof EntityOtherPlayerMP) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (!this.targetsProperty.isSelected(Targets.PLAYERS)) {
                return false;
            }
            final AntiBot antiBotInstance = ModuleManager.getInstance(AntiBot.class);
            if (antiBotInstance.isEnabled() && !PlayerUtils.checkPing(player)) {
                return false;
            }
            if (!this.targetsProperty.isSelected(Targets.TEAMMATES) && PlayerUtils.isTeamMate(player)) {
                return false;
            }
            if (!this.targetsProperty.isSelected(Targets.FRIENDS) && RadiumClient.getInstance().getFriendManager().isFriend(player)) {
                return false;
            }
        }
        else if (entity instanceof EntityMob) {
            if (!this.targetsProperty.isSelected(Targets.MOBS)) {
                return false;
            }
        }
        else {
            if (!(entity instanceof EntityAnimal)) {
                return false;
            }
            if (!this.targetsProperty.isSelected(Targets.ANIMALS)) {
                return false;
            }
        }
        return Wrapper.getPlayer().getDistanceToEntity(entity) < Math.max(this.blockRangeProperty.getValue(), this.rangeProperty.getValue());
    }
    
    private static boolean isLookingAtEntity(final float yaw, final float pitch, final Entity entity, final double range, final boolean rayTrace) {
        final EntityPlayer player = Wrapper.getPlayer();
        final Vec3 src = Wrapper.getPlayer().getPositionEyes(1.0f);
        final Vec3 rotationVec = Entity.getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        final MovingObjectPosition obj = Wrapper.getWorld().rayTraceBlocks(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (rayTrace) {
                return false;
            }
            if (player.getDistanceToEntity(entity) > 3.0) {
                return false;
            }
        }
        return entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612).calculateIntercept(src, dest) != null;
    }
    
    private static float[] getRotations(final Entity entity, final float prevYaw, final float prevPitch, final float aimSpeed) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final double yDist = (playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0);
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float yaw = interpolateRotation(prevYaw, (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f, aimSpeed);
        final float pitch = interpolateRotation(prevPitch, (float)(-(StrictMath.atan2(yDist, fDist) * 57.29577951308232)), aimSpeed);
        return new float[] { yaw, pitch };
    }
    
    private static float interpolateRotation(final float prev, final float now, final float maxTurn) {
        float var4 = MathHelper.wrapAngleTo180_float(now - prev);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return prev + var4;
    }
    
    private static class AngleSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            final float yaw = Wrapper.getPlayer().currentEvent.getYaw();
            return Double.compare(Math.abs(RotationUtils.getYawToEntity(o1) - yaw), Math.abs(RotationUtils.getYawToEntity(o2) - yaw));
        }
    }
    
    private enum AttackMethod
    {
        PRE("PRE", 0), 
        POST("POST", 1);
        
        private AttackMethod(final String name, final int ordinal) {
        }
    }
    
    private enum AuraMode
    {
        PRIORITY("PRIORITY", 0), 
        TICK("TICK", 1);
        
        private AuraMode(final String name, final int ordinal) {
        }
    }
    
    private static class CombinedSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            int t1 = 0;
            SortingMethod[] values;
            for (int length = (values = SortingMethod.values()).length, i = 0; i < length; ++i) {
                final SortingMethod sortingMethod = values[i];
                final Comparator<EntityLivingBase> sorter = sortingMethod.getSorter();
                if (sorter != this) {
                    t1 += sorter.compare(o1, o2);
                }
            }
            return t1;
        }
    }
    
    private enum SortingMethod
    {
        DISTANCE((Comparator<EntityLivingBase>)new DistanceSorting(null)), 
        HEALTH((Comparator<EntityLivingBase>)new HealthSorting(null)), 
        HURT_TIME((Comparator<EntityLivingBase>)new HurtTimeSorting(null)), 
        ANGLE((Comparator<EntityLivingBase>)new AngleSorting(null)), 
        COMBINED((Comparator<EntityLivingBase>)new CombinedSorting(null));
        
        private final Comparator<EntityLivingBase> sorter;
        
        private SortingMethod(final Comparator<EntityLivingBase> sorter) {
            this.sorter = sorter;
        }
        
        public Comparator<EntityLivingBase> getSorter() {
            return this.sorter;
        }
    }
    
    private static class DistanceSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(o1.getDistanceToEntity(Wrapper.getPlayer()), o2.getDistanceToEntity(Wrapper.getPlayer()));
        }
    }
    
    private static class HealthSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(KillAura.getEffectiveHealth(o1), KillAura.getEffectiveHealth(o2));
        }
    }
    
    private static class HurtTimeSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Integer.compare(20 - o2.hurtResistantTime, 20 - o1.hurtResistantTime);
        }
    }
    
    private enum Targets
    {
        PLAYERS("PLAYERS", 0), 
        TEAMMATES("TEAMMATES", 1), 
        FRIENDS("FRIENDS", 2), 
        INVISIBLES("INVISIBLES", 3), 
        MOBS("MOBS", 4), 
        ANIMALS("ANIMALS", 5);
        
        private Targets(final String name, final int ordinal) {
        }
    }
}
