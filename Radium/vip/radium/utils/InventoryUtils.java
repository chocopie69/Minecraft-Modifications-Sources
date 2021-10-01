// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAppleGold;
import java.util.Iterator;
import java.util.List;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.inventory.IInventory;

public final class InventoryUtils
{
    public static final int INCLUDE_ARMOR_BEGIN = 5;
    public static final int EXCLUDE_ARMOR_BEGIN = 9;
    public static final int ONLY_HOT_BAR_BEGIN = 36;
    public static final int END = 45;
    
    private InventoryUtils() {
    }
    
    public static boolean isInventoryEmpty(final IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (isValid(inventory.getStackInSlot(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static void openInventory() {
        Wrapper.sendPacketDirect(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
    }
    
    public static void closeInventory() {
        Wrapper.sendPacketDirect(new C0DPacketCloseWindow(Wrapper.getPlayer().inventoryContainer.windowId));
    }
    
    public static int getDepthStriderLevel() {
        return EnchantmentHelper.getDepthStriderModifier(Wrapper.getPlayer());
    }
    
    public static void windowClick(final int windowId, final int slotId, final int mouseButtonClicked, final ClickType mode) {
        Wrapper.getPlayerController().windowClick(windowId, slotId, mouseButtonClicked, mode.ordinal(), Wrapper.getPlayer());
    }
    
    public static void windowClick(final int slotId, final int mouseButtonClicked, final ClickType mode) {
        Wrapper.getPlayerController().windowClick(Wrapper.getPlayer().inventoryContainer.windowId, slotId, mouseButtonClicked, mode.ordinal(), Wrapper.getPlayer());
    }
    
    public static double getDamageReduction(final ItemStack stack) {
        double reduction = 0.0;
        final ItemArmor armor = (ItemArmor)stack.getItem();
        reduction += armor.damageReduceAmount;
        if (stack.isItemEnchanted()) {
            reduction += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25;
        }
        return reduction;
    }
    
    public static boolean isValid(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemBlock) {
            return isGoodBlockStack(stack);
        }
        if (stack.getItem() instanceof ItemSword) {
            return isBestSword(stack);
        }
        if (stack.getItem() instanceof ItemTool) {
            return isBestTool(stack);
        }
        if (stack.getItem() instanceof ItemArmor) {
            return isBestArmor(stack);
        }
        if (stack.getItem() instanceof ItemPotion) {
            return isBuffPotion(stack);
        }
        if (stack.getItem() instanceof ItemFood) {
            return isGoodFood(stack);
        }
        if (stack.getItem() instanceof ItemBow) {
            return isBestBow(stack);
        }
        return isGoodItem(stack);
    }
    
    public static boolean isBestBow(final ItemStack itemStack) {
        double bestBowDmg = -1.0;
        ItemStack bestBow = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBow) {
                final double damage = getBowDamage(stack);
                if (damage > bestBowDmg) {
                    bestBow = stack;
                    bestBowDmg = damage;
                }
            }
        }
        return itemStack == bestBow || getBowDamage(itemStack) > bestBowDmg;
    }
    
    public static double getBowDamage(final ItemStack stack) {
        double damage = 0.0;
        if (stack.getItem() instanceof ItemBow && stack.isItemEnchanted()) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
        }
        return damage;
    }
    
    public static boolean isGoodItem(final ItemStack stack) {
        final Item item = stack.getItem();
        return (!(item instanceof ItemBucket) || ((ItemBucket)item).isFull == Blocks.flowing_water) && (!(item instanceof ItemExpBottle) && !(item instanceof ItemFishingRod) && !(item instanceof ItemEgg) && !(item instanceof ItemSnowball) && !(item instanceof ItemSkull) && !(item instanceof ItemBucket));
    }
    
    public static boolean isBuffPotion(final ItemStack stack) {
        final ItemPotion potion = (ItemPotion)stack.getItem();
        final List<PotionEffect> effects = potion.getEffects(stack);
        for (final PotionEffect effect : effects) {
            if (Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isGoodFood(final ItemStack stack) {
        final ItemFood food = (ItemFood)stack.getItem();
        return food instanceof ItemAppleGold || (food.getHealAmount(stack) >= 4 && food.getSaturationModifier(stack) >= 0.3f);
    }
    
    public static boolean isBestSword(final ItemStack itemStack) {
        double damage = 0.0;
        ItemStack bestStack = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSword) {
                final double newDamage = getItemDamage(stack);
                if (newDamage > damage) {
                    damage = newDamage;
                    bestStack = stack;
                }
            }
        }
        return bestStack == itemStack || getItemDamage(itemStack) >= damage;
    }
    
    public static int getToolType(final ItemStack stack) {
        final ItemTool tool = (ItemTool)stack.getItem();
        if (tool instanceof ItemPickaxe) {
            return 0;
        }
        if (tool instanceof ItemAxe) {
            return 1;
        }
        if (tool instanceof ItemSpade) {
            return 2;
        }
        return -1;
    }
    
    public static boolean isBestTool(final ItemStack itemStack) {
        final int type = getToolType(itemStack);
        Tool bestTool = new Tool(-1, -1.0, null);
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemTool && type == getToolType(stack)) {
                final double efficiency = getToolEfficiency(stack);
                if (efficiency > bestTool.getEfficiency()) {
                    bestTool = new Tool(i, efficiency, stack);
                }
            }
        }
        return bestTool.getStack() == itemStack || getToolEfficiency(itemStack) > bestTool.getEfficiency();
    }
    
