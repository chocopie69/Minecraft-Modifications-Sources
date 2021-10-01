// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.entity;

import net.minecraft.entity.EntityLivingBase;
import vip.radium.event.Event;

public class EntitySwingEvent implements Event
{
    private final int entityId;
    
    public EntitySwingEvent(final EntityLivingBase entity) {
        this.entityId = entity.getEntityId();
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
