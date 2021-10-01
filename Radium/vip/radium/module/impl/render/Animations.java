// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.module.ModuleManager;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Animations", category = ModuleCategory.RENDER)
public final class Animations extends Module
{
    public final EnumProperty<Mode> animationModeProperty;
    public final Property<Boolean> equipProgressProperty;
    public final DoubleProperty equipProgMultProperty;
    public final DoubleProperty itemScale;
    public final DoubleProperty swingSpeed;
    public final DoubleProperty xPosProperty;
    public final DoubleProperty yPosProperty;
    public final DoubleProperty zPosProperty;
    
    public static Animations getInstance() {
        return ModuleManager.getInstance(Animations.class);
    }
    
    public Animations() {
        this.animationModeProperty = new EnumProperty<Mode>("Mode", Mode.LUL);
        this.equipProgressProperty = new Property<Boolean>("Equip Prog", true);
        this.equipProgMultProperty = new DoubleProperty("E-Prog Multiplier", 2.0, this.equipProgressProperty::getValue, 0.5, 3.0, 0.1);
        this.itemScale = new DoubleProperty("Item Scale", 0.7, 0.0, 2.0, 0.05);
        this.swingSpeed = new DoubleProperty("Swing Duration", 1.0, 0.1, 2.0, 0.1);
        this.xPosProperty = new DoubleProperty("X", 0.0, -1.0, 1.0, 0.05);
        this.yPosProperty = new DoubleProperty("Y", 0.0, -1.0, 1.0, 0.05);
        this.zPosProperty = new DoubleProperty("Z", 0.0, -1.0, 1.0, 0.05);
        this.toggle();
        this.setHidden(true);
    }
    
    public enum Mode
    {
        LOL("LOL", 0), 
        LEL("LEL", 1), 
        LIL("LIL", 2), 
        LUL("LUL", 3), 
        EXHIBIBI("EXHIBIBI", 4), 
        EXHIBOBO("EXHIBOBO", 5), 
        OLD("OLD", 6), 
        SWING("SWING", 7);
        
        private Mode(final String name, final int ordinal) {
        }
    }
}
