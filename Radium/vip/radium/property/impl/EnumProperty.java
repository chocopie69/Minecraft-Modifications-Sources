// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.property.impl;

import java.util.function.Supplier;
import vip.radium.property.Property;

public class EnumProperty<T extends Enum<T>> extends Property<T>
{
    private final T[] values;
    
    public EnumProperty(final String label, final T value, final Supplier<Boolean> dependency) {
        super(label, value, dependency);
        this.values = this.getEnumConstants();
    }
    
    public EnumProperty(final String label, final T value) {
        this(label, value, () -> true);
    }
    
    private T[] getEnumConstants() {
        return (T[])this.value.getClass().getEnumConstants();
    }
    
    public T[] getValues() {
        return this.values;
    }
    
    public void setValue(final int index) {
        this.setValue(this.values[index]);
    }
}
