// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.world;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vip.radium.utils.render.LockedResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.InventoryUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.Render2DEvent;
import vip.radium.utils.TimerUtil;
import vip.radium.property.impl.DoubleProperty;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.SafeWalkEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.Property;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Scaffold", category = ModuleCategory.WORLD)
public final class Scaffold extends Module
{
    private static final BlockPos[] BLOCK_POSITIONS;
    private static final EnumFacing[] FACINGS;
    private final Property<Boolean> swingProperty;
    private final Property<Boolean> safeWalkProperty;
    @EventLink
    public final Listener<SafeWalkEvent> onSafeWalkEvent;
    private final Property<Boolean> towerProperty;
    private final Property<Boolean> blockCountBarProperty;
    private final DoubleProperty blockSlotProperty;
    private final TimerUtil clickTimer;
    private int blockCount;
    @EventLink
    public final Listener<Render2DEvent> onRender2DEvent;
    private int originalHotBarSlot;
    private int bestBlockStack;
    private BlockData data;
    private float[] angles;
    @EventLink
    @Priority(1)
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    static {
        BLOCK_POSITIONS = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        FACINGS = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };
    }
    
    public Scaffold() {
        this.swingProperty = new Property<Boolean>("Swing", false);
        this.safeWalkProperty = new Property<Boolean>("Safe Walk", false);
        this.onSafeWalkEvent = (safeWalkEvent -> safeWalkEvent.setCancelled(this.safeWalkProperty.getValue()));
        this.towerProperty = new Property<Boolean>("Tower", true);
        this.blockCountBarProperty = new Property<Boolean>("Block Count", true);
        this.blockSlotProperty = new DoubleProperty("Override Slot", 9.0, 1.0, 9.0, 1.0);
        this.clickTimer = new TimerUtil();
        LockedResolution resolution;
        float x;
        float y;
        float percentage;
        float width;
        float half;
        this.onRender2DEvent = (event -> {
            if (this.blockCountBarProperty.getValue()) {
                resolution = event.getResolution();
                x = resolution.getWidth() / 2.0f;
                y = resolution.getHeight() / 2.0f + 15.0f;
                percentage = Math.min(1.0f, this.blockCount / 128.0f);
                width = 80.0f;
                half = width / 2.0f;
                Gui.drawRect(x - half - 0.5f, y - 2.0f, x + half + 0.5f, y + 2.0f, 2013265920);
                GL11.glEnable(3089);
                OGLUtils.startScissorBox(resolution, (int)(x - half), (int)y - 2, (int)(width * percentage), 4);
                RenderingUtils.drawGradientRect(x - half, y - 1.5f, x - half + width, y + 1.5f, true, -1571930, -16711936);
                GL11.glDisable(3089);
            }
            return;
        });
        boolean override;
        int i;
        ItemStack stack;
        int blockSlot;
        BlockPos blockUnder;
        BlockData data;
        int hotBarSlot;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                this.updateBlockCount();
                this.data = null;
                this.bestBlockStack = findBestBlockStack();
                if (this.bestBlockStack != -1) {
                    if (this.bestBlockStack < 36 && this.clickTimer.hasElapsed(250L)) {
                        override = true;
                        i = 44;
                        while (i >= 36) {
                            stack = Wrapper.getStackInSlot(i);
                            if (!InventoryUtils.isValid(stack)) {
                                InventoryUtils.windowClick(this.bestBlockStack, i - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                                this.bestBlockStack = i;
                                override = false;
                                break;
                            }
                            else {
                                --i;
                            }
                        }
                        if (override) {
                            blockSlot = this.blockSlotProperty.getValue().intValue() - 1;
                            InventoryUtils.windowClick(this.bestBlockStack, blockSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                            this.bestBlockStack = blockSlot + 36;
                        }
                    }
                    blockUnder = getBlockUnder();
                    data = getBlockData(blockUnder);
                    if (data == null) {
                        data = getBlockData(blockUnder.add(0, -1, 0));
                    }
                    if (data != null && this.bestBlockStack >= 36) {
                        if (validateReplaceable(data) && data.hitVec != null) {
                            this.angles = getRotations(data.hitVec);
                        }
                        else {
                            data = null;
                        }
                    }
                    if (this.angles != null) {
                        event.setYaw(this.angles[0]);
                        event.setPitch(this.angles[1]);
                    }
                    this.data = data;
                }
            }
            else if (this.data != null && this.bestBlockStack != -1 && this.bestBlockStack >= 36) {
                hotBarSlot = this.bestBlockStack - 36;
                if (Wrapper.getPlayer().inventory.currentItem != hotBarSlot) {
                    Wrapper.getPlayer().inventory.currentItem = hotBarSlot;
                }
                if (Wrapper.getPlayerController().onPlayerRightClick(Wrapper.getPlayer(), Wrapper.getWorld(), Wrapper.getPlayer().getCurrentEquippedItem(), this.data.pos, this.data.face, this.data.hitVec)) {
                    if (this.towerProperty.getValue() && Wrapper.getGameSettings().keyBindJump.isKeyDown() && Wrapper.getWorld().checkBlockCollision(Wrapper.getPlayer().getEntityBoundingBox().addCoord(0.0, -0.0626, 0.0))) {
                        Wrapper.getPlayer().motionY = MovementUtils.getJumpHeight() - 4.54352838557992E-4;
                    }
                    if (this.swingProperty.getValue()) {
                        Wrapper.getPlayer().swingItem();
                    }
                    else {
                        Wrapper.sendPacket(new C0APacketAnimation());
                    }
                }
            }
        });
    }
    
    private static int findBestBlockStack() {
        int bestSlot = -1;
        int blockCount = -1;
        for (int i = 44; i >= 9; --i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack) && stack.stackSize > blockCount) {
                bestSlot = i;
                blockCount = stack.stackSize;
            }
        }
        return bestSlot;
    }
    
    private static BlockPos getBlockUnder() {
        final EntityPlayerSP player = Wrapper.getPlayer();
        return new BlockPos(player.posX, player.posY - 1.0, player.posZ);
    }
    
    private static float[] getRotations(final Vec3 hitVec) {
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double xDist = hitVec.xCoord - player.posX;
        final double zDist = hitVec.zCoord - player.posZ;
        final double yDist = hitVec.yCoord - (player.posY + player.getEyeHeight());
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float rotationYaw = Wrapper.getPlayer().rotationYaw;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        final float yaw = rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
        final float rotationPitch = Wrapper.getPlayer().rotationPitch;
        final float var2 = (float)(-(StrictMath.atan2(yDist, fDist) * 180.0 / 3.141592653589793));
        final float pitch = rotationPitch + MathHelper.wrapAngleTo180_float(var2 - rotationPitch);
        return new float[] { yaw, MathHelper.clamp_float(pitch, -90.0f, 90.0f) };
    }
    
    private static boolean validateBlockRange(final BlockData data) {
        final Vec3 pos = data.hitVec;
        if (pos == null) {
            return false;
        }
        final EntityPlayerSP player = Wrapper.getPlayer();
        final double x = pos.xCoord - player.posX;
        final double y = pos.yCoord - (player.posY + player.getEyeHeight());
        final double z = pos.zCoord - player.posZ;
        return StrictMath.sqrt(x * x + y * y + z * z) <= 4.0;
    }
    
    private static boolean validateReplaceable(final BlockData data) {
        final BlockPos pos = data.pos.offset(data.face);
        final World world = Wrapper.getWorld();
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }
    
    private static BlockData getBlockData(final BlockPos pos) {
        final BlockPos[] blockPositions = Scaffold.BLOCK_POSITIONS;
        final EnumFacing[] facings = Scaffold.FACINGS;
        final WorldClient world = Wrapper.getWorld();
        for (int i = 0; i < blockPositions.length; ++i) {
            final BlockPos blockPos = pos.add(blockPositions[i]);
            if (InventoryUtils.isValidBlock(world.getBlockState(blockPos).getBlock(), false)) {
                final BlockData data = new BlockData(blockPos, facings[i]);
                if (validateBlockRange(data)) {
                    return data;
                }
            }
        }
        final BlockPos posBelow = pos.add(0, -1, 0);
        if (InventoryUtils.isValidBlock(world.getBlockState(posBelow).getBlock(), false)) {
            final BlockData data2 = new BlockData(posBelow, EnumFacing.UP);
            if (validateBlockRange(data2)) {
                return data2;
            }
        }
        BlockPos[] array;
        for (int length = (array = blockPositions).length, l = 0; l < length; ++l) {
            final BlockPos blockPosition = array[l];
            final BlockPos blockPos2 = pos.add(blockPosition);
            for (int j = 0; j < blockPositions.length; ++j) {
                final BlockPos blockPos3 = blockPos2.add(blockPositions[j]);
                if (InventoryUtils.isValidBlock(world.getBlockState(blockPos3).getBlock(), false)) {
                    final BlockData data3 = new BlockData(blockPos3, facings[j]);
                    if (validateBlockRange(data3)) {
                        return data3;
                    }
                }
            }
        }
        BlockPos[] array2;
        for (int length2 = (array2 = blockPositions).length, n = 0; n < length2; ++n) {
            final BlockPos blockPosition = array2[n];
            final BlockPos blockPos2 = pos.add(blockPosition);
            BlockPos[] array3;
            for (int length3 = (array3 = blockPositions).length, n2 = 0; n2 < length3; ++n2) {
                final BlockPos position = array3[n2];
                final BlockPos blockPos4 = blockPos2.add(position);
                for (int k = 0; k < blockPositions.length; ++k) {
                    final BlockPos blockPos5 = blockPos4.add(blockPositions[k]);
                    if (InventoryUtils.isValidBlock(world.getBlockState(blockPos5).getBlock(), false)) {
                        final BlockData data4 = new BlockData(blockPos5, facings[k]);
                        if (validateBlockRange(data4)) {
                            return data4;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void onEnable() {
        this.blockCount = 0;
        this.originalHotBarSlot = Wrapper.getPlayer().inventory.currentItem;
    }
    
    @Override
    public void onDisable() {
        this.angles = null;
        Wrapper.getPlayer().inventory.currentItem = this.originalHotBarSlot;
    }
    
    public boolean isRotating() {
        return this.angles != null;
    }
    
    private void updateBlockCount() {
        this.blockCount = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack)) {
                this.blockCount += stack.stackSize;
            }
        }
    }
    
    private static class BlockData
    {
        private final BlockPos pos;
        private final EnumFacing face;
        private final Vec3 hitVec;
        
        public BlockData(final BlockPos pos, final EnumFacing face) {
            this.pos = pos;
            this.face = face;
            this.hitVec = this.getHitVec();
        }
        
        private Vec3 getHitVec() {
            final Vec3i directionVec = this.face.getDirectionVec();
            double x = directionVec.getX() * 0.5;
            double z = directionVec.getZ() * 0.5;
            if (this.face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
                x = -x;
                z = -z;
            }
            final Vec3 hitVec = new Vec3(this.pos).addVector(x + z, directionVec.getY() * 0.5, x + z);
            final Vec3 src = Wrapper.getPlayer().getPositionEyes(1.0f);
            final MovingObjectPosition obj = Wrapper.getWorld().rayTraceBlocks(src, hitVec, false, false, true);
            if (obj == null || obj.hitVec == null || obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                return null;
            }
            if (this.face != EnumFacing.DOWN && this.face != EnumFacing.UP) {
                obj.hitVec = obj.hitVec.addVector(0.0, -0.2, 0.0);
            }
            return obj.hitVec;
        }
    }
}
