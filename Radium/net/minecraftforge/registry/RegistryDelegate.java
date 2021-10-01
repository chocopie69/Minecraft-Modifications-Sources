// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraftforge.registry;

import net.minecraft.util.ResourceLocation;

public interface RegistryDelegate<T>
{
    T get();
    
    ResourceLocation name();
    
    Class<T> type();
}
