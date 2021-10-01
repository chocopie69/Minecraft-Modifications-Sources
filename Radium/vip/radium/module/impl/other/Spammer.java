// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.world.TickEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.utils.TimerUtil;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Spammer", category = ModuleCategory.OTHER)
public final class Spammer extends Module
{
    private final TimerUtil timerUtil;
    @EventLink
    public final Listener<TickEvent> tickEvent;
    
    public Spammer() {
        this.timerUtil = new TimerUtil();
        final Minecraft mc;
        int i;
        this.tickEvent = (event -> {
            mc = Minecraft.getMinecraft();
            if (this.timerUtil.hasElapsed(1000L)) {
                for (i = 0; i < 10; ++i) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("test 123"));
                }
                mc.thePlayer.addChatMessage(new ChatComponentText("/l bw"));
            }
        });
    }
}
