// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module;

public interface Toggleable
{
    void toggle();
    
    void setEnabled(final boolean p0);
    
    boolean isEnabled();
    
    void onEnable();
    
    void onDisable();
}
