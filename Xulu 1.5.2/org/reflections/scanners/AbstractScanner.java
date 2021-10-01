// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import org.reflections.adapters.MetadataAdapter;
import org.reflections.util.Utils;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.vfs.Vfs;
import java.util.function.Predicate;
import org.reflections.Configuration;

public abstract class AbstractScanner implements Scanner
{
    private Configuration configuration;
    private Predicate<String> resultFilter;
    
    public AbstractScanner() {
        this.resultFilter = (s -> true);
    }
    
    @Override
    public boolean acceptsInput(final String file) {
        return this.getMetadataAdapter().acceptsInput(file);
    }
    
    @Override
    public Object scan(final Vfs.File file, Object classObject, final Store store) {
        if (classObject == null) {
            try {
                classObject = this.configuration.getMetadataAdapter().getOrCreateClassObject(file);
            }
            catch (Exception e) {
                throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), e);
            }
        }
        this.scan(classObject, store);
        return classObject;
    }
    
    public abstract void scan(final Object p0, final Store p1);
    
    protected void put(final Store store, final String key, final String value) {
        store.put(Utils.index(this.getClass()), key, value);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    @Override
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    public Predicate<String> getResultFilter() {
        return this.resultFilter;
    }
    
    public void setResultFilter(final Predicate<String> resultFilter) {
        this.resultFilter = resultFilter;
    }
    
    @Override
    public Scanner filterResultsBy(final Predicate<String> filter) {
        this.setResultFilter(filter);
        return this;
    }
    
    @Override
    public boolean acceptResult(final String fqn) {
        return fqn != null && this.resultFilter.test(fqn);
    }
    
    protected MetadataAdapter getMetadataAdapter() {
        return this.configuration.getMetadataAdapter();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
