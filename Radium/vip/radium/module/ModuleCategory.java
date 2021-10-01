// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module;

public enum ModuleCategory
{
    COMBAT("COMBAT", 0), 
    MOVEMENT("MOVEMENT", 1), 
    PLAYER("PLAYER", 2), 
    RENDER("RENDER", 3), 
    WORLD("WORLD", 4), 
    GHOST("GHOST", 5), 
    OTHER("OTHER", 6);
    
    private ModuleCategory(final String name, final int ordinal) {
    }
}
