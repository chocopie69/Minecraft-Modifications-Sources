// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.combat;

import net.minecraft.potion.Potion;
import java.util.List;
import net.minecraft.item.ItemSkull;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import vip.radium.utils.InventoryUtils;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.world.Scaffold;
import vip.radium.utils.MovementUtils;
import vip.radium.property.impl.Representation;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.item.ItemPotion;
import vip.radium.utils.Wrapper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemStack;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.player.MoveEntityEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.WindowClickEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Auto Potion", category = ModuleCategory.COMBAT)
public final class AutoPotion extends Module
{
    private static final PotionType[] VALID_POTIONS;
    private final C08PacketPlayerBlockPlacement THROW_POTION_PACKET;
    private final Property<Boolean> potionsProperty;
    private final Property<Boolean> headsProperty;
    private final Property<Boolean> jumpOnlyProperty;
    private final DoubleProperty healthProperty;
    private final DoubleProperty slotProperty;
    private final DoubleProperty delayProperty;
    private final Property<Boolean> distanceCheckProperty;
    private final DoubleProperty minDistanceProperty;
    private final TimerUtil interactionTimer;
    private int prevSlot;
    private boolean potting;
    private String potionCounter;
    private int jumpTicks;
    private boolean jump;
    @EventLink
    public final Listener<WindowClickEvent> onWindowClickEvent;
    @EventLink
    @Priority(0)
    public final Listener<MoveEntityEvent> onMoveEntityEvent;
    @EventLink
    @Priority(0)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    static {
        VALID_POTIONS = new PotionType[] { PotionType.HEALTH, PotionType.REGEN, PotionType.SPEED };
    }
    
