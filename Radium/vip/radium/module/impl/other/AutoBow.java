// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import vip.radium.utils.Wrapper;
import vip.radium.utils.MathUtils;
import vip.radium.utils.MovementUtils;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Auto Bow", category = ModuleCategory.OTHER)
public final class AutoBow extends Module
{
    private static final C07PacketPlayerDigging PLAYER_DIGGING;
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT;
    private boolean isCharging;
    private int chargedTicks;
    private int bowSlot;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition;
    
    static {
        PLAYER_DIGGING = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
        BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);
    }
    
    public AutoBow() {
        int bowSlot;
        boolean needSwitch;
        int physicalHeldItem;
        this.onUpdatePosition = (event -> {
            if (event.isPre()) {
                bowSlot = this.findBowInHotBar();
                if (bowSlot == -1) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Auto Bow", "You must have a bow on your hotbar", NotificationType.ERROR));
                    this.toggle();
                }
                else if (!this.isCharging) {
                    if (MathUtils.roundToDecimalPlace(MovementUtils.getBlockHeight(), 0.001) == 0.166) {
                        needSwitch = (Wrapper.getPlayer().inventory.currentItem != bowSlot);
                        if (needSwitch) {
                            Wrapper.sendPacketDirect(new C09PacketHeldItemChange(bowSlot));
                        }
                        this.bowSlot = bowSlot;
                        Wrapper.sendPacketDirect(AutoBow.BLOCK_PLACEMENT);
                        this.isCharging = true;
                        this.chargedTicks = 0;
                    }
                }
                else {
                    ++this.chargedTicks;
                    if (bowSlot != this.bowSlot) {
                        this.toggle();
                    }
                    else {
                        switch (this.chargedTicks) {
                            case 2: {
                                event.setPitch(0.0f);
                                event.setYaw(MovementUtils.getMovementDirection());
                                break;
                            }
                            case 3: {
                                physicalHeldItem = Wrapper.getPlayer().inventory.currentItem;
                                Wrapper.sendPacketDirect(AutoBow.PLAYER_DIGGING);
                                if (this.bowSlot != physicalHeldItem) {
                                    Wrapper.sendPacketDirect(new C09PacketHeldItemChange(physicalHeldItem));
                                }
                                this.isCharging = false;
                                this.toggle();
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.isCharging = false;
        this.chargedTicks = 0;
    }
    
    private int findBowInHotBar() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBow) {
                return i - 36;
            }
        }
        return -1;
    }
}
