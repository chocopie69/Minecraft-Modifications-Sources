// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.config;

import com.google.gson.JsonObject;

public interface Serializable
{
    JsonObject save();
    
    void load(final JsonObject p0);
}
