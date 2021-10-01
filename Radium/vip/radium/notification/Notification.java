// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.notification;

import net.minecraft.client.gui.Gui;
import vip.radium.utils.render.OGLUtils;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.render.Translate;

public final class Notification
{
    private final String title;
    private final String body;
    private final Translate translate;
    private final float width;
    private final float height;
    private final long duration;
    private final int color;
    private final TimerUtil timer;
    private boolean dead;
    
    public Notification(final String title, final String body, final long duration, final NotificationType type) {
        this.title = title;
        this.body = body;
        if (body != null) {
            this.width = Math.max(Wrapper.getFontRenderer().getWidth(title), Wrapper.getSmallFontRenderer().getWidth(body)) + 4.0f;
        }
        else {
            this.width = Wrapper.getFontRenderer().getWidth(title) + 4.0f;
        }
        this.height = 27.0f;
        if (Wrapper.getCurrentScreen() == null) {
            final LockedResolution lr = RenderingUtils.getLockedResolution();
            this.translate = new Translate(lr.getWidth(), lr.getHeight() - this.height - 2.0f);
        }
        else {
            final ScaledResolution sr = RenderingUtils.getScaledResolution();
            this.translate = new Translate(sr.getScaledWidth(), sr.getScaledHeight() - this.height - 2.0f);
        }
        this.duration = duration;
        this.color = type.getColor();
        this.timer = new TimerUtil();
    }
    
    public Notification(final String title, final String body, final NotificationType type) {
        this(title, body, (title.length() + body.length()) * 40L, type);
    }
    
    public Notification(final String title, final NotificationType type) {
        this(title, null, title.length() * 40L, type);
    }
    
    public Notification(final String title, final long duration, final NotificationType type) {
        this(title, null, duration, type);
    }
    
    public void render(final LockedResolution lockedResolution, final ScaledResolution scaledResolution, final int index, final int yOffset) {
        int width;
        int height;
        if (lockedResolution != null) {
            width = lockedResolution.getWidth();
            height = lockedResolution.getHeight();
        }
        else {
            width = scaledResolution.getScaledWidth();
            height = scaledResolution.getScaledHeight();
        }
        final float notificationY = height - (this.height + 2.0f) * index - yOffset;
        final float notificationX = width - this.width;
        if (this.timer.hasElapsed(this.duration)) {
            this.translate.animate(width, notificationY);
        }
        else {
            this.translate.animate(notificationX, notificationY);
        }
        final float x = (float)this.translate.getX();
        final float y = (float)this.translate.getY();
        if (x >= width) {
            this.dead = true;
            return;
        }
        GL11.glEnable(3089);
        if (lockedResolution != null) {
            OGLUtils.startScissorBox(lockedResolution, (int)x, (int)y, MathHelper.ceiling_float_int(this.width), (int)this.height);
        }
        else {
            OGLUtils.startScissorBox(scaledResolution, (int)x, (int)y, MathHelper.ceiling_float_int(this.width), (int)this.height);
        }
        Gui.drawRect(notificationX, y, notificationX + this.width, y + this.height, 2013265920);
        final double progress = (System.currentTimeMillis() - this.timer.lastReset()) / (double)this.duration * this.width;
        Gui.drawRect(notificationX, y + this.height - 2.0f, notificationX + this.width, y + this.height, RenderingUtils.darker(this.color, 0.4f));
        Gui.drawRect(notificationX, y + this.height - 2.0f, notificationX + progress, y + this.height, this.color);
        if (this.body != null && this.body.length() > 0) {
            Wrapper.getMediumFontRenderer().drawStringWithShadow(this.title, notificationX + 2.0f, y + 2.0f, -1);
            Wrapper.getSmallFontRenderer().drawStringWithShadow(this.body, notificationX + 2.0f, y + 14.0f, -1);
        }
        else {
            Wrapper.getMediumFontRenderer().drawStringWithShadow(this.title, notificationX + 2.0f, y + 9.0f, -1);
        }
        GL11.glDisable(3089);
    }
    
    public boolean isDead() {
        return this.dead;
    }
}
