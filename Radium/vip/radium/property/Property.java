// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.property;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Property<T>
{
    protected final String label;
    protected final Supplier<Boolean> dependency;
    private final List<ValueChangeListener<T>> valueChangeListeners;
    protected T value;
    
    public Property(final String label, final T value, final Supplier<Boolean> dependency) {
        this.valueChangeListeners = new ArrayList<ValueChangeListener<T>>();
        this.label = label;
        this.value = value;
        this.dependency = dependency;
    }
    
    public Property(final String label, final T value) {
        this(label, value, () -> true);
    }
    
    public void addValueChangeListener(final ValueChangeListener<T> valueChangeListener) {
        this.valueChangeListeners.add(valueChangeListener);
    }
    
    public boolean isAvailable() {
        return this.dependency.get();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public void setValue(final T value) {
        final T oldValue = this.value;
        this.value = value;
        if (oldValue != value) {
            for (final ValueChangeListener<T> valueChangeListener : this.valueChangeListeners) {
                valueChangeListener.onValueChange(oldValue, value);
            }
        }
    }
    
    public void callFirstTime() {
        for (final ValueChangeListener<T> valueChangeListener : this.valueChangeListeners) {
            valueChangeListener.onValueChange(this.value, this.value);
        }
    }
    
    public Class<?> getType() {
        return this.value.getClass();
    }
}
