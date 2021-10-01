// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.player;

import vip.radium.event.impl.render.HurtShakeEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.ViewClipEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Camera", category = ModuleCategory.PLAYER)
public final class Camera extends Module
{
    private final Property<Boolean> noHurtShakeProperty;
    private final Property<Boolean> viewClipProperty;
    @EventLink
    public final Listener<ViewClipEvent> onViewClipEvent;
    @EventLink
    public final Listener<HurtShakeEvent> onHurtShakeEvent;
    
    public Camera() {
        this.noHurtShakeProperty = new Property<Boolean>("Hurt Shake", true);
        this.viewClipProperty = new Property<Boolean>("View Clip", true);
        this.onViewClipEvent = (event -> {
            if (this.viewClipProperty.getValue()) {
                event.setCancelled();
            }
            return;
        });
        this.onHurtShakeEvent = (event -> {
            if (!this.noHurtShakeProperty.getValue()) {
                event.setCancelled();
            }
        });
    }
}
