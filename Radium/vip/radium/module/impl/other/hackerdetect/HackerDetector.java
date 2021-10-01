// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other.hackerdetect;

import vip.radium.module.impl.other.hackerdetect.check.impl.OmniSprintCheck;
import vip.radium.module.impl.other.hackerdetect.check.impl.NoSlowCheck;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import vip.radium.module.impl.other.hackerdetect.check.Check;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import vip.radium.utils.Wrapper;
import java.util.HashMap;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import java.util.UUID;
import java.util.Map;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Hacker Detector", category = ModuleCategory.OTHER)
public final class HackerDetector extends Module
{
    private final DoubleProperty vlToAlertProperty;
    private final MultiSelectEnumProperty<CheckType> checksProperty;
    private final Map<UUID, Flag> flagMap;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public HackerDetector() {
        this.vlToAlertProperty = new DoubleProperty("VL To Alert", 20.0, 1.0, 100.0, 1.0);
        this.checksProperty = new MultiSelectEnumProperty<CheckType>("Checks", new CheckType[] { CheckType.NO_SLOW });
        this.flagMap = new HashMap<UUID, Flag>();
        final Iterator<EntityPlayer> iterator;
        EntityPlayer player;
        CheckType[] array;
        final Object o;
        int length;
        int i = 0;
        CheckType check;
        Check type;
        UUID uuid;
        Flag flag;
        final Flag flag3;
        Flag flag2;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                Wrapper.getLoadedPlayers().iterator();
                while (iterator.hasNext()) {
                    player = iterator.next();
                    if (player instanceof EntityPlayerSP) {
                        continue;
                    }
                    else {
                        array = this.checksProperty.getValues();
                        for (length = o.length; i < length; ++i) {
                            check = array[i];
                            type = check.getCheck();
                            if (type.flag(player)) {
                                uuid = player.getUniqueID();
                                if (this.flagMap.containsKey(uuid)) {
                                    flag = this.flagMap.get(uuid);
                                    if (flag.checkClass == type.getClass()) {
                                        Flag.access$2(flag3, flag3.vl + 1);
                                        if (this.shouldAlert(flag.vl)) {
                                            this.flag(flag);
                                        }
                                    }
                                }
                                else {
                                    flag2 = new Flag(player, type.getClass(), 1);
                                    this.flagMap.put(uuid, flag2);
                                    if (this.shouldAlert(1)) {
                                        this.flag(flag2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    private boolean shouldAlert(final int vl) {
        return this.vlToAlertProperty.getValue().intValue() <= vl;
    }
    
    private void flag(final Flag flag) {
        final EntityPlayer flaggedPlayer = flag.player;
        if (flag.vl % this.vlToAlertProperty.getValue().intValue() == 0) {
            RadiumClient.getInstance().getNotificationManager().add(new Notification("Hacker Detected", String.format("%s flagged for %s (VL - %s)", flaggedPlayer.getGameProfile().getName(), flag.checkClass.getSimpleName(), flag.vl), flag.vl * 30L, NotificationType.WARNING));
        }
    }
    
    private enum CheckType
    {
        NO_SLOW("NO_SLOW", 0, (Check)new NoSlowCheck()), 
        OMNI_SPRINT("OMNI_SPRINT", 1, (Check)new OmniSprintCheck());
        
        private final Check check;
        
        private CheckType(final String name, final int ordinal, final Check check) {
            this.check = check;
        }
        
        public Check getCheck() {
            return this.check;
        }
    }
    
    private static final class Flag
    {
        private final EntityPlayer player;
        private final Class<?> checkClass;
        private int vl;
        
        public Flag(final EntityPlayer player, final Class<?> checkClass, final int vl) {
            this.player = player;
            this.checkClass = checkClass;
            this.vl = vl;
        }
        
        static /* synthetic */ void access$2(final Flag flag, final int vl) {
            flag.vl = vl;
        }
    }
}
