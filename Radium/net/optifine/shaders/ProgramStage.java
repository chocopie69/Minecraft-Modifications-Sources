// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders;

public enum ProgramStage
{
    NONE("NONE", 0, ""), 
    SHADOW("SHADOW", 1, "shadow"), 
    GBUFFERS("GBUFFERS", 2, "gbuffers"), 
    DEFERRED("DEFERRED", 3, "deferred"), 
    COMPOSITE("COMPOSITE", 4, "composite");
    
    private String name;
    
    private ProgramStage(final String name2, final int ordinal, final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
