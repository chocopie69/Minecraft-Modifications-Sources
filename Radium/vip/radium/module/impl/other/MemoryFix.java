// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import vip.radium.module.ModuleManager;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Memory Fix", category = ModuleCategory.OTHER)
public final class MemoryFix extends Module
{
    public MemoryFix() {
        this.toggle();
        this.setHidden(true);
    }
    
    public static boolean cancelGarbageCollection() {
        return ModuleManager.getInstance(MemoryFix.class).isEnabled();
    }
}
