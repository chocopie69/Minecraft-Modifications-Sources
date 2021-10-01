// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other.hackerdetect.check.impl;

import vip.radium.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import java.util.Map;
import vip.radium.module.impl.other.hackerdetect.check.Check;

public final class NoSlowCheck implements Check
{
    private static final Map<Integer, Integer> VL_MAP;
    
    static {
        VL_MAP = new HashMap<Integer, Integer>();
    }
    
    @Override
    public boolean flag(final EntityPlayer player) {
        final int id = player.getEntityId();
        if (player.isUsingItem() && player.isSprinting() && PlayerUtils.isMoving(player)) {
            if (NoSlowCheck.VL_MAP.containsKey(id)) {
                NoSlowCheck.VL_MAP.put(id, NoSlowCheck.VL_MAP.get(id) + 1);
            }
            else {
                NoSlowCheck.VL_MAP.put(id, 1);
            }
        }
        else {
            NoSlowCheck.VL_MAP.put(id, 0);
        }
        return NoSlowCheck.VL_MAP.get(id) >= 10;
    }
}
