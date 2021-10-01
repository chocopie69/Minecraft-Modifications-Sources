// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.property.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.List;
import vip.radium.property.Property;

public class MultiSelectEnumProperty<T extends Enum<T>> extends Property<List<T>>
{
    private final T[] values;
    
    @SafeVarargs
    public MultiSelectEnumProperty(final String label, final Supplier<Boolean> dependency, final T... values) {
        super(label, Arrays.asList(values), dependency);
        if (values.length == 0) {
            throw new RuntimeException("Must have at least one default value.");
        }
        this.values = this.getEnumConstants();
    }
    
    @SafeVarargs
    public MultiSelectEnumProperty(final String label, final T... values) {
        this(label, () -> true, (Enum[])values);
    }
    
    private T[] getEnumConstants() {
        return (T[])((List)this.value).get(0).getClass().getEnumConstants();
    }
    
    public T[] getValues() {
        return this.values;
    }
    
    public boolean isSelected(final T variant) {
        return this.getValue().contains(variant);
    }
    
    public void setValue(final int index) {
        final List<T> values = new ArrayList<T>((Collection<? extends T>)this.value);
        final T referencedVariant = this.values[index];
        if (values.contains(referencedVariant)) {
            values.remove(referencedVariant);
        }
        else {
            values.add(referencedVariant);
        }
        this.setValue(values);
    }
}
