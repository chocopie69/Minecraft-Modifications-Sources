// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.RadiumClient;

public final class PlayerInfoCache
{
    private static double lastDist;
    private static double prevLastDist;
    private static double baseMoveSpeed;
    
    static {
        RadiumClient.getInstance().getEventBus().subscribe(new PlayerUpdatePositionSubscriber(null));
    }
    
    public static double getPrevLastDist() {
        return PlayerInfoCache.prevLastDist;
    }
    
    public static double getLastDist() {
        return PlayerInfoCache.lastDist;
    }
    
    public static double getBaseMoveSpeed() {
        return PlayerInfoCache.baseMoveSpeed;
    }
    
    public static double getFriction(final double moveSpeed) {
        return MovementUtils.calculateFriction(moveSpeed, PlayerInfoCache.lastDist, PlayerInfoCache.baseMoveSpeed);
    }
    
    static /* synthetic */ void access$0(final double baseMoveSpeed) {
        PlayerInfoCache.baseMoveSpeed = baseMoveSpeed;
    }
    
    static /* synthetic */ void access$2(final double prevLastDist) {
        PlayerInfoCache.prevLastDist = prevLastDist;
    }
    
    static /* synthetic */ void access$3(final double lastDist) {
        PlayerInfoCache.lastDist = lastDist;
    }
    
    private static class PlayerUpdatePositionSubscriber
    {
        @EventLink
        private final Listener<UpdatePositionEvent> onUpdatePositionEvent;
        
        private PlayerUpdatePositionSubscriber() {
            EntityPlayerSP player;
            double xDif;
            double zDif;
            this.onUpdatePositionEvent = (event -> {
                if (event.isPre()) {
                    PlayerInfoCache.access$0(MovementUtils.getBaseMoveSpeed());
                    player = Wrapper.getPlayer();
                    xDif = player.posX - player.lastTickPosX;
                    zDif = player.posZ - player.lastTickPosZ;
                    PlayerInfoCache.access$2(PlayerInfoCache.lastDist);
                    PlayerInfoCache.access$3(StrictMath.sqrt(xDif * xDif + zDif * zDif));
                }
            });
        }
    }
}
