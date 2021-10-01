// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.utils.render.LockedResolution;
import net.minecraft.client.gui.Gui;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.PlayerInfoCache;
import vip.radium.utils.Wrapper;
import vip.radium.event.CancellableEvent;
import vip.radium.utils.render.Colors;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.render.Render2DEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.RenderCrosshairEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Crosshair", category = ModuleCategory.RENDER)
public final class Crosshair extends Module
{
    private final DoubleProperty gapProperty;
    private final DoubleProperty lengthProperty;
    private final DoubleProperty widthProperty;
    private final Property<Boolean> tShapeProperty;
    private final Property<Boolean> dotProperty;
    private final Property<Boolean> dynamicProperty;
    private final Property<Boolean> outlineProperty;
    private final DoubleProperty outlineWidthProperty;
    private final Property<Integer> colorProperty;
    @EventLink
    public final Listener<RenderCrosshairEvent> onRenderCrosshairEvent;
    @EventLink
    @Priority(0)
    public final Listener<Render2DEvent> onRender2D;
    
    public Crosshair() {
        this.gapProperty = new DoubleProperty("Gap", 1.0, 0.0, 10.0, 0.5);
        this.lengthProperty = new DoubleProperty("Length", 3.0, 0.0, 10.0, 0.5);
        this.widthProperty = new DoubleProperty("Width", 1.0, 0.0, 5.0, 0.5);
        this.tShapeProperty = new Property<Boolean>("T Shape", false);
        this.dotProperty = new Property<Boolean>("Dot", false);
        this.dynamicProperty = new Property<Boolean>("Dynamic", true);
        this.outlineProperty = new Property<Boolean>("Outline", true);
        this.outlineWidthProperty = new DoubleProperty("Outline Width", 0.5, this.outlineProperty::getValue, 0.5, 5.0, 0.5);
        this.colorProperty = new Property<Integer>("Color", Colors.BLUE);
        this.onRenderCrosshairEvent = CancellableEvent::setCancelled;
        final double width;
        final double halfWidth;
        double gap;
        final double length;
        final int color;
        final double outlineWidth;
        final boolean outline;
        final boolean tShape;
        final LockedResolution lr;
        final double middleX;
        final double middleY;
        this.onRender2D = (event -> {
            width = this.widthProperty.getValue();
            halfWidth = width / 2.0;
            gap = this.gapProperty.getValue();
            if (this.dynamicProperty.getValue()) {
                gap *= Math.max(Wrapper.getPlayer().isSneaking() ? 0.5 : 1.0, RenderingUtils.interpolate(PlayerInfoCache.getPrevLastDist(), PlayerInfoCache.getLastDist(), event.getPartialTicks()) * 10.0);
            }
            length = this.lengthProperty.getValue();
            color = this.colorProperty.getValue();
            outlineWidth = this.outlineWidthProperty.getValue();
            outline = this.outlineProperty.getValue();
            tShape = this.tShapeProperty.getValue();
            lr = event.getResolution();
            middleX = lr.getWidth() / 2.0;
            middleY = lr.getHeight() / 2.0;
            if (outline) {
                Gui.drawRect(middleX - gap - length - outlineWidth, middleY - halfWidth - outlineWidth, middleX - gap + outlineWidth, middleY + halfWidth + outlineWidth, -1778384896);
                Gui.drawRect(middleX + gap - outlineWidth, middleY - halfWidth - outlineWidth, middleX + gap + length + outlineWidth, middleY + halfWidth + outlineWidth, -1778384896);
                Gui.drawRect(middleX - halfWidth - outlineWidth, middleY + gap - outlineWidth, middleX + halfWidth + outlineWidth, middleY + gap + length + outlineWidth, -1778384896);
                if (!tShape) {
                    Gui.drawRect(middleX - halfWidth - outlineWidth, middleY - gap - length - outlineWidth, middleX + halfWidth + outlineWidth, middleY - gap + outlineWidth, -1778384896);
                }
            }
            Gui.drawRect(middleX - gap - length, middleY - halfWidth, middleX - gap, middleY + halfWidth, color);
            Gui.drawRect(middleX + gap, middleY - halfWidth, middleX + gap + length, middleY + halfWidth, color);
            Gui.drawRect(middleX - halfWidth, middleY + gap, middleX + halfWidth, middleY + gap + length, color);
            if (!tShape) {
                Gui.drawRect(middleX - halfWidth, middleY - gap - length, middleX + halfWidth, middleY - gap, color);
            }
        });
    }
}
