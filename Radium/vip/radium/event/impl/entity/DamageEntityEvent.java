// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.entity;

import net.minecraft.entity.EntityLivingBase;
import vip.radium.event.Event;

public final class DamageEntityEvent implements Event
{
    private final EntityLivingBase entity;
    private final double damage;
    
    public DamageEntityEvent(final EntityLivingBase entity, final double damage) {
        this.entity = entity;
        this.damage = damage;
    }
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public double getDamage() {
        return this.damage;
    }
}
