// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import java.util.Arrays;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBlock;
import vip.radium.utils.HypixelGameUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiScreen;
import vip.radium.module.impl.combat.KillAura;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import vip.radium.utils.InventoryUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.client.gui.inventory.GuiInventory;
import vip.radium.utils.Wrapper;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import java.util.ArrayList;
import vip.radium.property.impl.Representation;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.packet.PacketSendEvent;
import java.util.List;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.WindowClickEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Inventory Manager", category = ModuleCategory.PLAYER)
public final class InventoryManager extends Module
{
    private final DoubleProperty clickDelayProperty;
    private final Property<Boolean> whileFightingProperty;
    private final Property<Boolean> archeryProperty;
    private final Property<Boolean> sortHotbarProperty;
    private final Property<Boolean> sortToolsProperty;
    private final Property<Boolean> spoofInventoryProperty;
    private final TimerUtil interactionsTimer;
    @EventLink
    public final Listener<WindowClickEvent> onWindowClickEvent;
    private final int[] bestArmorPieces;
    private final List<Integer> trash;
    private final List<Integer> duplicateSwords;
    private final int[] bestToolSlots;
    private final List<Integer> gappleStackSlots;
    private int bestSwordSlot;
    private int bestBowSlot;
    private boolean openInventory;
    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public InventoryManager() {
        this.clickDelayProperty = new DoubleProperty("Click Delay", 150.0, 10.0, 300.0, 10.0, Representation.MILLISECONDS);
        this.whileFightingProperty = new Property<Boolean>("While Fighting", true);
        this.archeryProperty = new Property<Boolean>("Archery", false);
        this.sortHotbarProperty = new Property<Boolean>("Sort Hotbar", true);
        this.sortToolsProperty = new Property<Boolean>("Sort Tools", false, this.sortHotbarProperty::getValue);
        this.spoofInventoryProperty = new Property<Boolean>("Spoof", true);
        this.interactionsTimer = new TimerUtil();
        this.onWindowClickEvent = (event -> this.interactionsTimer.reset());
        this.bestArmorPieces = new int[4];
        this.trash = new ArrayList<Integer>();
        this.duplicateSwords = new ArrayList<Integer>();
        this.bestToolSlots = new int[3];
        this.gappleStackSlots = new ArrayList<Integer>();
        C16PacketClientStatus packet;
        this.onPacketSendEvent = (event -> {
            if (this.openInventory) {
                if (event.getPacket() instanceof C16PacketClientStatus) {
                    packet = (C16PacketClientStatus)event.getPacket();
                    if (packet.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                        event.setCancelled();
                    }
                }
                else if (event.getPacket() instanceof C0DPacketCloseWindow) {
                    event.setCancelled();
                }
            }
            return;
        });
        this.onPacketReceiveEvent = (event -> {
            if (this.openInventory) {
                if (event.getPacket() instanceof S2DPacketOpenWindow) {
                    this.close();
                }
                else if (event.getPacket() instanceof S2EPacketCloseWindow) {
                    event.setCancelled();
                }
            }
            return;
        });
        GuiScreen currentScreen;
        long clickDelay;
        boolean foundSword;
        int slot3;
        ItemStack stack;
        int toolType;
        ItemArmor armor;
        int pieceSlot;
        int i;
        int piece;
        int armorPieceSlot;
        ItemStack stack2;
        int currentSlot;
        int slot4;
        int bestGappleSlot;
        int[] toolSlots;
        int[] bestToolSlots;
        final Object o;
        int length;
        int j = 0;
        int toolSlot;
        int type;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                currentScreen = Wrapper.getCurrentScreen();
                if (currentScreen == null || currentScreen instanceof GuiInventory) {
                    clickDelay = this.clickDelayProperty.getValue().longValue();
                    if (!(!this.interactionsTimer.hasElapsed(clickDelay))) {
                        this.clear();
                        foundSword = false;
                        for (slot3 = 5; slot3 < 45; ++slot3) {
                            stack = Wrapper.getStackInSlot(slot3);
                            if (stack != null) {
                                if (stack.getItem() instanceof ItemSword && InventoryUtils.isBestSword(stack)) {
                                    if (foundSword) {
                                        this.duplicateSwords.add(slot3);
                                    }
                                    else if (slot3 != this.bestSwordSlot) {
                                        foundSword = true;
                                        this.bestSwordSlot = slot3;
                                    }
                                }
                                else if (stack.getItem() instanceof ItemTool && InventoryUtils.isBestTool(stack)) {
                                    toolType = InventoryUtils.getToolType(stack);
                                    if (toolType != -1 && slot3 != this.bestToolSlots[toolType]) {
                                        this.bestToolSlots[toolType] = slot3;
                                    }
                                }
                                else if (stack.getItem() instanceof ItemArmor && InventoryUtils.isBestArmor(stack)) {
                                    armor = (ItemArmor)stack.getItem();
                                    pieceSlot = this.bestArmorPieces[armor.armorType];
                                    if (pieceSlot == -1 || slot3 != pieceSlot) {
                                        this.bestArmorPieces[armor.armorType] = slot3;
                                    }
                                }
                                else if (stack.getItem() instanceof ItemBow && this.archeryProperty.getValue() && InventoryUtils.isBestBow(stack)) {
                                    if (slot3 != this.bestBowSlot) {
                                        this.bestBowSlot = slot3;
                                    }
                                }
                                else if (stack.getItem() instanceof ItemAppleGold) {
                                    this.gappleStackSlots.add(slot3);
                                }
                                else if (!this.trash.contains(slot3) && !this.isValidStack(stack)) {
                                    this.trash.add(slot3);
                                }
                            }
                        }
                        for (i = 0; i < this.bestArmorPieces.length; ++i) {
                            piece = this.bestArmorPieces[i];
                            if (piece != -1) {
                                armorPieceSlot = i + 5;
                                stack2 = Wrapper.getStackInSlot(armorPieceSlot);
                                if (stack2 == null) {
                                    this.open();
                                    InventoryUtils.windowClick(piece, 0, InventoryUtils.ClickType.SHIFT_CLICK);
                                    this.close();
                                    return;
                                }
                            }
                        }
                        if (!this.whileFightingProperty.getValue() && KillAura.getInstance().getTarget() != null) {
                            this.interactionsTimer.reset();
                        }
                        else if (!this.purgeList(this.duplicateSwords)) {
                            currentSlot = 36;
                            if (this.bestSwordSlot != -1) {
                                if (this.bestSwordSlot != currentSlot) {
                                    this.putSwordInSlot(currentSlot);
                                    return;
                                }
                                else {
                                    ++currentSlot;
                                }
                            }
                            if (!this.purgeList(this.trash)) {
                                if (!this.trash.isEmpty()) {
                                    this.open();
                                    slot4 = this.trash.remove(0);
                                    InventoryUtils.windowClick(slot4, 1, InventoryUtils.ClickType.DROP_ITEM);
                                    if (this.trash.isEmpty()) {
                                        this.close();
                                    }
                                }
                                else if (this.sortHotbarProperty.getValue()) {
                                    if (this.bestBowSlot != -1) {
                                        if (this.bestBowSlot != currentSlot) {
                                            this.putBowInSlot(currentSlot);
                                            return;
                                        }
                                        else {
                                            ++currentSlot;
                                        }
                                    }
                                    if (!this.gappleStackSlots.isEmpty()) {
                                        this.gappleStackSlots.sort((slot1, slot2) -> Wrapper.getStackInSlot(slot2).stackSize - Wrapper.getStackInSlot(slot1).stackSize);
                                        bestGappleSlot = this.gappleStackSlots.get(0);
                                        if (bestGappleSlot != currentSlot) {
                                            this.putGappleInSlot(currentSlot, bestGappleSlot);
                                            this.gappleStackSlots.set(0, currentSlot);
                                            return;
                                        }
                                        else {
                                            ++currentSlot;
                                        }
                                    }
                                    if (this.sortToolsProperty.getValue()) {
                                        toolSlots = new int[] { currentSlot, currentSlot + 1, currentSlot + 2 };
                                        bestToolSlots = this.bestToolSlots;
                                        for (length = o.length; j < length; ++j) {
                                            toolSlot = bestToolSlots[j];
                                            if (toolSlot != -1) {
                                                type = InventoryUtils.getToolType(Wrapper.getStackInSlot(toolSlot));
                                                if (type != -1 && toolSlot != toolSlots[type]) {
                                                    this.putToolsInSlot(type, toolSlots);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    private boolean isValidStack(final ItemStack stack) {
        return (stack.hasDisplayName() && HypixelGameUtils.getGameMode() != HypixelGameUtils.GameMode.BLITZ_SG) || (stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack)) || (stack.getItem() instanceof ItemPotion && InventoryUtils.isBuffPotion(stack)) || (stack.getItem() instanceof ItemFood && InventoryUtils.isGoodFood(stack));
    }
    
    private boolean purgeList(final List<Integer> listOfSlots) {
        if (!listOfSlots.isEmpty()) {
            this.open();
            final int slot = listOfSlots.remove(0);
            InventoryUtils.windowClick(slot, 1, InventoryUtils.ClickType.DROP_ITEM);
            if (listOfSlots.isEmpty()) {
                this.close();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onEnable() {
        this.openInventory = (Wrapper.getCurrentScreen() instanceof GuiInventory);
        this.interactionsTimer.reset();
    }
    
    @Override
    public void onDisable() {
        this.close();
    }
    
    private void clear() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
        this.duplicateSwords.clear();
    }
    
    private void putSwordInSlot(final int swordSlot) {
        this.open();
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId, this.bestSwordSlot, swordSlot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestSwordSlot = swordSlot;
        this.close();
    }
    
    private void putBowInSlot(final int bowSlot) {
        this.open();
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId, this.bestBowSlot, bowSlot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestBowSlot = bowSlot;
        this.close();
    }
    
    private void putGappleInSlot(final int gappleSlot, final int slotIn) {
        this.open();
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId, slotIn, gappleSlot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.close();
    }
    
    private void putToolsInSlot(final int tool, final int[] toolSlots) {
        this.open();
        final int toolSlot = toolSlots[tool];
        InventoryUtils.windowClick(Wrapper.getPlayer().inventoryContainer.windowId, this.bestToolSlots[tool], toolSlot - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
        this.bestToolSlots[tool] = toolSlot;
        this.close();
    }
    
    private void open() {
        if (!this.openInventory) {
            this.interactionsTimer.reset();
            if (this.spoofInventoryProperty.getValue()) {
                InventoryUtils.openInventory();
            }
            this.openInventory = true;
        }
    }
    
    private void close() {
        if (this.openInventory) {
            if (this.spoofInventoryProperty.getValue()) {
                InventoryUtils.closeInventory();
            }
            this.openInventory = false;
        }
    }
}
