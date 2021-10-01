// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.event.impl;

import vip.radium.event.Event;

public final class KeyPressEvent implements Event
{
    private final int key;
    
    public KeyPressEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
