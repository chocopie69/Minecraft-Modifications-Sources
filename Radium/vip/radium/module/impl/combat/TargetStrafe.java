// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.combat;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import vip.radium.utils.MovementUtils;
import vip.radium.event.impl.player.MoveEntityEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.utils.Wrapper;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.radium.utils.render.OGLUtils;
import net.minecraft.util.AxisAlignedBB;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.module.ModuleManager;
import java.util.ArrayList;
import vip.radium.event.impl.player.UpdatePositionEvent;
import net.minecraft.entity.EntityLivingBase;
import io.github.nevalackin.homoBus.Priority;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.Render3DEvent;
import io.github.nevalackin.homoBus.Listener;
import java.util.List;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Target Strafe", category = ModuleCategory.COMBAT)
public final class TargetStrafe extends Module
{
    private static final float DOUBLE_PI = 6.2831855f;
    public final DoubleProperty radiusProperty;
    public final Property<Boolean> holdSpaceProperty;
    public final Property<Boolean> behindProperty;
    public final Property<Boolean> adaptiveSpeedProperty;
    private final Property<Boolean> renderProperty;
    private final DoubleProperty pointsProperty;
    private final Property<Integer> activePointColorProperty;
    private final Property<Integer> dormantPointColorProperty;
    private final Property<Integer> invalidPointColorProperty;
    private final List<Point3D> currentPoints;
    private Point3D currentPoint;
    @EventLink
    @Priority(0)
    public final Listener<Render3DEvent> onRender3DEvent;
    private EntityLivingBase currentTarget;
    private int pointIndex;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public TargetStrafe() {
        this.radiusProperty = new DoubleProperty("Radius", 2.0, 0.1, 4.0, 0.1);
        this.holdSpaceProperty = new Property<Boolean>("Hold Space", true);
        this.behindProperty = new Property<Boolean>("Behind", true);
        this.adaptiveSpeedProperty = new Property<Boolean>("Adapt Speed", true);
        this.renderProperty = new Property<Boolean>("Render", true);
        this.pointsProperty = new DoubleProperty("Points", 12.0, 1.0, 90.0, 1.0);
        this.activePointColorProperty = new Property<Integer>("Active Color", -2147418368, this.renderProperty::getValue);
        this.dormantPointColorProperty = new Property<Integer>("Dormant Color", 553648127, this.renderProperty::getValue);
        this.invalidPointColorProperty = new Property<Integer>("Invalid Color", 553582592, this.renderProperty::getValue);
        this.currentPoints = new ArrayList<Point3D>();
        final KillAura killAura;
        float partialTicks;
        final Iterator<Point3D> iterator;
        Point3D point;
        int color;
        double x;
        double y;
        double z;
        double pointSize;
        AxisAlignedBB bb;
        double renderX;
        double renderY;
        double renderZ;
        this.onRender3DEvent = (event -> {
            killAura = ModuleManager.getInstance(KillAura.class);
            if (this.renderProperty.getValue() && killAura.getTarget() != null) {
                partialTicks = event.getPartialTicks();
                this.currentPoints.iterator();
                while (iterator.hasNext()) {
                    point = iterator.next();
                    if (this.currentPoint == point) {
                        color = this.activePointColorProperty.getValue();
                    }
                    else if (point.valid) {
                        color = this.dormantPointColorProperty.getValue();
                    }
                    else {
                        color = this.invalidPointColorProperty.getValue();
                    }
                    x = RenderingUtils.interpolate(point.prevX, point.x, partialTicks);
                    y = RenderingUtils.interpolate(point.prevY, point.y, partialTicks);
                    z = RenderingUtils.interpolate(point.prevZ, point.z, partialTicks);
                    pointSize = 0.03;
                    bb = new AxisAlignedBB(x, y, z, x + pointSize, y + pointSize, z + pointSize);
                    OGLUtils.enableBlending();
                    OGLUtils.disableDepth();
                    OGLUtils.disableTexture2D();
                    OGLUtils.color(color);
                    renderX = RenderManager.renderPosX;
                    renderY = RenderManager.renderPosY;
                    renderZ = RenderManager.renderPosZ;
                    GL11.glTranslated(-renderX, -renderY, -renderZ);
                    RenderGlobal.func_181561_a(bb, false, true);
                    GL11.glTranslated(renderX, renderY, renderZ);
                    OGLUtils.disableBlending();
                    OGLUtils.enableDepth();
                    OGLUtils.enableTexture2D();
                }
            }
            return;
        });
        EntityLivingBase target;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                target = KillAura.getInstance().getTarget();
                if (target != null) {
                    this.collectPoints(this.currentTarget = target);
                    this.currentPoint = this.findOptimalPoint(target, this.currentPoints);
                }
                else {
                    this.currentTarget = null;
                    this.currentPoint = null;
                }
            }
        });
    }
    
    public boolean shouldAdaptSpeed() {
        if (!this.adaptiveSpeedProperty.getValue()) {
            return false;
        }
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDist = this.currentPoint.x - player.posX;
        final double zDist = this.currentPoint.z - player.posZ;
        return StrictMath.sqrt(xDist * xDist + zDist * zDist) < 0.2;
    }
    
    public double getAdaptedSpeed() {
        if (this.currentTarget == null) {
            return 0.0;
        }
        final double xDist = this.currentTarget.posX - this.currentTarget.prevPosX;
        final double zDist = this.currentTarget.posZ - this.currentTarget.prevPosZ;
        return StrictMath.sqrt(xDist * xDist + zDist * zDist);
    }
    
    public static TargetStrafe getInstance() {
        return ModuleManager.getInstance(TargetStrafe.class);
    }
    
    public boolean shouldStrafe() {
        return this.currentPoint != null;
    }
    
    public void setSpeed(final MoveEntityEvent event, final double speed) {
        MovementUtils.setSpeed(event, speed, 1.0f, 0.0f, this.getYawToPoint(this.currentPoint));
    }
    
    private float getYawToPoint(final Point3D point) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDist = point.x - player.posX;
        final double zDist = point.z - player.posZ;
        final float rotationYaw = player.rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    private Point3D findOptimalPoint(final EntityLivingBase target, final List<Point3D> points) {
        if (this.behindProperty.getValue()) {
            Point3D bestPoint = null;
            float biggestDif = -1.0f;
            for (final Point3D point : points) {
                if (point.valid) {
                    final float yawChange = Math.abs(this.getYawChangeToPoint(target, point));
                    if (yawChange <= biggestDif) {
                        continue;
                    }
                    biggestDif = yawChange;
                    bestPoint = point;
                }
            }
            return bestPoint;
        }
        return null;
    }
    
    private float getYawChangeToPoint(final EntityLivingBase target, final Point3D point) {
        final double xDist = point.x - target.posX;
        final double zDist = point.z - target.posZ;
        final float rotationYaw = target.rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }
    
    private void collectPoints(final EntityLivingBase entity) {
        final int size = this.pointsProperty.getValue().intValue();
        final double radius = this.radiusProperty.getValue();
        this.currentPoints.clear();
        final double x = entity.posX;
        final double y = entity.posY;
        final double z = entity.posZ;
        final double prevX = entity.prevPosX;
        final double prevY = entity.prevPosY;
        final double prevZ = entity.prevPosZ;
        for (int i = 0; i < size; ++i) {
            final double cos = radius * StrictMath.cos(i * 6.2831855f / size);
            final double sin = radius * StrictMath.sin(i * 6.2831855f / size);
            final double pointX = x + cos;
            final double pointZ = z + sin;
            final Point3D point = new Point3D(pointX, y, pointZ, prevX + cos, prevY, prevZ + sin, this.validatePoint(pointX, pointZ));
            this.currentPoints.add(point);
        }
    }
    
    private boolean validatePoint(final double x, final double z) {
        final Vec3 pointVec = new Vec3(x, Wrapper.getPlayer().posY, z);
        final IBlockState blockState = Wrapper.getWorld().getBlockState(new BlockPos(pointVec));
        final boolean canBeSeen = Wrapper.getWorld().rayTraceBlocks(Wrapper.getPlayer().getPositionVector(), pointVec, false, false, false) == null;
        return !this.isOverVoid(x, z) && !blockState.getBlock().canCollideCheck(blockState, false) && canBeSeen;
    }
    
    private boolean isOverVoid(final double x, final double z) {
        for (double posY = Wrapper.getPlayer().posY; posY > 0.0; --posY) {
            if (!(Wrapper.getWorld().getBlockState(new BlockPos(x, posY, z)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }
    
    private static final class Point3D
    {
        private final double x;
        private final double y;
        private final double z;
        private final double prevX;
        private final double prevY;
        private final double prevZ;
        private final boolean valid;
        
        public Point3D(final double x, final double y, final double z, final double prevX, final double prevY, final double prevZ, final boolean valid) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.prevX = prevX;
            this.prevY = prevY;
            this.prevZ = prevZ;
            this.valid = valid;
        }
    }
}
