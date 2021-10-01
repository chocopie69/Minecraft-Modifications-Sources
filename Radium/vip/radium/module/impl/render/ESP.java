// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import vip.radium.module.ModuleManager;
import net.minecraft.client.model.ModelPlayer;
import vip.radium.gui.font.FontRenderer;
import java.util.Iterator;
import vip.radium.utils.render.LockedResolution;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.gui.Gui;
import vip.radium.RadiumClient;
import vip.radium.utils.HypixelGameUtils;
import vip.radium.utils.PlayerUtils;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.RotationUtils;
import net.minecraft.entity.Entity;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.HashMap;
import java.awt.Color;
import vip.radium.utils.render.Colors;
import vip.radium.event.impl.render.Render3DEvent;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.render.Render2DEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.RenderNameTagEvent;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "ESP", category = ModuleCategory.RENDER)
public final class ESP extends Module
{
    private final Property<Boolean> arrowsProperty;
    private final Property<Integer> arrowsColorProperty;
    private final DoubleProperty arrowsRadiusProperty;
    private final DoubleProperty arrowsSizeProperty;
    private final EnumProperty<ArrowsShape> arrowShapeProperty;
    private final Property<Boolean> localPlayerProperty;
    private final Property<Boolean> tagsProperty;
    private final EnumProperty<TagsFont> tagsFontProperty;
    private final Property<Boolean> tagsBackgroundProperty;
    private final Property<Boolean> tagsHealthProperty;
    private final Property<Boolean> tagsHealthBarProperty;
    private final Property<Boolean> esp2dProperty;
    private final Property<Boolean> boxProperty;
    private final EnumProperty<BoxColor> boxColorModeProperty;
    private final Property<Integer> boxColorProperty;
    private final Property<Integer> firstColorProperty;
    private final Property<Integer> secondColorProperty;
    private final Property<Integer> thirdColorProperty;
    private final Property<Integer> forthColorProperty;
    private final Property<Boolean> healthBarProperty;
    private final Property<Boolean> healthBarLinesProperty;
    private final Property<Boolean> armorBarProperty;
    private final Property<Integer> armorBarColorProperty;
    private final EnumProperty<HealthBarColor> healthBarColorModeProperty;
    private final Property<Integer> healthBarColorProperty;
    private final Property<Integer> healthBarStartColorProperty;
    private final Property<Integer> healthBarEndColorProperty;
    private final Property<Boolean> skeletonsProperty;
    private final DoubleProperty skeletonWidthProperty;
    private final Property<Integer> skeletonsColorProperty;
    private final Map<EntityPlayer, float[][]> playerRotationMap;
    private final Map<EntityPlayer, float[]> entityPosMap;
    @EventLink
    public final Listener<RenderNameTagEvent> onRenderNameTagEvent;
    @EventLink
    @Priority(4)
    public final Listener<Render2DEvent> onRender2DEvent;
    @EventLink
    public final Listener<Render3DEvent> onRender3DEvent;
    
