// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.world;

import vip.radium.utils.InventoryUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import vip.radium.module.impl.combat.KillAura;
import net.minecraft.item.ItemTool;
import vip.radium.utils.Wrapper;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Auto Tool", category = ModuleCategory.WORLD)
public final class AutoTool extends Module
{
    private final Property<Boolean> autoSwordProperty;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public AutoTool() {
        this.autoSwordProperty = new Property<Boolean>("Auto Sword", true);
        MovingObjectPosition objectMouseOver;
        final Object o;
        final BlockPos blockPos2;
        BlockPos blockPos;
        Block block;
        float strongestToolStr;
        int strongestToolSlot;
        int i;
        ItemStack stack;
        float strVsBlock;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                objectMouseOver = Wrapper.getMinecraft().objectMouseOver;
                if (o != null && Wrapper.getGameSettings().keyBindAttack.isKeyDown()) {
                    if (objectMouseOver.entityHit != null) {
                        this.doSwordSwap();
                    }
                    else {
                        objectMouseOver.getBlockPos();
                        if ((blockPos = blockPos2) != null) {
                            block = Wrapper.getWorld().getBlockState(blockPos).getBlock();
                            strongestToolStr = 1.0f;
                            strongestToolSlot = -1;
                            for (i = 36; i < 45; ++i) {
                                stack = Wrapper.getStackInSlot(i);
                                if (stack != null && stack.getItem() instanceof ItemTool) {
                                    strVsBlock = stack.getStrVsBlock(block);
                                    if (strVsBlock > strongestToolStr) {
                                        strongestToolStr = strVsBlock;
                                        strongestToolSlot = i;
                                    }
                                }
                            }
                            if (strongestToolSlot != -1) {
                                Wrapper.getPlayer().inventory.currentItem = strongestToolSlot - 36;
                            }
                        }
                    }
                }
                else if (KillAura.getInstance().getTarget() != null) {
                    this.doSwordSwap();
                }
            }
        });
    }
    
    private void doSwordSwap() {
        if (!this.autoSwordProperty.getValue()) {
            return;
        }
        double damage = 1.0;
        int slot = -1;
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSword) {
                final double damageVs = InventoryUtils.getItemDamage(stack);
                if (damageVs > damage) {
                    damage = damageVs;
                    slot = i;
                }
            }
        }
        if (slot != -1) {
            Wrapper.getPlayer().inventory.currentItem = slot - 36;
        }
    }
}
