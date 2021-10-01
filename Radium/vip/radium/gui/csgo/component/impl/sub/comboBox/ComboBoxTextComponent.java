// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component.impl.sub.comboBox;

import vip.radium.gui.csgo.component.impl.sub.text.TextComponent;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.Component;

public final class ComboBoxTextComponent extends Component implements PredicateComponent
{
    private static final int COMBO_BOX_HEIGHT = 10;
    private static final int COMBO_BOX_Y_OFFSET = 1;
    private final ComboBoxComponent comboBoxComponent;
    
    public ComboBoxTextComponent(final Component parent, final String name, final Supplier<Enum<?>[]> getValues, final Consumer<Integer> setValueByIndex, final Supplier<Enum<?>> getValue, final Supplier<List<Enum<?>>> getValueMultiSelect, final Supplier<Boolean> isVisible, final boolean multiSelect, final float x, final float y) {
        super(parent, x, y, 40.166668f, 16.0f);
        this.addChild(this.comboBoxComponent = new ComboBoxComponent(this, 0.0f, 6.0f, this.getWidth(), 10.0f) {
            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
            
            @Override
            public Enum<?> getValue() {
                return getValue.get();
            }
            
            @Override
            public void setValue(final int index) {
                setValueByIndex.accept(index);
            }
            
            @Override
            public List<Enum<?>> getMultiSelectValues() {
                return getValueMultiSelect.get();
            }
            
            @Override
            public boolean isMultiSelectable() {
                return multiSelect;
            }
            
            @Override
            public Enum<?>[] getValues() {
                return (Enum<?>[])getValues.get();
            }
        });
        this.addChild(new TextComponent(this, name, 1.0f, 0.0f));
    }
    
    public ComboBoxTextComponent(final Component parent, final String name, final Supplier<Enum<?>[]> getValues, final Consumer<Integer> setValueByIndex, final Supplier<Enum<?>> getValue, final Supplier<List<Enum<?>>> getValueMultiSelect, final Supplier<Boolean> isVisible, final boolean multiSelect) {
        this(parent, name, getValues, setValueByIndex, getValue, getValueMultiSelect, isVisible, multiSelect, 0.0f, 0.0f);
    }
    
    public ComboBoxTextComponent(final Component parent, final String name, final Supplier<Enum<?>[]> getValues, final Consumer<Integer> setValueByIndex, final Supplier<Enum<?>> getValue, final Supplier<List<Enum<?>>> getValueMultiSelect, final boolean multiSelect) {
        this(parent, name, getValues, setValueByIndex, getValue, getValueMultiSelect, () -> true, multiSelect, 0.0f, 0.0f);
    }
    
    @Override
    public boolean isVisible() {
        return this.comboBoxComponent.isVisible();
    }
    
    public ComboBoxComponent getComboBoxComponent() {
        return this.comboBoxComponent;
    }
}
