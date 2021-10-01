// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import vip.radium.RadiumClient;
import java.lang.reflect.Field;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.property.impl.DoubleProperty;
import java.util.Iterator;
import vip.radium.utils.StringUtils;
import vip.radium.property.impl.EnumProperty;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import vip.radium.utils.render.Translate;
import vip.radium.config.Serializable;
import vip.radium.keybind.Bindable;
import vip.radium.property.Property;
import vip.radium.utils.handler.Manager;

public class Module extends Manager<Property<?>> implements Bindable, Toggleable, Serializable
{
    private final String label;
    private final String description;
    private final ModuleCategory category;
    private final Translate translate;
    private int key;
    private boolean enabled;
    private boolean hidden;
    private Supplier<String> suffix;
    private String updatedSuffix;
    
    public Module() {
        super(new ArrayList());
        this.label = this.getClass().getAnnotation(ModuleInfo.class).label();
        this.description = this.getClass().getAnnotation(ModuleInfo.class).description();
        this.category = this.getClass().getAnnotation(ModuleInfo.class).category();
        this.translate = new Translate(0.0, 0.0);
        this.key = this.getClass().getAnnotation(ModuleInfo.class).key();
    }
    
    public String getUpdatedSuffix() {
        return this.updatedSuffix;
    }
    
    public void setUpdatedSuffix(final String updatedSuffix) {
        this.updatedSuffix = updatedSuffix;
    }
    
    private void updateSuffix(final EnumProperty<?> mode) {
        this.setUpdatedSuffix(StringUtils.upperSnakeCaseToPascal(mode.getValue().name()));
    }
    
    public void setSuffixListener(final EnumProperty<?> mode) {
        this.updateSuffix(mode);
        mode.addValueChangeListener((oldValue, value) -> this.updateSuffix(mode));
    }
    
    public void resetPropertyValues() {
        for (final Property<?> property : this.getElements()) {
            property.callFirstTime();
        }
    }
    
    public Translate getTranslate() {
        return this.translate;
    }
    
    public Supplier<String> getSuffix() {
        return this.suffix;
    }
    
    public void setSuffix(final Supplier<String> suffix) {
        this.suffix = suffix;
    }
    
    public ModuleCategory getCategory() {
        return this.category;
    }
    
    public void reflectProperties() {
        Field[] declaredFields;
        for (int length = (declaredFields = this.getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            final Class<?> type = field.getType();
            if (type.isAssignableFrom(Property.class) || type.isAssignableFrom(DoubleProperty.class) || type.isAssignableFrom(EnumProperty.class) || type.isAssignableFrom(MultiSelectEnumProperty.class)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    this.elements.add((T)field.get(this));
                }
                catch (IllegalAccessException ex) {}
            }
        }
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    @Override
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    @Override
    public void onPress() {
        this.toggle();
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                this.onEnable();
                RadiumClient.getInstance().getEventBus().subscribe(this);
            }
            else {
                RadiumClient.getInstance().getEventBus().unsubscribe(this);
                this.onDisable();
            }
        }
    }
    
    public boolean isVisible() {
        return this.enabled && !this.hidden;
    }
    
    @Override
    public void toggle() {
        this.setEnabled(!this.enabled);
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public JsonObject save() {
        final JsonObject object = new JsonObject();
        object.addProperty("toggled", Boolean.valueOf(this.isEnabled()));
        object.addProperty("key", (Number)this.getKey());
        object.addProperty("hidden", Boolean.valueOf(this.isHidden()));
        final List<Property<?>> properties = this.getElements();
        if (!properties.isEmpty()) {
            final JsonObject propertiesObject = new JsonObject();
            for (final Property<?> property : properties) {
                if (property instanceof DoubleProperty) {
                    propertiesObject.addProperty(property.getLabel(), (Number)((Property<Number>)property).getValue());
                }
                else if (property instanceof EnumProperty) {
                    final EnumProperty<?> enumProperty = (EnumProperty<?>)(EnumProperty)property;
                    propertiesObject.add(property.getLabel(), (JsonElement)new JsonPrimitive(enumProperty.getValue().name()));
                }
                else if (property.getType() == Boolean.class) {
                    propertiesObject.addProperty(property.getLabel(), (Boolean)property.getValue());
                }
                else if (property.getType() == Integer.class) {
                    propertiesObject.addProperty(property.getLabel(), Integer.toHexString((int)property.getValue()));
                }
                else {
                    if (property.getType() != String.class) {
                        continue;
                    }
                    propertiesObject.addProperty(property.getLabel(), (String)property.getValue());
                }
            }
            object.add("Properties", (JsonElement)propertiesObject);
        }
        return object;
    }
    
    @Override
    public void load(final JsonObject object) {
        if (object.has("toggled")) {
            this.setEnabled(object.get("toggled").getAsBoolean());
        }
        if (object.has("key")) {
            this.setKey(object.get("key").getAsInt());
        }
        if (object.has("hidden")) {
            this.setHidden(object.get("hidden").getAsBoolean());
        }
        if (object.has("Properties") && !this.getElements().isEmpty()) {
            final JsonObject propertiesObject = object.getAsJsonObject("Properties");
            for (final Property<?> property : this.getElements()) {
                if (propertiesObject.has(property.getLabel())) {
                    if (property instanceof DoubleProperty) {
                        ((DoubleProperty)property).setValue(propertiesObject.get(property.getLabel()).getAsDouble());
                    }
                    else if (property instanceof EnumProperty) {
                        this.findEnumValue(property, propertiesObject);
                    }
                    else if (property.getValue() instanceof Boolean) {
                        property.setValue((Object)propertiesObject.get(property.getLabel()).getAsBoolean());
                    }
                    else if (property.getValue() instanceof Integer) {
                        property.setValue((Object)(int)Long.parseLong(propertiesObject.get(property.getLabel()).getAsString(), 16));
                    }
                    else {
                        if (!(property.getValue() instanceof String)) {
                            continue;
                        }
                        property.setValue(propertiesObject.get(property.getLabel()).getAsString());
                    }
                }
            }
        }
    }
    
    private <T extends Enum<T>> void findEnumValue(final Property<?> property, final JsonObject propertiesObject) {
        final EnumProperty<T> enumProperty = (EnumProperty<T>)(EnumProperty)property;
        final String value = propertiesObject.getAsJsonPrimitive(property.getLabel()).getAsString();
        T[] values;
        for (int length = (values = enumProperty.getValues()).length, i = 0; i < length; ++i) {
            final T possibleValue = values[i];
            if (possibleValue.name().equalsIgnoreCase(value)) {
                enumProperty.setValue(possibleValue);
                break;
            }
        }
    }
}
