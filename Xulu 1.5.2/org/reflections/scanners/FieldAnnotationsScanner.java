// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import java.util.Iterator;
import java.util.List;
import org.reflections.Store;

public class FieldAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object cls, final Store store) {
        final String className = this.getMetadataAdapter().getClassName(cls);
        final List<Object> fields = this.getMetadataAdapter().getFields(cls);
        for (final Object field : fields) {
            final List<String> fieldAnnotations = this.getMetadataAdapter().getFieldAnnotationNames(field);
            for (final String fieldAnnotation : fieldAnnotations) {
                if (this.acceptResult(fieldAnnotation)) {
                    final String fieldName = this.getMetadataAdapter().getFieldName(field);
                    this.put(store, fieldAnnotation, String.format("%s.%s", className, fieldName));
                }
            }
        }
    }
}