    private boolean checkEffectAmplifier(final ItemStack stack, final PotionEffect effectToCheck) {
        int bestAmplifier = -1;
        ItemStack bestStack = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stackInSlot = Wrapper.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() instanceof ItemPotion) {
                final ItemPotion itemPotion = (ItemPotion)stackInSlot.getItem();
                for (final PotionEffect effect : itemPotion.getEffects(stackInSlot.getMetadata())) {
                    final int amp = effect.getAmplifier();
                    if (effect.getPotionID() == effectToCheck.getPotionID() && amp > bestAmplifier) {
                        bestStack = stackInSlot;
                        bestAmplifier = amp;
                    }
                }
            }
        }
        return bestStack == stack;
    }
    
    public boolean isPlayerInRange(final double distance) {
        final Entity player = Wrapper.getPlayer();
        final double x = player.posX;
        final double y = player.posY;
        final double z = player.posZ;
        for (final EntityPlayer entity : Wrapper.getLoadedPlayers()) {
            if (!entity.isSpectator() && entity instanceof EntityOtherPlayerMP) {
                final double d1 = entity.getDistanceSq(x, y, z);
                if (d1 < distance * distance) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public AutoPotion() {
        this.THROW_POTION_PACKET = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
        this.potionsProperty = new Property<Boolean>("Potions", true);
        this.headsProperty = new Property<Boolean>("Heads", true);
        this.jumpOnlyProperty = new Property<Boolean>("Force Jump", true, this.potionsProperty::getValue);
        this.healthProperty = new DoubleProperty("Health", 6.0, this::hasModeSelected, 1.0, 10.0, 0.5);
        this.slotProperty = new DoubleProperty("Slot", 7.0, this::hasModeSelected, 1.0, 9.0, 1.0);
        this.delayProperty = new DoubleProperty("Delay", 400.0, this::hasModeSelected, 0.0, 1000.0, 50.0, Representation.MILLISECONDS);
        this.distanceCheckProperty = new Property<Boolean>("Dist Check", true, this.potionsProperty::getValue);
        this.minDistanceProperty = new DoubleProperty("Min Player Dist", 1.0, () -> this.distanceCheckProperty.isAvailable() && this.distanceCheckProperty.getValue(), 0.1, 5.0, 0.1);
        this.interactionTimer = new TimerUtil();
        this.onWindowClickEvent = (event -> this.interactionTimer.reset());
        this.onMoveEntityEvent = (event -> {
            if (this.jump && this.jumpTicks >= 0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            return;
        });
        float health;
        int slot;
        ItemStack stack;
        ItemPotion itemPotion;
        boolean validEffects;
        final Iterator<PotionEffect> iterator;
        PotionEffect effect;
        PotionType[] valid_POTIONS;
        final Object o;
        int length;
        int j = 0;
        PotionType potionType;
        final Object o2;
        int length2;
        int k = 0;
        final Requirement[] array;
        Requirement requirement;
        double xDist;
        double zDist;
        double speed;
        boolean shouldPredict;
        boolean shouldJump;
        boolean onGround;
        boolean jumpOnly;
        int potSlot;
        int potionSlot;
        int i;
        ItemStack stack2;
        int headSlot;
        int desiredSlot;
        int oldSlot;
        this.onUpdatePositionEvent = (event -> {
            if (this.potionsProperty.getValue()) {
                if (event.isPre()) {
                    if (this.jump) {
                        --this.jumpTicks;
                        if (MovementUtils.isOnGround()) {
                            this.jump = false;
                            this.jumpTicks = -1;
                        }
                    }
                    if (ModuleManager.getInstance(Scaffold.class).isEnabled()) {
                        return;
                    }
                    else {
                        this.potionCounter = Integer.toString(this.getValidPotionsInInv());
                        if (this.interactionTimer.hasElapsed(this.delayProperty.getValue().longValue())) {
                            health = this.healthProperty.getValue().floatValue() * 2.0f;
                            for (slot = 9; slot < 45; ++slot) {
                                stack = Wrapper.getStackInSlot(slot);
                                if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                                    itemPotion = (ItemPotion)stack.getItem();
                                    validEffects = false;
                                    itemPotion.getEffects(stack.getMetadata()).iterator();
                                    while (iterator.hasNext()) {
                                        effect = iterator.next();
                                        if (this.checkEffectAmplifier(stack, effect)) {
                                            valid_POTIONS = AutoPotion.VALID_POTIONS;
                                            for (length = o.length; j < length; ++j) {
                                                potionType = valid_POTIONS[j];
                                                if (potionType.potionId == effect.getPotionID()) {
                                                    validEffects = true;
                                                    potionType.requirements;
                                                    length2 = o2.length;
                                                    while (k < length2) {
                                                        requirement = array[k];
                                                        if (!requirement.test(health, effect.getAmplifier(), potionType.potionId)) {
                                                            validEffects = false;
                                                            break;
                                                        }
                                                        else {
                                                            ++k;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (validEffects) {
                                        if (MovementUtils.isOverVoid()) {
                                            return;
                                        }
                                        else if (Wrapper.getMinecraft().currentScreen != null) {
                                            return;
                                        }
                                        else if (this.distanceCheckProperty.getValue() && this.isPlayerInRange(this.minDistanceProperty.getValue())) {
                                            return;
                                        }
                                        else {
                                            this.prevSlot = Wrapper.getPlayer().inventory.currentItem;
                                            xDist = Wrapper.getPlayer().posX - Wrapper.getPlayer().lastTickPosX;
                                            zDist = Wrapper.getPlayer().posZ - Wrapper.getPlayer().lastTickPosZ;
                                            speed = StrictMath.sqrt(xDist * xDist + zDist * zDist);
                                            shouldPredict = (speed > 0.38);
                                            shouldJump = (speed < 0.22100000083446503);
                                            onGround = MovementUtils.isOnGround();
                                            jumpOnly = this.jumpOnlyProperty.getValue();
                                            if ((shouldJump || jumpOnly) && onGround && !MovementUtils.isBlockAbove() && MovementUtils.getJumpBoostModifier() == 0) {
                                                Wrapper.getPlayer().motionX = 0.0;
                                                Wrapper.getPlayer().motionZ = 0.0;
                                                event.setPitch(-90.0f);
                                                Wrapper.getPlayer().jump();
                                                this.jump = true;
                                                this.jumpTicks = 9;
                                            }
                                            else if ((shouldPredict || onGround) && !jumpOnly) {
                                                event.setYaw(MovementUtils.getMovementDirection());
                                                event.setPitch(shouldPredict ? 0.0f : 45.0f);
                                            }
                                            else {
                                                return;
                                            }
                                            KillAura.waitTicks = 2;
                                            if (slot >= 36) {
                                                potSlot = slot - 36;
                                            }
                                            else {
                                                potionSlot = this.slotProperty.getValue().intValue() - 1;
                                                InventoryUtils.windowClick(slot, potionSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                                potSlot = potionSlot;
                                            }
                                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(potSlot));
                                            this.potting = true;
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else if (this.potting && this.prevSlot != -1) {
                    Wrapper.sendPacketDirect(this.THROW_POTION_PACKET);
                    Wrapper.sendPacketDirect(new C09PacketHeldItemChange(this.prevSlot));
                    this.interactionTimer.reset();
                    this.prevSlot = -1;
                    this.potting = false;
                    return;
                }
            }
            if (this.headsProperty.getValue() && event.isPre() && this.interactionTimer.hasElapsed(this.delayProperty.getValue().longValue()) && !ModuleManager.getInstance(Scaffold.class).isEnabled() && Wrapper.getPlayer().getHealth() <= this.healthProperty.getValue() * 2.0) {
                i = 0;
                while (i < 45) {
                    stack2 = Wrapper.getStackInSlot(i);
                    if (stack2 != null && stack2.getItem() instanceof ItemSkull && stack2.getDisplayName().contains("Golden")) {
                        KillAura.waitTicks = 2;
                        if (i >= 36) {
                            headSlot = i - 36;
                        }
                        else {
                            desiredSlot = this.slotProperty.getValue().intValue() - 1;
                            InventoryUtils.windowClick(i, desiredSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                            headSlot = desiredSlot;
                        }
                        oldSlot = Wrapper.getPlayer().inventory.currentItem;
                        Wrapper.sendPacketDirect(new C09PacketHeldItemChange(headSlot));
                        Wrapper.sendPacketDirect(this.THROW_POTION_PACKET);
                        Wrapper.sendPacketDirect(new C09PacketHeldItemChange(oldSlot));
                        this.interactionTimer.reset();
                    }
                    else {
                        ++i;
                    }
                }
            }
            return;
        });
        this.setSuffix(() -> this.potionCounter);
    }
    
    private boolean hasModeSelected() {
        return this.potionsProperty.getValue() || this.headsProperty.getValue();
    }
    
    @Override
    public void onEnable() {
        this.potionCounter = "0";
        this.prevSlot = -1;
        this.jump = false;
        this.jumpTicks = -1;
        this.potting = false;
    }
    
    private int getValidPotionsInInv() {
        int count = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata()) && InventoryUtils.isBuffPotion(stack)) {
                final ItemPotion itemPotion = (ItemPotion)stack.getItem();
                final List<PotionEffect> effects = itemPotion.getEffects(stack.getMetadata());
                if (effects != null) {
                    for (final PotionEffect effect : effects) {
                        boolean breakOuter = false;
                        PotionType[] valid_POTIONS;
                        for (int length = (valid_POTIONS = AutoPotion.VALID_POTIONS).length, j = 0; j < length; ++j) {
                            final PotionType type = valid_POTIONS[j];
                            if (type.potionId == effect.getPotionID()) {
                                ++count;
                                breakOuter = true;
                                break;
                            }
                        }
                        if (breakOuter) {
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }
    
    private enum PotionType
    {
        SPEED("SPEED", 0, Potion.moveSpeed.id, new Requirement[] { Requirements.BETTER_THAN_CURRENT.getRequirement() }), 
        REGEN("REGEN", 1, Potion.regeneration.id, new Requirement[] { Requirements.HEALTH_BELOW.getRequirement(), Requirements.BETTER_THAN_CURRENT.getRequirement() }), 
        HEALTH("HEALTH", 2, Potion.heal.id, new Requirement[] { Requirements.HEALTH_BELOW.getRequirement() });
        
        private final int potionId;
        private final Requirement[] requirements;
        
        private PotionType(final String name, final int ordinal, final int potionId, final Requirement... requirements) {
            this.potionId = potionId;
            this.requirements = requirements;
        }
    }
    
    private enum Requirements
    {
        BETTER_THAN_CURRENT("BETTER_THAN_CURRENT", 0, (Requirement)new BetterThanCurrentRequirement(null)), 
        HEALTH_BELOW("HEALTH_BELOW", 1, (Requirement)new HealthBelowRequirement(null));
        
        private final Requirement requirement;
        
        private Requirements(final String name, final int ordinal, final Requirement requirement) {
            this.requirement = requirement;
        }
        
        public Requirement getRequirement() {
            return this.requirement;
        }
    }
    
    private static class HealthBelowRequirement implements Requirement
    {
        @Override
        public boolean test(final float healthTarget, final int currentAmplifier, final int potionId) {
            return Wrapper.getPlayer().getHealth() < healthTarget;
        }
    }
    
    private static class BetterThanCurrentRequirement implements Requirement
    {
        @Override
        public boolean test(final float healthTarget, final int currentAmplifier, final int potionId) {
            final PotionEffect effect = Wrapper.getPlayer().getActivePotionEffect(potionId);
            return effect == null || effect.getAmplifier() < currentAmplifier;
        }
    }
    
    private interface Requirement
    {
        boolean test(final float p0, final int p1, final int p2);
    }
}
