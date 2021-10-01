// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import java.util.Iterator;
import org.reflections.Store;
import java.util.function.Predicate;
import org.reflections.util.FilterBuilder;

public class SubTypesScanner extends AbstractScanner
{
    public SubTypesScanner() {
        this(true);
    }
    
    public SubTypesScanner(final boolean excludeObjectClass) {
        if (excludeObjectClass) {
            this.filterResultsBy(new FilterBuilder().exclude(Object.class.getName()));
        }
    }
    
    @Override
    public void scan(final Object cls, final Store store) {
        final String className = this.getMetadataAdapter().getClassName(cls);
        final String superclass = this.getMetadataAdapter().getSuperclassName(cls);
        if (this.acceptResult(superclass)) {
            this.put(store, superclass, className);
        }
        for (final String anInterface : this.getMetadataAdapter().getInterfacesNames(cls)) {
            if (this.acceptResult(anInterface)) {
                this.put(store, anInterface, className);
            }
        }
    }
}
