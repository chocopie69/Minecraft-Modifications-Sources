// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import net.minecraft.inventory.IInventory;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import vip.radium.property.impl.Representation;
import java.util.ArrayList;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import java.util.List;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Chest Stealer", category = ModuleCategory.PLAYER)
public final class ChestStealer extends Module
{
    private final List<Integer> lootedChests;
    private final DoubleProperty clickDelayProperty;
    private final DoubleProperty closeDelayProperty;
    private final Property<Boolean> nameCheckProperty;
    private final TimerUtil timer;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public ChestStealer() {
        this.lootedChests = new ArrayList<Integer>();
        this.clickDelayProperty = new DoubleProperty("Click Delay", 150.0, 10.0, 500.0, 10.0, Representation.MILLISECONDS);
        this.closeDelayProperty = new DoubleProperty("Close Delay", 150.0, 10.0, 500.0, 10.0, Representation.MILLISECONDS);
        this.nameCheckProperty = new Property<Boolean>("Name Check", true);
        this.timer = new TimerUtil();
        this.onPacketReceiveEvent = (event -> {
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                this.timer.reset();
            }
            return;
        });
        GuiChest chest;
        IInventory lowerChestInv;
        int i;
        this.onUpdatePositionEvent = (e -> {
            if (e.isPre() && Wrapper.getCurrentScreen() instanceof GuiChest) {
                chest = (GuiChest)Wrapper.getCurrentScreen();
                lowerChestInv = chest.getLowerChestInventory();
                if (lowerChestInv.getDisplayName().getUnformattedText().contains("Chest") || !this.nameCheckProperty.getValue()) {
                    if (this.isInventoryFull() || InventoryUtils.isInventoryEmpty(lowerChestInv)) {
                        if (this.timer.hasElapsed(this.closeDelayProperty.getValue().longValue())) {
                            Wrapper.getPlayer().closeScreen();
                        }
                    }
                    else {
                        i = 0;
                        while (i < lowerChestInv.getSizeInventory()) {
                            if (this.timer.hasElapsed(this.clickDelayProperty.getValue().longValue()) && InventoryUtils.isValid(lowerChestInv.getStackInSlot(i))) {
                                InventoryUtils.windowClick(chest.inventorySlots.windowId, i, 0, InventoryUtils.ClickType.SHIFT_CLICK);
                                this.timer.reset();
                            }
                            else {
                                ++i;
                            }
                        }
                    }
                }
            }
        });
    }
    
    private boolean isInventoryFull() {
        for (int i = 9; i < 45; ++i) {
            if (!Wrapper.getPlayer().inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
}
