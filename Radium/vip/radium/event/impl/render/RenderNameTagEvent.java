// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.render;

import net.minecraft.entity.EntityLivingBase;
import vip.radium.event.CancellableEvent;

public final class RenderNameTagEvent extends CancellableEvent
{
    private final EntityLivingBase entityLivingBase;
    
    public RenderNameTagEvent(final EntityLivingBase entityLivingBase) {
        this.entityLivingBase = entityLivingBase;
    }
    
    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }
}
