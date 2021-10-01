// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.keybind;

import java.util.Iterator;
import java.util.HashSet;
import vip.radium.module.Module;
import java.util.Collection;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.KeyPressEvent;
import io.github.nevalackin.homoBus.Listener;
import java.util.Set;

public final class BindSystem
{
    private final Set<Bindable> objects;
    @EventLink
    public final Listener<KeyPressEvent> onKeyPressEvent;
    
    public BindSystem(final Collection<Module> modules) {
        this.objects = new HashSet<Bindable>(modules);
        final Iterator<Bindable> iterator;
        Bindable object;
        this.onKeyPressEvent = (event -> {
            this.objects.iterator();
            while (iterator.hasNext()) {
                object = iterator.next();
                if (object.getKey() == event.getKey()) {
                    object.onPress();
                }
            }
        });
    }
    
    public void register(final Bindable object) {
        this.objects.add(object);
    }
}
