// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.lang.ref.WeakReference;
import net.minecraft.client.renderer.ImageBufferDownload;

public class CapeImageBuffer extends ImageBufferDownload
{
    public ImageBufferDownload imageBufferDownload;
    public final WeakReference<AbstractClientPlayer> playerRef;
    public final ResourceLocation resourceLocation;
    
    public CapeImageBuffer(final AbstractClientPlayer player, final ResourceLocation resourceLocation) {
        this.playerRef = new WeakReference<AbstractClientPlayer>(player);
        this.resourceLocation = resourceLocation;
        this.imageBufferDownload = new ImageBufferDownload();
    }
    
    @Override
    public BufferedImage parseUserSkin(final BufferedImage imageRaw) {
        return CapeUtils.parseCape(imageRaw);
    }
    
    @Override
    public void skinAvailable() {
        final AbstractClientPlayer player = this.playerRef.get();
        if (player != null) {
            player.setLocationOfCape(this.resourceLocation);
        }
    }
}
