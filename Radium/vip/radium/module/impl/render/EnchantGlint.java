// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.module.ModuleManager;
import vip.radium.utils.render.Colors;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Enchant Glint", category = ModuleCategory.RENDER)
public final class EnchantGlint extends Module
{
    private final Property<Integer> itemColorProperty;
    private final Property<Integer> armorModelColorProperty;
    
    public EnchantGlint() {
        this.itemColorProperty = new Property<Integer>("Item Glint Color", Colors.RED);
        this.armorModelColorProperty = new Property<Integer>("Armor Glint Color", Colors.RED);
    }
    
    private static EnchantGlint getInstance() {
        return ModuleManager.getInstance(EnchantGlint.class);
    }
    
    public static boolean isGlintEnabled() {
        return getInstance().isEnabled();
    }
    
    public static int getItemColor() {
        return getInstance().itemColorProperty.getValue();
    }
    
    public static int getArmorModelColor() {
        return getInstance().armorModelColorProperty.getValue();
    }
}
