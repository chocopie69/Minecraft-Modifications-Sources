// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render;

import net.minecraft.client.renderer.OpenGlHelper;
import vip.radium.module.ModuleManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import io.github.nevalackin.homoBus.Priority;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.Render3DEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Chest ESP", category = ModuleCategory.RENDER)
public final class ChestESP extends Module
{
    private static ChestESP instance;
    public final EnumProperty<Mode> mode;
    private final Property<Boolean> outlineProperty;
    private final Property<Integer> colorProperty;
    public final Property<Integer> visibleColorProperty;
    public final Property<Integer> occludedColorProperty;
    public final Property<Boolean> visibleFlatProperty;
    public final Property<Boolean> occludedFlatProperty;
    @EventLink
    @Priority(0)
    public final Listener<Render3DEvent> onRender3DEvent;
    
    public ChestESP() {
        this.mode = new EnumProperty<Mode>("Mode", Mode.BOX);
        this.outlineProperty = new Property<Boolean>("Outline", false, () -> this.mode.getValue() == Mode.BOX);
        this.colorProperty = new Property<Integer>("Color", 8158463, () -> this.mode.getValue() != Mode.CHAMS);
        this.visibleColorProperty = new Property<Integer>("Visible Color", Colors.PURPLE, this::isChams);
        this.occludedColorProperty = new Property<Integer>("Occluded Color", -1753449217, this::isChams);
        this.visibleFlatProperty = new Property<Boolean>("Visible Flat", false, this::isChams);
        this.occludedFlatProperty = new Property<Boolean>("Occluded Flat", true, this::isChams);
        final Iterator<TileEntity> iterator;
        TileEntity entity;
        BlockPos pos;
        AxisAlignedBB bb;
        boolean outline;
        double rX;
        double rY;
        double rZ;
        this.onRender3DEvent = (e -> {
            if (this.mode.getValue() == Mode.BOX) {
                Wrapper.getWorld().loadedTileEntityList.iterator();
                while (iterator.hasNext()) {
                    entity = iterator.next();
                    if (entity instanceof TileEntityChest) {
                        pos = entity.getPos();
                        bb = entity.getBlockType().getCollisionBoundingBox(Wrapper.getWorld(), pos, entity.getBlockType().getStateFromMeta(entity.getBlockMetadata()));
                        if (bb != null) {
                            GL11.glDisable(2929);
                            OGLUtils.enableBlending();
                            GL11.glDepthMask(false);
                            GL11.glDisable(3553);
                            OGLUtils.color(this.colorProperty.getValue());
                            outline = this.outlineProperty.getValue();
                            if (outline) {
                                GL11.glLineWidth(1.0f);
                                GL11.glEnable(2848);
                                GL11.glHint(3154, 4354);
                            }
                            rX = RenderManager.renderPosX;
                            rY = RenderManager.renderPosY;
                            rZ = RenderManager.renderPosZ;
                            GL11.glTranslated(-rX, -rY, -rZ);
                            RenderGlobal.func_181561_a(bb, outline, true);
                            GL11.glTranslated(rX, rY, rZ);
                            GL11.glEnable(2929);
                            OGLUtils.disableBlending();
                            GL11.glDepthMask(true);
                            if (outline) {
                                GL11.glDisable(2848);
                            }
                            GL11.glEnable(3553);
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        });
    }
    
    public boolean isChams() {
        return this.mode.getValue() == Mode.CHAMS;
    }
    
    public static ChestESP getInstance() {
        return (ChestESP.instance != null) ? ChestESP.instance : (ChestESP.instance = ModuleManager.getInstance(ChestESP.class));
    }
    
    public static void preOccludedRender(final int occludedColor, final boolean occludedFlat) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        if (occludedFlat) {
            GL11.glDisable(2896);
        }
        GL11.glEnable(32823);
        GL11.glPolygonOffset(0.0f, -1000000.0f);
        OpenGlHelper.setLightmapTextureCoords(1, 240.0f, 240.0f);
        GL11.glDepthMask(false);
        OGLUtils.color(occludedColor);
    }
    
    public static void preVisibleRender(final int visibleColor, final boolean visibleFlat, final boolean occludedFlat) {
        GL11.glDepthMask(true);
        if (occludedFlat && !visibleFlat) {
            GL11.glEnable(2896);
        }
        else if (!occludedFlat && visibleFlat) {
            GL11.glDisable(2896);
        }
        OGLUtils.color(visibleColor);
        GL11.glDisable(32823);
    }
    
    public static void postRender(final boolean visibleFlat) {
        if (visibleFlat) {
            GL11.glEnable(2896);
        }
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    private enum Mode
    {
        CHAMS("CHAMS", 0), 
        BOX("BOX", 1);
        
        private Mode(final String name, final int ordinal) {
        }
    }
}
