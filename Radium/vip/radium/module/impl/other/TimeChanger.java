// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import vip.radium.module.ModuleManager;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Time Changer", category = ModuleCategory.OTHER)
public final class TimeChanger extends Module
{
    private final EnumProperty<Time> time;
    
    public static boolean shouldChangeTime() {
        return ModuleManager.getInstance(TimeChanger.class).isEnabled();
    }
    
    public static int getWorldTime() {
        return ModuleManager.getInstance(TimeChanger.class).time.getValue().worldTicks;
    }
    
    public TimeChanger() {
        this.time = new EnumProperty<Time>("World Time", Time.MORNING);
        this.setHidden(true);
        this.toggle();
    }
    
    private enum Time
    {
        NIGHT("NIGHT", 0, 13000), 
        MIDNIGHT("MIDNIGHT", 1, 18000), 
        MORNING("MORNING", 2, 23000), 
        DAY("DAY", 3, 1000);
        
        private final int worldTicks;
        
        private Time(final String name, final int ordinal, final int worldTicks) {
            this.worldTicks = worldTicks;
        }
    }
}