    public static float getToolEfficiency(final ItemStack itemStack) {
        final ItemTool tool = (ItemTool)itemStack.getItem();
        float efficiency = tool.getEfficiencyOnProperMaterial();
        final int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
        if (efficiency > 1.0f && lvl > 0) {
            efficiency += lvl * lvl + 1;
        }
        return efficiency;
    }
    
    public static boolean isBestArmor(final ItemStack itemStack) {
        final ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        double reduction = 0.0;
        ItemStack bestStack = null;
        for (int i = 5; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemArmor) {
                final ItemArmor stackArmor = (ItemArmor)stack.getItem();
                if (stackArmor.armorType == itemArmor.armorType) {
                    final double newReduction = getDamageReduction(stack);
                    if (newReduction > reduction) {
                        reduction = newReduction;
                        bestStack = stack;
                    }
                }
            }
        }
        return bestStack == itemStack || getDamageReduction(itemStack) > reduction;
    }
    
    public static boolean isGoodBlockStack(final ItemStack stack) {
        return stack.stackSize >= 1 && isValidBlock(Block.getBlockFromItem(stack.getItem()), true);
    }
    
    public static boolean isValidBlock(final Block block, final boolean toPlace) {
        if (block instanceof BlockContainer) {
            return false;
        }
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.isFullBlock() && block.isFullCube();
        }
        final Material material = block.getMaterial();
        return !material.isReplaceable() && !material.isLiquid();
    }
    
    public static double getItemDamage(final ItemStack stack) {
        double damage = 0.0;
        final Multimap<String, AttributeModifier> attributeModifierMap = stack.getAttributeModifiers();
        for (final String attributeName : attributeModifierMap.keySet()) {
            if (attributeName.equals("generic.attackDamage")) {
                final Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get((Object)attributeName).iterator();
                if (attributeModifiers.hasNext()) {
                    damage += attributeModifiers.next().getAmount();
                    break;
                }
                break;
            }
        }
        if (stack.isItemEnchanted()) {
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
        }
        return damage;
    }
    
    public enum ClickType
    {
        CLICK("CLICK", 0), 
        SHIFT_CLICK("SHIFT_CLICK", 1), 
        SWAP_WITH_HOT_BAR_SLOT("SWAP_WITH_HOT_BAR_SLOT", 2), 
        PLACEHOLDER("PLACEHOLDER", 3), 
        DROP_ITEM("DROP_ITEM", 4);
        
        private ClickType(final String name, final int ordinal) {
        }
    }
    
    private static class Tool
    {
        private final int slot;
        private final double efficiency;
        private final ItemStack stack;
        
        public Tool(final int slot, final double efficiency, final ItemStack stack) {
            this.slot = slot;
            this.efficiency = efficiency;
            this.stack = stack;
        }
        
        public int getSlot() {
            return this.slot;
        }
        
        public double getEfficiency() {
            return this.efficiency;
        }
        
        public ItemStack getStack() {
            return this.stack;
        }
    }
}
