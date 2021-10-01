// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.ghost;

import org.apache.commons.lang3.RandomUtils;
import vip.radium.utils.Wrapper;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.world.TickEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Auto Clicker", category = ModuleCategory.GHOST)
public class AutoClicker extends Module
{
    private final DoubleProperty minApsProperty;
    private final DoubleProperty maxApsProperty;
    private final Property<Boolean> rightClickProperty;
    private final TimerUtil cpsTimer;
    @EventLink
    public final Listener<TickEvent> onTickEvent;
    
    public AutoClicker() {
        this.minApsProperty = new DoubleProperty("Min APS", 9.0, 1.0, 20.0, 0.1);
        this.maxApsProperty = new DoubleProperty("Max APS", 12.0, 1.0, 20.0, 0.1);
        this.rightClickProperty = new Property<Boolean>("Right Click", false);
        this.cpsTimer = new TimerUtil();
        int cps;
        this.onTickEvent = (event -> {
            if (this.rightClickProperty.getValue() && Wrapper.getGameSettings().keyBindUseItem.isKeyDown()) {
                Wrapper.getMinecraft().rightClickMouse();
            }
            else if (Wrapper.getGameSettings().keyBindAttack.isKeyDown() && !Wrapper.getPlayer().isUsingItem()) {
                cps = RandomUtils.nextInt(this.minApsProperty.getValue().intValue(), this.maxApsProperty.getValue().intValue());
                if (this.cpsTimer.hasElapsed(1000 / cps)) {
                    Wrapper.getMinecraft().clickMouse();
                    this.cpsTimer.reset();
                }
            }
        });
    }
}
