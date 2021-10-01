// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import java.util.Iterator;
import java.lang.annotation.Inherited;
import org.reflections.Store;

public class TypeAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object cls, final Store store) {
        final String className = this.getMetadataAdapter().getClassName(cls);
        for (final String annotationType : this.getMetadataAdapter().getClassAnnotationNames(cls)) {
            if (this.acceptResult(annotationType) || annotationType.equals(Inherited.class.getName())) {
                this.put(store, annotationType, className);
            }
        }
    }
}
