// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.ghost;

import vip.radium.module.ModuleManager;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Reach", category = ModuleCategory.GHOST)
public class Reach extends Module
{
    private final DoubleProperty reachProperty;
    
    public Reach() {
        this.reachProperty = new DoubleProperty("Reach", 3.5, 3.0, 6.0, 0.05);
    }
    
    private static Reach getInstance() {
        return ModuleManager.getInstance(Reach.class);
    }
    
    public static boolean isReachEnabled() {
        return getInstance().isEnabled();
    }
    
    public static float getReachValue() {
        return getInstance().reachProperty.getValue().floatValue();
    }
}
