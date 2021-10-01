// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render.hud;

import vip.radium.event.CancellableEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.RenderScoreboardEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "No Scoreboard", category = ModuleCategory.RENDER)
public final class NoScoreboard extends Module
{
    @EventLink
    public final Listener<RenderScoreboardEvent> onRenderScoreboardEvent;
    
    public NoScoreboard() {
        this.onRenderScoreboardEvent = CancellableEvent::setCancelled;
    }
}
