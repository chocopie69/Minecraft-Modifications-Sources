// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.vfs.Vfs;
import java.util.function.Predicate;
import org.reflections.Configuration;

public interface Scanner
{
    void setConfiguration(final Configuration p0);
    
    Scanner filterResultsBy(final Predicate<String> p0);
    
    boolean acceptsInput(final String p0);
    
    Object scan(final Vfs.File p0, final Object p1, final Store p2);
    
    boolean acceptResult(final String p0);
}
