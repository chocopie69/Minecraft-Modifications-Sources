// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.utils.render.LockedResolution;
import net.minecraft.network.Packet;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.render.Render2DEvent;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.sound.EntityHitSoundEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Hitmarkers", category = ModuleCategory.RENDER)
public final class Hitmarkers extends Module
{
    private final Property<Integer> hitColorProperty;
    private final Property<Integer> killColorProperty;
    private final DoubleProperty xOffsetProperty;
    private final DoubleProperty lengthProperty;
    private final DoubleProperty hitMarkerThicknessProperty;
    private final Property<Boolean> soundsProperty;
    private final TimerUtil attackTimeOut;
    private final TimerUtil killTimeOut;
    private int color;
    private double progress;
    private int lastAttackedEntity;
    private int toBeKilledEntity;
    @EventLink
    private final Listener<EntityHitSoundEvent> onHitSound;
    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive;
    @EventLink
    private final Listener<PacketSendEvent> onPacketSend;
    @EventLink
    @Priority(0)
    private final Listener<Render2DEvent> onRender2D;
    
    public Hitmarkers() {
        this.hitColorProperty = new Property<Integer>("Hit Color", -1);
        this.killColorProperty = new Property<Integer>("Kill Color", -65536);
        this.xOffsetProperty = new DoubleProperty("X Offset", 2.0, 0.5, 10.0, 0.5);
        this.lengthProperty = new DoubleProperty("Length", 4.0, 0.5, 10.0, 0.5);
        this.hitMarkerThicknessProperty = new DoubleProperty("Thickness", 1.0, 0.5, 3.0, 0.5);
        this.soundsProperty = new Property<Boolean>("Sounds", true);
        this.attackTimeOut = new TimerUtil();
        this.killTimeOut = new TimerUtil();
        this.onHitSound = (event -> event.setCancelled(!this.soundsProperty.getValue()));
        final Packet<?> packet;
        S19PacketEntityStatus packetEntityStatus;
        int entityId;
        this.onPacketReceive = (event -> {
            packet = event.getPacket();
            if (packet instanceof S19PacketEntityStatus) {
                packetEntityStatus = (S19PacketEntityStatus)packet;
                entityId = packetEntityStatus.getEntityId();
                if (entityId == this.lastAttackedEntity || (!this.killTimeOut.hasElapsed(50L) && entityId == this.toBeKilledEntity)) {
                    switch (packetEntityStatus.getOpCode()) {
                        case 2: {
                            this.color = this.hitColorProperty.getValue();
                            this.progress = 1.0;
                            this.killTimeOut.reset();
                            this.toBeKilledEntity = this.lastAttackedEntity;
                            break;
                        }
                        case 3: {
                            this.color = this.killColorProperty.getValue();
                            this.progress = 1.0;
                            this.toBeKilledEntity = -1;
                            break;
                        }
                    }
                    this.lastAttackedEntity = -1;
                }
            }
            return;
        });
        final Packet<?> packet2;
        C02PacketUseEntity packetUseEntity;
        this.onPacketSend = (event -> {
            packet2 = event.getPacket();
            if (packet2 instanceof C02PacketUseEntity) {
                packetUseEntity = (C02PacketUseEntity)packet2;
                if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    this.lastAttackedEntity = packetUseEntity.getEntityId();
                    this.attackTimeOut.reset();
                }
            }
            else if (packet2 instanceof C03PacketPlayer && this.lastAttackedEntity != -1 && this.attackTimeOut.hasElapsed(500L)) {
                this.lastAttackedEntity = -1;
            }
            return;
        });
        LockedResolution resolution;
        double xMiddle;
        double yMiddle;
        int i;
        this.onRender2D = (event -> {
            if (this.progress > 0.0) {
                this.progress = RenderingUtils.linearAnimation(this.progress, 0.0, 0.02);
                resolution = event.getResolution();
                xMiddle = resolution.getWidth() / 2.0;
                yMiddle = resolution.getHeight() / 2.0;
                GL11.glPushMatrix();
                OGLUtils.enableBlending();
                GL11.glDisable(3553);
                GL11.glHint(3155, 4354);
                GL11.glEnable(2881);
                GL11.glTranslated(xMiddle, yMiddle, 0.0);
                GL11.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
                OGLUtils.color(RenderingUtils.fadeTo(removeAlphaComponent(this.color), this.color, (float)this.progress));
                for (i = 0; i < 4; ++i) {
                    drawHitMarker(this.xOffsetProperty.getValue(), this.lengthProperty.getValue(), this.hitMarkerThicknessProperty.getValue());
                    if (i != 3) {
                        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                    }
                }
                GL11.glDisable(2881);
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        });
    }
    
    private static int removeAlphaComponent(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    private static void drawHitMarker(final double xOffset, final double length, final double width) {
        final double halfWidth = width * 0.5;
        GL11.glBegin(7);
        GL11.glVertex2d(-(xOffset + length), -halfWidth);
        GL11.glVertex2d(-(xOffset + length), halfWidth);
        GL11.glVertex2d(-xOffset, halfWidth);
        GL11.glVertex2d(-xOffset, -halfWidth);
        GL11.glEnd();
    }
}
