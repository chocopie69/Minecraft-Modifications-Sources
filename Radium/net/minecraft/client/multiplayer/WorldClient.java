// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import net.optifine.DynamicLights;
import net.minecraft.src.Config;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Random;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.chunk.IChunkProvider;
import net.optifine.CustomGuis;
import net.optifine.override.PlayerControllerOF;
import net.optifine.reflect.Reflector;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import java.util.Set;
import net.minecraft.world.World;

public class WorldClient extends World
{
    private final Set<Entity> entityList;
    private final Set<Entity> entitySpawnQueue;
    private final Minecraft mc;
    private final Set<ChunkCoordIntPair> previousActiveChunkSet;
    private final NetHandlerPlayClient sendQueue;
    private ChunkProviderClient clientChunkProvider;
    private boolean playerUpdate;
    
    public WorldClient(final NetHandlerPlayClient p_i45063_1_, final WorldSettings p_i45063_2_, final int p_i45063_3_, final EnumDifficulty p_i45063_4_) {
        super(new SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), true);
        this.entityList = (Set<Entity>)Sets.newHashSet();
        this.entitySpawnQueue = (Set<Entity>)Sets.newHashSet();
        this.mc = Minecraft.getMinecraft();
        this.previousActiveChunkSet = (Set<ChunkCoordIntPair>)Sets.newHashSet();
        this.playerUpdate = false;
        this.sendQueue = p_i45063_1_;
        this.getWorldInfo().setDifficulty(p_i45063_4_);
        this.provider.registerWorld(this);
        this.setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = this.createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, this);
        if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
            this.mc.playerController = new PlayerControllerOF(this.mc, p_i45063_1_);
            CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
        if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
            this.setWorldTime(this.getWorldTime() + 1L);
        }
        for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); ++i) {
            final Entity entity = this.entitySpawnQueue.iterator().next();
            this.entitySpawnQueue.remove(entity);
            if (!this.loadedEntityList.contains(entity)) {
                this.spawnEntityInWorld(entity);
            }
        }
        this.clientChunkProvider.unloadQueuedChunks();
        this.updateBlocks();
    }
    
    public void invalidateBlockReceiveRegion(final int p_73031_1_, final int p_73031_2_, final int p_73031_3_, final int p_73031_4_, final int p_73031_5_, final int p_73031_6_) {
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        return this.clientChunkProvider = new ChunkProviderClient(this);
    }
    
    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        this.previousActiveChunkSet.retainAll(this.activeChunkSet);
        if (this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
            this.previousActiveChunkSet.clear();
        }
        int i = 0;
        for (final ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet) {
            if (!this.previousActiveChunkSet.contains(chunkcoordintpair)) {
                final int j = chunkcoordintpair.chunkXPos * 16;
                final int k = chunkcoordintpair.chunkZPos * 16;
                final Chunk chunk = this.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                this.playMoodSoundAndCheckLight(j, k, chunk);
                this.previousActiveChunkSet.add(chunkcoordintpair);
                if (++i >= 10) {
                    return;
                }
                continue;
            }
        }
    }
    
    public void doPreChunk(final int p_73025_1_, final int p_73025_2_, final boolean p_73025_3_) {
        if (p_73025_3_) {
            this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
        }
        else {
            this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
        }
        if (!p_73025_3_) {
            this.markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
        }
    }
    
    @Override
    public boolean spawnEntityInWorld(final Entity entityIn) {
        final boolean flag = super.spawnEntityInWorld(entityIn);
        this.entityList.add(entityIn);
        if (!flag) {
            this.entitySpawnQueue.add(entityIn);
        }
        else if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)entityIn));
        }
        return flag;
    }
    
    @Override
    public void removeEntity(final Entity entityIn) {
        super.removeEntity(entityIn);
        this.entityList.remove(entityIn);
    }
    
    @Override
    protected void onEntityAdded(final Entity entityIn) {
        super.onEntityAdded(entityIn);
        this.entitySpawnQueue.remove(entityIn);
    }
    
    @Override
    protected void onEntityRemoved(final Entity entityIn) {
        super.onEntityRemoved(entityIn);
        boolean flag = false;
        if (this.entityList.contains(entityIn)) {
            if (entityIn.isEntityAlive()) {
                this.entitySpawnQueue.add(entityIn);
                flag = true;
            }
            else {
                this.entityList.remove(entityIn);
            }
        }
    }
    
    public void addEntityToWorld(final int p_73027_1_, final Entity p_73027_2_) {
        final Entity entity = this.getEntityByID(p_73027_1_);
        if (entity != null) {
            this.removeEntity(entity);
        }
        this.entityList.add(p_73027_2_);
        p_73027_2_.setEntityId(p_73027_1_);
        if (!this.spawnEntityInWorld(p_73027_2_)) {
            this.entitySpawnQueue.add(p_73027_2_);
        }
        this.entitiesById.addKey(p_73027_1_, p_73027_2_);
    }
    
    @Override
    public Entity getEntityByID(final int id) {
        return (id == this.mc.thePlayer.getEntityId()) ? this.mc.thePlayer : super.getEntityByID(id);
    }
    
    public Entity removeEntityFromWorld(final int p_73028_1_) {
        final Entity entity = this.entitiesById.removeObject(p_73028_1_);
        if (entity != null) {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }
        return entity;
    }
    
    public boolean invalidateRegionAndSetBlock(final BlockPos p_180503_1_, final IBlockState p_180503_2_) {
        final int i = p_180503_1_.getX();
        final int j = p_180503_1_.getY();
        final int k = p_180503_1_.getZ();
        this.invalidateBlockReceiveRegion(i, j, k, i, j, k);
        return super.setBlockState(p_180503_1_, p_180503_2_, 3);
    }
    
    @Override
    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
    }
    
    @Override
    protected void updateWeather() {
    }
    
    @Override
    protected int getRenderDistanceChunks() {
        return this.mc.gameSettings.renderDistanceChunks;
    }
    
    public void doVoidFogParticles(final int p_73029_1_, final int p_73029_2_, final int p_73029_3_) {
        final int i = 16;
        final Random random = new Random();
        final ItemStack itemstack = this.mc.thePlayer.getHeldItem();
        final boolean flag = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 1000; ++j) {
            final int k = p_73029_1_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            final int l = p_73029_2_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            final int i2 = p_73029_3_ + this.rand.nextInt(i) - this.rand.nextInt(i);
            blockpos$mutableblockpos.func_181079_c(k, l, i2);
            final IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
            iblockstate.getBlock().randomDisplayTick(this, blockpos$mutableblockpos, iblockstate, random);
            if (flag && iblockstate.getBlock() == Blocks.barrier) {
                this.spawnParticle(EnumParticleTypes.BARRIER, k + 0.5f, l + 0.5f, i2 + 0.5f, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    public void removeAllEntities() {
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int i = 0; i < this.unloadedEntityList.size(); ++i) {
            final Entity entity = this.unloadedEntityList.get(i);
            final int j = entity.chunkCoordX;
            final int k = entity.chunkCoordZ;
            if (entity.addedToChunk && this.isChunkLoaded(j, k, true)) {
                this.getChunkFromChunkCoords(j, k).removeEntity(entity);
            }
        }
        for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
            this.onEntityRemoved(this.unloadedEntityList.get(l));
        }
        this.unloadedEntityList.clear();
        for (int i2 = 0; i2 < this.loadedEntityList.size(); ++i2) {
            final Entity entity2 = this.loadedEntityList.get(i2);
            if (entity2.ridingEntity != null) {
                if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
                    continue;
                }
                entity2.ridingEntity.riddenByEntity = null;
                entity2.ridingEntity = null;
            }
            if (entity2.isDead) {
                final int j2 = entity2.chunkCoordX;
                final int k2 = entity2.chunkCoordZ;
                if (entity2.addedToChunk && this.isChunkLoaded(j2, k2, true)) {
                    this.getChunkFromChunkCoords(j2, k2).removeEntity(entity2);
                }
                this.loadedEntityList.remove(i2--);
                this.onEntityRemoved(entity2);
            }
        }
    }
    
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
        crashreportcategory.addCrashSectionCallable("Forced entities", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(WorldClient.this.entityList.size()) + " total; " + WorldClient.this.entityList.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Retry entities", new Callable<String>() {
            @Override
            public String call() {
                return String.valueOf(WorldClient.this.entitySpawnQueue.size()) + " total; " + WorldClient.this.entitySpawnQueue.toString();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server brand", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return WorldClient.this.mc.thePlayer.getClientBrand();
            }
        });
        crashreportcategory.addCrashSectionCallable("Server type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return (WorldClient.this.mc.getIntegratedServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
            }
        });
        return crashreportcategory;
    }
    
    public void playSoundAtPos(final BlockPos p_175731_1_, final String p_175731_2_, final float p_175731_3_, final float p_175731_4_, final boolean p_175731_5_) {
        this.playSound(p_175731_1_.getX() + 0.5, p_175731_1_.getY() + 0.5, p_175731_1_.getZ() + 0.5, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
    }
    
    @Override
    public void playSound(final double x, final double y, final double z, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
        final double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
        final PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
        if (distanceDelay && d0 > 100.0) {
            final double d2 = Math.sqrt(d0) / 40.0;
            this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d2 * 20.0));
        }
        else {
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }
    
    @Override
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, final NBTTagCompound compund) {
        this.mc.effectRenderer.addEffect(new EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }
    
    public void setWorldScoreboard(final Scoreboard p_96443_1_) {
        this.worldScoreboard = p_96443_1_;
    }
    
    @Override
    public void setWorldTime(long time) {
        if (time < 0L) {
            time = -time;
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
        }
        else {
            this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
        }
        super.setWorldTime(time);
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        int i = super.getCombinedLight(pos, lightValue);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(pos, i);
        }
        return i;
    }
    
    @Override
    public boolean setBlockState(final BlockPos pos, final IBlockState newState, final int flags) {
        this.playerUpdate = this.isPlayerActing();
        final boolean flag = super.setBlockState(pos, newState, flags);
        this.playerUpdate = false;
        return flag;
    }
    
    private boolean isPlayerActing() {
        if (this.mc.playerController instanceof PlayerControllerOF) {
            final PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.mc.playerController;
            return playercontrollerof.isActing();
        }
        return false;
    }
    
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
}
