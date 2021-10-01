// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl.render;

import vip.radium.event.CancellableEvent;

public final class DisplayTitleEvent extends CancellableEvent
{
    private final String title;
    
    public DisplayTitleEvent(final String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
}
