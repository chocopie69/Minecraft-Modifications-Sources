// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.vfs.Vfs;

public class ResourcesScanner extends AbstractScanner
{
    @Override
    public boolean acceptsInput(final String file) {
        return !file.endsWith(".class");
    }
    
    @Override
    public Object scan(final Vfs.File file, final Object classObject, final Store store) {
        this.put(store, file.getName(), file.getRelativePath());
        return classObject;
    }
    
    @Override
    public void scan(final Object cls, final Store store) {
        throw new UnsupportedOperationException();
    }
}
