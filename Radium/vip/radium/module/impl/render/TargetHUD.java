// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.LockedResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import vip.radium.utils.render.RenderingUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.utils.Wrapper;
import vip.radium.module.impl.combat.KillAura;
import java.util.HashMap;
import vip.radium.event.impl.render.Render2DEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.entity.DamageEntityEvent;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Target HUD", category = ModuleCategory.RENDER)
public final class TargetHUD extends Module
{
    private final EnumProperty<ColorMode> colorModeProperty;
    private final Property<Boolean> pulsingProperty;
    private final Property<Integer> colorProperty;
    private final Property<Integer> secondColorProperty;
    private final Map<EntityLivingBase, Double> entityDamageMap;
    @EventLink
    public final Listener<DamageEntityEvent> onDamageEntityEvent;
    @EventLink
    public final Listener<Render2DEvent> onRender2DEvent;
    
    public TargetHUD() {
        this.colorModeProperty = new EnumProperty<ColorMode>("Color Mode", ColorMode.COLOR);
        this.pulsingProperty = new Property<Boolean>("Pulsing", true, () -> this.colorModeProperty.getValue() != ColorMode.BLEND);
        this.colorProperty = new Property<Integer>("Color", -1, () -> this.colorModeProperty.getValue() != ColorMode.HEALTH);
        this.secondColorProperty = new Property<Integer>("Second Color", -1, () -> this.colorModeProperty.getValue() == ColorMode.BLEND);
        this.entityDamageMap = new HashMap<EntityLivingBase, Double>();
        this.onDamageEntityEvent = (event -> this.entityDamageMap.put(event.getEntity(), event.getDamage()));
        final EntityLivingBase target;
        final Object o;
        LockedResolution lr;
        FontRenderer fontRenderer;
        float sWidth;
        float sHeight;
        float middleX;
        float middleY;
        float top;
        float xOffset;
        String name;
        float modelWidth;
        float nameYOffset;
        float nameHeight;
        float width;
        float height;
        float half;
        float left;
        float right;
        float bottom;
        float textLeft;
        float healthTextY;
        float health;
        String healthText;
        float scale;
        float healthTextHeight;
        float healthPercentage;
        int fadeColor = 0;
        float downScale;
        float healthBarY;
        float healthBarHeight;
        float healthBarRight;
        float dif;
        Double lastDamage;
        float healthWidth;
        float healthBarEnd;
        float damage;
        float damageWidth;
        this.onRender2DEvent = (event -> {
            target = KillAura.getInstance().getTarget();
            if (o != null) {
                lr = event.getResolution();
                fontRenderer = Wrapper.getMinecraftFontRenderer();
                sWidth = (float)lr.getWidth();
                sHeight = (float)lr.getHeight();
                middleX = sWidth / 2.0f;
                middleY = sHeight / 2.0f;
                top = middleY + 20.0f;
                xOffset = 0.0f;
                if (target instanceof EntityPlayer) {
                    name = ((EntityPlayer)target).getGameProfile().getName();
                }
                else {
                    name = target.getDisplayName().getUnformattedText();
                }
                modelWidth = 30.0f;
                nameYOffset = 4.0f;
                nameHeight = fontRenderer.getHeight(name);
                width = Math.max(120.0f, modelWidth + 4.0f + fontRenderer.getWidth(name) + 2.0f);
                height = 50.0f;
                half = width / 2.0f;
                left = middleX - half + xOffset;
                right = middleX + half + xOffset;
                bottom = top + height;
                Gui.drawRect(left, top, right, bottom, Integer.MIN_VALUE);
                GL11.glDisable(3553);
                GL11.glLineWidth(0.5f);
                GL11.glColor3f(0.0f, 0.0f, 0.0f);
                GL11.glBegin(2);
                GL11.glVertex2f(left, top);
                GL11.glVertex2f(left, bottom);
                GL11.glVertex2f(right, bottom);
                GL11.glVertex2f(right, top);
                GL11.glEnd();
                GL11.glEnable(3553);
                textLeft = left + modelWidth;
                fontRenderer.drawStringWithShadow(name, textLeft, top + nameYOffset, -1);
                healthTextY = top + nameHeight + nameYOffset;
                health = target.getHealth();
                healthText = String.format("%.1f", health);
                scale = 2.0f;
                healthTextHeight = fontRenderer.getHeight(healthText) * scale;
                healthPercentage = health / target.getMaxHealth();
                switch (this.colorModeProperty.getValue()) {
                    case COLOR: {
                        fadeColor = this.colorProperty.getValue();
                        break;
                    }
                    case BLEND: {
                        fadeColor = RenderingUtils.fadeBetween(this.colorProperty.getValue(), this.secondColorProperty.getValue(), System.currentTimeMillis() % 3000L / 1500.0f);
                        break;
                    }
                    default: {
                        fadeColor = RenderingUtils.getColorFromPercentage(healthPercentage);
                        break;
                    }
                }
                if (this.pulsingProperty.isAvailable() && this.pulsingProperty.getValue()) {
                    fadeColor = RenderingUtils.fadeBetween(fadeColor, RenderingUtils.darker(fadeColor, 0.49f), System.currentTimeMillis() % 3000L / 1500.0f);
                }
                downScale = 1.0f / scale;
                GL11.glScalef(scale, scale, 1.0f);
                fontRenderer.drawStringWithShadow(healthText, textLeft / scale, healthTextY / scale + 2.0f, fadeColor);
                GL11.glScalef(downScale, downScale, 1.0f);
                healthBarY = healthTextY + healthTextHeight + 2.0f;
                healthBarHeight = 8.0f;
                healthBarRight = right - 4.0f;
                dif = healthBarRight - textLeft;
                Gui.drawRect(textLeft, healthBarY, healthBarRight, healthBarY + healthBarHeight, 1342177280);
                target.healthProgressX = (float)RenderingUtils.progressiveAnimation(target.healthProgressX, healthPercentage, 1.0);
                lastDamage = this.entityDamageMap.get(target);
                healthWidth = dif * target.healthProgressX;
                healthBarEnd = textLeft + healthWidth;
                if (lastDamage != null && lastDamage > 0.0) {
                    damage = lastDamage.floatValue();
                    damageWidth = dif * (damage / target.getMaxHealth());
                    Gui.drawRect(healthBarEnd, healthBarY, Math.min(healthBarEnd + damageWidth, healthBarRight), healthBarY + healthBarHeight, RenderingUtils.darker(fadeColor, 0.49f));
                }
                Gui.drawRect(textLeft, healthBarY, healthBarEnd, healthBarY + healthBarHeight, fadeColor);
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                GuiInventory.drawEntityOnScreen((int)(left + modelWidth / 2.0f), (int)bottom - 2, 23, 0.0f, 0.0f, target);
            }
        });
    }
    
    private enum ColorMode
    {
        HEALTH("HEALTH", 0), 
        COLOR("COLOR", 1), 
        BLEND("BLEND", 2);
        
        private ColorMode(final String name, final int ordinal) {
        }
    }
}
