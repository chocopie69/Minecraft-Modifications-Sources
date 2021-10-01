// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other.hackerdetect.check.impl;

import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import java.util.Map;
import vip.radium.module.impl.other.hackerdetect.check.Check;

public final class OmniSprintCheck implements Check
{
    private static final Map<Integer, Integer> VL_MAP;
    
    static {
        VL_MAP = new HashMap<Integer, Integer>();
    }
    
    @Override
    public boolean flag(final EntityPlayer player) {
        return false;
    }
}
