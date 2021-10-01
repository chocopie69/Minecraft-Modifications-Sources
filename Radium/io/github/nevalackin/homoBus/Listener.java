// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nevalackin.homoBus;

@FunctionalInterface
public interface Listener<Event>
{
    void call(final Event p0);
}