    public ESP() {
        this.arrowsProperty = new Property<Boolean>("Arrows", true);
        this.arrowsColorProperty = new Property<Integer>("Arrow Color", Colors.DEEP_PURPLE, this.arrowsProperty::getValue);
        this.arrowsRadiusProperty = new DoubleProperty("Arrow Radius", 30.0, this.arrowsProperty::getValue, 10.0, 100.0, 1.0);
        this.arrowsSizeProperty = new DoubleProperty("Arrow Size", 6.0, this.arrowsProperty::getValue, 3.0, 30.0, 1.0);
        this.arrowShapeProperty = new EnumProperty<ArrowsShape>("Arrow Shape", ArrowsShape.ARROW, this.arrowsProperty::getValue);
        this.localPlayerProperty = new Property<Boolean>("Local Player", true);
        this.tagsProperty = new Property<Boolean>("Tags", true);
        this.tagsFontProperty = new EnumProperty<TagsFont>("Tags Font", TagsFont.SMOOTH, this.tagsProperty::getValue);
        this.tagsBackgroundProperty = new Property<Boolean>("Tag Backdrop", true);
        this.tagsHealthProperty = new Property<Boolean>("Tags HP", true, this.tagsProperty::getValue);
        this.tagsHealthBarProperty = new Property<Boolean>("Tags HP Bar", true, this.tagsProperty::getValue);
        this.esp2dProperty = new Property<Boolean>("2D ESP", true);
        this.boxProperty = new Property<Boolean>("Box", true, this.esp2dProperty::getValue);
        this.boxColorModeProperty = new EnumProperty<BoxColor>("Box Color Mode", BoxColor.GRADIENT, () -> this.boxProperty.isAvailable() && this.boxProperty.getValue());
        this.boxColorProperty = new Property<Integer>("Box Color", Colors.LIGHT_BLUE, () -> this.boxColorModeProperty.isAvailable() && this.boxColorModeProperty.getValue() == BoxColor.SOLID);
        this.firstColorProperty = new Property<Integer>("First Color", Colors.LIGHT_BLUE, this::isGradientBoxAvailable);
        this.secondColorProperty = new Property<Integer>("Second Color", Colors.PINK, this::isGradientBoxAvailable);
        this.thirdColorProperty = new Property<Integer>("Third Color", Colors.PURPLE, this::isGradientBoxAvailable);
        this.forthColorProperty = new Property<Integer>("Forth Color", -256, this::isGradientBoxAvailable);
        this.healthBarProperty = new Property<Boolean>("HP Bar", true, this.esp2dProperty::getValue);
        this.healthBarLinesProperty = new Property<Boolean>("HP Bar Lines", true, () -> this.healthBarProperty.isAvailable() && this.healthBarProperty.getValue());
        this.armorBarProperty = new Property<Boolean>("Armor Bar", true, this.esp2dProperty::getValue);
        this.armorBarColorProperty = new Property<Integer>("Armor Bar Color", new Color(0, 255, 255).getRGB(), () -> this.armorBarProperty.isAvailable() && this.armorBarProperty.getValue());
        this.healthBarColorModeProperty = new EnumProperty<HealthBarColor>("HP Color Mode", HealthBarColor.SOLID, () -> this.healthBarProperty.isAvailable() && this.healthBarProperty.getValue());
        this.healthBarColorProperty = new Property<Integer>("HP Bar Color", new Color(0, 255, 89).getRGB(), () -> this.healthBarColorModeProperty.isAvailable() && this.healthBarColorModeProperty.getValue() == HealthBarColor.SOLID);
        this.healthBarStartColorProperty = new Property<Integer>("HP Bar S-Color", Colors.WHITE, () -> this.healthBarColorModeProperty.isAvailable() && this.healthBarColorModeProperty.getValue() == HealthBarColor.GRADIENT);
        this.healthBarEndColorProperty = new Property<Integer>("HP Bar E-Color", Colors.LIGHT_BLUE, () -> this.healthBarColorModeProperty.isAvailable() && this.healthBarColorModeProperty.getValue() == HealthBarColor.GRADIENT);
        this.skeletonsProperty = new Property<Boolean>("Skeletons", true);
        this.skeletonWidthProperty = new DoubleProperty("Skeleton Width", 0.5, this.skeletonsProperty::getValue, 0.5, 5.0, 0.5);
        this.skeletonsColorProperty = new Property<Integer>("Skeletons Color", Colors.LIGHT_BLUE, this.skeletonsProperty::getValue);
        this.playerRotationMap = new HashMap<EntityPlayer, float[][]>();
        this.entityPosMap = new HashMap<EntityPlayer, float[]>();
        this.onRenderNameTagEvent = (event -> {
            if (this.tagsProperty.getValue() && this.entityPosMap.containsKey(event.getEntityLivingBase())) {
                event.setCancelled();
            }
            return;
        });
        final LockedResolution lr;
        final float middleX;
        final float middleY;
        final float pt;
        final Iterator<EntityPlayer> iterator;
        EntityPlayer player;
        double arrowSize;
        float alpha;
        int color;
        float yaw;
        double offset;
        double offset2;
        boolean tags;
        boolean esp2d;
        float[] positions;
        float x;
        float y;
        float x2;
        float y2;
        float health;
        float maxHealth;
        float healthPercentage;
        FontRenderer fontRenderer;
        String name;
        float halfWidth;
        float xDif;
        float middle;
        float textHeight;
        float renderY;
        boolean teamMate;
        boolean friend;
        final StringBuilder sb;
        String text;
        boolean tagsHealthBar;
        float left;
        float right;
        int healthColor;
        String healthString;
        float healthWidth;
        float healthBoxLeft;
        double armorPercentage;
        double armorBarWidth;
        double healthBarLeft;
        double healthBarRight;
        double healthBarLeft2;
        double healthBarRight2;
        double heightDif;
        double healthBarHeight;
        boolean needScissor;
        int lineCount;
        double yDif;
        double inc;
        double linePos;
        int i;
        int boxColor;
        this.onRender2DEvent = (e -> {
            lr = e.getResolution();
            middleX = lr.getWidth() / 2.0f;
            middleY = lr.getHeight() / 2.0f;
            pt = e.getPartialTicks();
            this.entityPosMap.keySet().iterator();
            while (iterator.hasNext()) {
                player = iterator.next();
                if (this.arrowsProperty.getValue() && player instanceof EntityOtherPlayerMP) {
                    GL11.glPushMatrix();
                    OGLUtils.enableBlending();
                    arrowSize = this.arrowsSizeProperty.getValue();
                    alpha = Math.max(1.0f - Wrapper.getPlayer().getDistanceToEntity(player) / 40.0f, 0.2f);
                    color = this.arrowsColorProperty.getValue();
                    GL11.glTranslated(middleX + 0.5, (double)middleY, 1.0);
                    yaw = RenderingUtils.interpolate(RotationUtils.getOldYaw(player), RotationUtils.getYawToEntity(player), pt) - RenderingUtils.interpolate(Wrapper.getPlayer().prevRotationYaw, Wrapper.getPlayer().rotationYaw, pt);
                    GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glEnable(2881);
                    GL11.glHint(3155, 4354);
                    GL11.glTranslated(0.0, -this.arrowsRadiusProperty.getValue() - this.arrowsSizeProperty.getValue(), 0.0);
                    GL11.glDisable(3553);
                    GL11.glBegin((this.arrowShapeProperty.getValue() == ArrowsShape.ARROW) ? 9 : 5);
                    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(alpha * 255.0f));
                    GL11.glVertex2d(0.0, 0.0);
                    switch (this.arrowShapeProperty.getValue()) {
                        case ARROW: {
                            offset = (int)(arrowSize / 3.0);
                            GL11.glVertex2d(-arrowSize + offset, arrowSize);
                            GL11.glVertex2d(0.0, arrowSize - offset);
                            GL11.glVertex2d(arrowSize - offset, arrowSize);
                            break;
                        }
                        case EQUILATERAL: {
                            offset2 = (int)(arrowSize / 3.0);
                            GL11.glVertex2d(-arrowSize + offset2, arrowSize);
                            GL11.glVertex2d(arrowSize - offset2, arrowSize);
                            break;
                        }
                        case ISOSCELES: {
                            GL11.glVertex2d(-arrowSize, arrowSize);
                            GL11.glVertex2d(arrowSize, arrowSize);
                            break;
                        }
                    }
                    GL11.glEnd();
                    GL11.glEnable(3553);
                    GL11.glDisable(2881);
                    GL11.glDisable(3042);
                    GL11.glPopMatrix();
                }
                tags = this.tagsProperty.getValue();
                esp2d = this.esp2dProperty.getValue();
                if ((esp2d || tags) && (player.getDistanceToEntity(Wrapper.getPlayer()) >= 1.0f || Wrapper.isInThirdPerson())) {
                    if (!RenderingUtils.isBBInFrustum(player.getEntityBoundingBox())) {
                        continue;
                    }
                    else {
                        GL11.glPushMatrix();
                        positions = this.entityPosMap.get(player);
                        x = positions[0];
                        y = positions[1];
                        x2 = positions[2];
                        y2 = positions[3];
                        health = player.getHealth();
                        maxHealth = player.getMaxHealth();
                        healthPercentage = health / maxHealth;
                        if (tags) {
                            fontRenderer = this.tagsFontProperty.getValue().fontRenderer;
                            name = player.getGameProfile().getName();
                            halfWidth = fontRenderer.getWidth(name) / 2.0f;
                            xDif = x2 - x;
                            middle = x + xDif / 2.0f;
                            textHeight = fontRenderer.getHeight(name);
                            renderY = y - textHeight - 2.0f;
                            teamMate = (PlayerUtils.isTeamMate(player) && HypixelGameUtils.getGameMode() != HypixelGameUtils.GameMode.PIT);
                            friend = RadiumClient.getInstance().getFriendManager().isFriend(player);
                            new StringBuilder(String.valueOf(friend ? "§B" : (teamMate ? "§A" : "§C")));
                            text = sb.append(name).toString();
                            tagsHealthBar = this.tagsHealthBarProperty.getValue();
                            left = middle - halfWidth - 1.0f;
                            right = middle + halfWidth + 1.0f;
                            if (tagsHealthBar) {
                                renderY -= 2.5f;
                            }
                            if (this.tagsBackgroundProperty.getValue()) {
                                Gui.drawRect(left, renderY - 1.0f, right, renderY + textHeight + 1.0f, 2013265920);
                            }
                            fontRenderer.drawStringWithShadow(text, middle - halfWidth, renderY + 0.5f, -1);
                            if (tagsHealthBar) {
                                healthColor = RenderingUtils.getColorFromPercentage(healthPercentage);
                                Gui.drawRect(left, renderY + textHeight + 1.0f, right, renderY + textHeight + 2.0f, RenderingUtils.darker(healthColor, 0.49f));
                                Gui.drawRect(left, renderY + textHeight + 1.0f, left + (right - left) * healthPercentage, renderY + textHeight + 2.0f, healthColor);
                            }
                            if (this.tagsHealthProperty.getValue()) {
                                healthString = String.valueOf((int)health);
                                healthWidth = fontRenderer.getWidth(healthString);
                                healthBoxLeft = right + 1.0f;
                                if (this.tagsBackgroundProperty.getValue()) {
                                    Gui.drawRect(healthBoxLeft, renderY - 1.0f, healthBoxLeft + healthWidth + 2.0f, renderY + textHeight + 1.0f, 2013265920);
                                }
                                fontRenderer.drawStringWithShadow(healthString, healthBoxLeft + 1.0f, renderY + 0.5f, RenderingUtils.getColorFromPercentage(health / player.getMaxHealth()));
                            }
                        }
                        if (esp2d) {
                            if (this.armorBarProperty.getValue()) {
                                armorPercentage = player.getTotalArmorValue() / 20.0;
                                armorBarWidth = (x2 - x) * armorPercentage;
                                Gui.drawRect(x, y2, x2, y2 + 2.0f, -1778384896);
                                if (armorPercentage > 0.0) {
                                    Gui.drawRect(x + 0.5, y2 + 0.5, x + armorBarWidth - 0.5, y2 + 1.5, this.armorBarColorProperty.getValue());
                                }
                            }
                            if (this.healthBarProperty.getValue()) {
                                healthBarLeft = x - 2.0;
                                healthBarRight = x;
                                Gui.drawRect(healthBarLeft, y, healthBarRight, y2, -1778384896);
                                healthBarLeft2 = healthBarLeft + 0.5;
                                healthBarRight2 = healthBarRight - 0.5;
                                heightDif = y - y2;
                                healthBarHeight = heightDif * healthPercentage;
                                switch (this.healthBarColorModeProperty.getValue()) {
                                    case SOLID: {
                                        Gui.drawRect(healthBarLeft2, y2 + 0.5 + healthBarHeight, healthBarRight2, y2 - 0.5, this.healthBarColorProperty.getValue());
                                        break;
                                    }
                                    case HEALTH: {
                                        Gui.drawRect(healthBarLeft2, y2 + 0.5 + healthBarHeight, healthBarRight2, y2 - 0.5, RenderingUtils.getColorFromPercentage(healthPercentage));
                                        break;
                                    }
                                    case GRADIENT: {
                                        needScissor = (health < maxHealth);
                                        if (needScissor) {
                                            GL11.glEnable(3089);
                                            OGLUtils.startScissorBox(lr, (int)x - 3, (int)(y2 + healthBarHeight), 2, (int)(-healthBarHeight) + 1);
                                        }
                                        RenderingUtils.drawGradientRect(healthBarLeft2, y + 0.5, healthBarRight2, y2 - 0.5, false, this.healthBarStartColorProperty.getValue(), this.healthBarEndColorProperty.getValue());
                                        if (needScissor) {
                                            GL11.glDisable(3089);
                                            break;
                                        }
                                        else {
                                            break;
                                        }
                                        break;
                                    }
                                }
                                lineCount = 10;
                                yDif = y2 - y;
                                inc = yDif / 10.0;
                                if (this.healthBarLinesProperty.getValue() && inc >= 3.0) {
                                    linePos = y + inc;
                                    for (i = 0; i < 9; ++i) {
                                        Gui.drawRect(healthBarLeft2, linePos, healthBarRight2, linePos + 0.5, -16777216);
                                        linePos += inc;
                                    }
                                }
                            }
                            if (this.boxProperty.getValue()) {
                                boxColor = this.boxColorProperty.getValue();
                                Gui.drawRect(x, y, x + 1.5f, y2, -1778384896);
                                Gui.drawRect(x2 - 1.5f, y, x2, y2, -1778384896);
                                Gui.drawRect(x, y, x2, y + 1.5f, -1778384896);
                                Gui.drawRect(x, y2 - 1.5f, x2, y2, -1778384896);
                                Gui.drawRect(x + 0.5f, y + 0.5f, x + 1.0f, y2 - 0.5f, boxColor);
                                Gui.drawRect(x2 - 1.0f, y + 0.5f, x2 - 0.5f, y2 - 0.5f, boxColor);
                                Gui.drawRect(x + 0.5f, y + 0.5f, x2 - 0.5f, y + 1.0f, boxColor);
                                Gui.drawRect(x + 0.5f, y2 - 1.0f, x2 - 0.5f, y2 - 0.5f, boxColor);
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
            return;
        });
        float partialTicks;
        final Iterator<EntityPlayer> iterator2;
        EntityPlayer player2;
        double posX;
        double posY;
        double posZ;
        double halfWidth2;
        final AxisAlignedBB axisAlignedBB;
        AxisAlignedBB bb;
        double[][] vectors;
        float[] position;
        final Object o;
        int length;
        int j = 0;
        final double[][] array;
        double[] vec;
        float[] projection;
        float pX;
        float pY;
        final Iterator<EntityPlayer> iterator3;
        EntityPlayer player3;
        this.onRender3DEvent = (e -> {
            if (!this.entityPosMap.isEmpty()) {
                this.entityPosMap.clear();
            }
            if (this.esp2dProperty.getValue() || this.arrowsProperty.getValue() || this.tagsProperty.getValue()) {
                partialTicks = e.getPartialTicks();
                Wrapper.getLoadedPlayers().iterator();
                while (iterator2.hasNext()) {
                    player2 = iterator2.next();
                    if ((player2 instanceof EntityOtherPlayerMP || (getInstance().localPlayerProperty.getValue() && Wrapper.isInThirdPerson())) && player2.isEntityAlive()) {
                        if (player2.isInvisible()) {
                            continue;
                        }
                        else {
                            GL11.glPushMatrix();
                            posX = RenderingUtils.interpolate(player2.prevPosX, player2.posX, partialTicks) - RenderManager.viewerPosX;
                            posY = RenderingUtils.interpolate(player2.prevPosY, player2.posY, partialTicks) - RenderManager.viewerPosY;
                            posZ = RenderingUtils.interpolate(player2.prevPosZ, player2.posZ, partialTicks) - RenderManager.viewerPosZ;
                            halfWidth2 = player2.width / 2.0;
                            new AxisAlignedBB(posX - halfWidth2, posY, posZ - halfWidth2, posX + halfWidth2, posY + player2.height + (player2.isSneaking() ? -0.2 : 0.1), posZ + halfWidth2);
                            bb = axisAlignedBB;
                            vectors = new double[][] { { bb.minX, bb.minY, bb.minZ }, { bb.minX, bb.maxY, bb.minZ }, { bb.minX, bb.maxY, bb.maxZ }, { bb.minX, bb.minY, bb.maxZ }, { bb.maxX, bb.minY, bb.minZ }, { bb.maxX, bb.maxY, bb.minZ }, { bb.maxX, bb.maxY, bb.maxZ }, { bb.maxX, bb.minY, bb.maxZ } };
                            position = new float[] { Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f };
                            for (length = o.length; j < length; ++j) {
                                vec = array[j];
                                projection = OGLUtils.project2D((float)vec[0], (float)vec[1], (float)vec[2], 2);
                                if (projection != null && projection[2] >= 0.0f && projection[2] < 1.0f) {
                                    pX = projection[0];
                                    pY = projection[1];
                                    position[0] = Math.min(position[0], pX);
                                    position[1] = Math.min(position[1], pY);
                                    position[2] = Math.max(position[2], pX);
                                    position[3] = Math.max(position[3], pY);
                                }
                            }
                            this.entityPosMap.put(player2, position);
                            GL11.glPopMatrix();
                        }
                    }
                }
            }
            if (this.skeletonsProperty.getValue()) {
                GL11.glPushMatrix();
                GL11.glLineWidth(this.skeletonWidthProperty.getValue().floatValue());
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glBlendFunc(770, 771);
                OGLUtils.color(this.skeletonsColorProperty.getValue());
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glDepthMask(false);
                this.playerRotationMap.keySet().iterator();
                while (iterator3.hasNext()) {
                    player3 = iterator3.next();
                    this.drawSkeleton(e, player3);
                }
                this.playerRotationMap.clear();
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glPopMatrix();
            }
            return;
        });
        this.toggle();
    }
    
    public static boolean shouldDrawOutlineESP() {
        return false;
    }
    
    public static boolean shouldDrawSkeletons() {
        return getInstance().isEnabled() && getInstance().skeletonsProperty.getValue();
    }
    
    public static void addEntity(final EntityPlayer e, final ModelPlayer model) {
        if (e instanceof EntityOtherPlayerMP) {
            getInstance().playerRotationMap.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
        }
    }
    
    public static boolean isValid(final Entity entity) {
        return Wrapper.getLoadedPlayers().contains(entity) && (entity instanceof EntityOtherPlayerMP || (getInstance().localPlayerProperty.getValue() && Wrapper.isInThirdPerson() && entity instanceof EntityPlayer)) && entity.isEntityAlive() && !entity.isInvisible() && RenderingUtils.isBBInFrustum(entity.getEntityBoundingBox());
    }
    
    public static ESP getInstance() {
        return ModuleManager.getInstance(ESP.class);
    }
    
    @Override
    public void onDisable() {
        this.entityPosMap.clear();
        this.playerRotationMap.clear();
    }
    
    private boolean isGradientBoxAvailable() {
        return this.boxProperty.isAvailable() && this.boxProperty.getValue() && this.boxColorModeProperty.getValue() == BoxColor.GRADIENT;
    }
    
    private void drawSkeleton(final Render3DEvent event, final EntityPlayer player) {
        final float[][] entPos;
        if ((entPos = this.playerRotationMap.get(player)) != null) {
            GL11.glPushMatrix();
            final float pt = event.getPartialTicks();
            final float x = (float)(RenderingUtils.interpolate(player.prevPosX, player.posX, pt) - RenderManager.renderPosX);
            final float y = (float)(RenderingUtils.interpolate(player.prevPosY, player.posY, pt) - RenderManager.renderPosY);
            final float z = (float)(RenderingUtils.interpolate(player.prevPosZ, player.posZ, pt) - RenderManager.renderPosZ);
            GL11.glTranslated((double)x, (double)y, (double)z);
            final boolean sneaking = player.isSneaking();
            final float xOff = RenderingUtils.interpolate(player.prevRenderYawOffset, player.renderYawOffset, pt);
            final float yOff = sneaking ? 0.6f : 0.75f;
            GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, 0.0f, sneaking ? -0.235f : 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.125f, yOff, 0.0f);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, -yOff, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.125f, yOff, 0.0f);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, -yOff, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslatef(0.0f, 0.0f, sneaking ? 0.25f : 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, sneaking ? -0.05f : 0.0f, sneaking ? -0.01725f : 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.375f, yOff + 0.55f, 0.0f);
            if (entPos[1][0] != 0.0f) {
                GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[1][1] != 0.0f) {
                GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[1][2] != 0.0f) {
                GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, -0.5f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.375f, yOff + 0.55f, 0.0f);
            if (entPos[2][0] != 0.0f) {
                GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[2][1] != 0.0f) {
                GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, -0.5f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(xOff - player.rotationYawHead, 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, yOff + 0.55f, 0.0f);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, 0.3f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(sneaking ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(0.0f, sneaking ? -0.16175f : 0.0f, sneaking ? -0.48025f : 0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double)yOff, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3f(-0.125f, 0.0f, 0.0f);
            GL11.glVertex3f(0.125f, 0.0f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, yOff, 0.0f);
            GL11.glBegin(3);
            GL11.glVertex3i(0, 0, 0);
            GL11.glVertex3f(0.0f, 0.55f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, yOff + 0.55f, 0.0f);
            GL11.glBegin(3);
            GL11.glVertex3f(-0.375f, 0.0f, 0.0f);
            GL11.glVertex3f(0.375f, 0.0f, 0.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
    
    private enum ArrowsShape
    {
        EQUILATERAL("EQUILATERAL", 0), 
        ARROW("ARROW", 1), 
        ISOSCELES("ISOSCELES", 2);
        
        private ArrowsShape(final String name, final int ordinal) {
        }
    }
    
    private enum BoxColor
    {
        SOLID("SOLID", 0), 
        GRADIENT("GRADIENT", 1), 
        RAINBOW("RAINBOW", 2);
        
        private BoxColor(final String name, final int ordinal) {
        }
    }
    
    private enum HealthBarColor
    {
        SOLID("SOLID", 0), 
        HEALTH("HEALTH", 1), 
        GRADIENT("GRADIENT", 2);
        
        private HealthBarColor(final String name, final int ordinal) {
        }
    }
    
    private enum TagsFont
    {
        MINECRAFT("MINECRAFT", 0, (FontRenderer)Wrapper.getMinecraftFontRenderer()), 
        SMOOTH("SMOOTH", 1, (FontRenderer)Wrapper.getFontRenderer()), 
        CSGO("CSGO", 2, (FontRenderer)Wrapper.getCSGOFontRenderer());
        
        private final FontRenderer fontRenderer;
        
        private TagsFont(final String name, final int ordinal, final FontRenderer fr) {
            this.fontRenderer = fr;
        }
    }
}
