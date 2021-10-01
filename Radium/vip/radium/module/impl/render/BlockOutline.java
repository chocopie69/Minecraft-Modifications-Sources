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

@ModuleInfo(label = "Block Outline", category = ModuleCategory.RENDER)
public final class BlockOutline extends Module
{
    private final Property<Integer> blockOutlineColorProperty;
    
    public BlockOutline() {
        this.blockOutlineColorProperty = new Property<Integer>("Outline Color", Colors.PURPLE);
    }
    
    public static float getOutlineAlpha() {
        return (ModuleManager.getInstance(BlockOutline.class).blockOutlineColorProperty.getValue() >> 25 & 0xFF) / 255.0f;
    }
    
    public static int getOutlineColor() {
        return ModuleManager.getInstance(BlockOutline.class).blockOutlineColorProperty.getValue();
    }
    
    public static boolean isOutlineActive() {
        return ModuleManager.getInstance(BlockOutline.class).isEnabled();
    }
}
