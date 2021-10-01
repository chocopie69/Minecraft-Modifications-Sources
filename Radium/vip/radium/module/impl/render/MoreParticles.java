// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.entity.SpawnParticleEntityEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "More Particles", category = ModuleCategory.RENDER)
public final class MoreParticles extends Module
{
    private final DoubleProperty multiplierProperty;
    @EventLink
    public final Listener<SpawnParticleEntityEvent> onSpawnEntityEvent;
    
    public MoreParticles() {
        this.multiplierProperty = new DoubleProperty("Multiplier", 2.0, 0.0, 10.0, 1.0);
        this.onSpawnEntityEvent = (event -> event.setMultiplier(this.multiplierProperty.getValue().intValue()));
    }
}
