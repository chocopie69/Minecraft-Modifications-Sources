// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other.entityDesync;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import vip.radium.utils.Wrapper;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.entity.Entity;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Enttiy Desync", category = ModuleCategory.OTHER)
public final class EntityDesync extends Module
{
    private Entity ridingEntity;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition;
    
    public EntityDesync() {
        EntityPlayer player;
        this.onUpdatePosition = (event -> {
            if (this.ridingEntity != null) {
                player = Wrapper.getPlayer();
                this.ridingEntity.posX = player.posX;
                this.ridingEntity.posY = player.posY;
                this.ridingEntity.posZ = player.posZ;
                Wrapper.sendPacketDirect(new CPacketVehicleMove(this.ridingEntity));
            }
        });
    }
    
    @Override
    public void onEnable() {
        final Entity ridingEntity = Wrapper.getPlayer().ridingEntity;
        if (ridingEntity != null) {
            this.ridingEntity = ridingEntity;
            Wrapper.getPlayer().dismountEntity(ridingEntity);
            Wrapper.getWorld().removeEntity(ridingEntity);
        }
    }
    
    @Override
    public void onDisable() {
        this.ridingEntity = null;
    }
}
