// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.world;

import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "World Color", category = ModuleCategory.WORLD)
public final class WorldColor extends Module
{
    public final Property<Integer> lightMapColorProperty;
    
    public WorldColor() {
        this.lightMapColorProperty = new Property<Integer>("Light Map Color", -32640);
    }
}
