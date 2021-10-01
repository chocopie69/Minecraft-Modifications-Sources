// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.property.impl;

import java.util.function.Supplier;
import vip.radium.property.Property;

public class DoubleProperty extends Property<Double>
{
    private final double min;
    private final double max;
    private final double increment;
    private final Representation representation;
    
    public DoubleProperty(final String label, final double value, final Supplier<Boolean> dependency, final double min, final double max, final double increment, final Representation representation) {
        super(label, value, dependency);
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.representation = representation;
    }
    
    public DoubleProperty(final String label, final double value, final Supplier<Boolean> dependency, final double min, final double max, final double increment) {
        this(label, value, dependency, min, max, increment, Representation.DOUBLE);
    }
    
    public DoubleProperty(final String label, final double value, final double min, final double max, final double increment, final Representation representation) {
        this(label, value, () -> true, min, max, increment, representation);
    }
    
    public DoubleProperty(final String label, final double value, final double min, final double max, final double increment) {
        this(label, value, () -> true, min, max, increment, Representation.DOUBLE);
    }
    
    public Representation getRepresentation() {
        return this.representation;
    }
    
    @Override
    public void setValue(Double value) {
        if (this.value != null && (double)this.value != value) {
            if (value < this.min) {
                value = this.min;
            }
            else if (value > this.max) {
                value = this.max;
            }
        }
        super.setValue(value);
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public double getIncrement() {
        return this.increment;
    }
}
