// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nevalackin.homoBus;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;

public final class EventBus<Event>
{
    private final Map<Type, List<CallSite<Event>>> callSiteMap;
    private final Map<Type, List<Listener<Event>>> listenerCache;
    
    public EventBus() {
        this.callSiteMap = new HashMap<Type, List<CallSite<Event>>>();
        this.listenerCache = new HashMap<Type, List<Listener<Event>>>();
    }
    
    public final void subscribe(final Object subscriber) {
        Field[] declaredFields;
        for (int length = (declaredFields = subscriber.getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            if (field.isAnnotationPresent(EventLink.class)) {
                final Type eventType = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    final Listener<Event> listener = (Listener<Event>)field.get(subscriber);
                    final Priority annotation = field.getAnnotation(Priority.class);
                    byte priority;
                    if (annotation != null) {
                        priority = annotation.value();
                    }
                    else {
                        priority = 2;
                    }
                    if (this.callSiteMap.containsKey(eventType)) {
                        final List<CallSite<Event>> callSites = this.callSiteMap.get(eventType);
                        callSites.add(new CallSite<Event>(subscriber, listener, priority));
                        callSites.sort((o1, o2) -> o2.priority - o1.priority);
                    }
                    else {
                        this.callSiteMap.put(eventType, new ArrayList<CallSite<Event>>((Collection<? extends CallSite<Event>>)Arrays.asList(new CallSite(subscriber, (Listener<Event>)listener, priority))));
                    }
                }
                catch (IllegalAccessException ex) {}
            }
        }
        this.populateListenerCache();
    }
    
    private void populateListenerCache() {
        final Map<Type, List<CallSite<Event>>> callSiteMap = this.callSiteMap;
        final Map<Type, List<Listener<Event>>> listenerCache = this.listenerCache;
        for (final Type type : callSiteMap.keySet()) {
            final List<CallSite<Event>> callSites = callSiteMap.get(type);
            final int size = callSites.size();
            final List<Listener<Event>> listeners = new ArrayList<Listener<Event>>(size);
            for (int i = 0; i < size; ++i) {
                listeners.add(((CallSite<Object>)callSites.get(i)).listener);
            }
            listenerCache.put(type, listeners);
        }
    }
    
    public final void unsubscribe(final Object subscriber) {
        for (final List<CallSite<Event>> callSites : this.callSiteMap.values()) {
            callSites.removeIf(eventCallSite -> eventCallSite.owner == subscriber);
        }
        this.populateListenerCache();
    }
    
    public final void post(final Event event) {
        final List<Listener<Event>> listeners = this.listenerCache.get(event.getClass());
        if (listeners != null) {
            for (final Listener<Event> listener : listeners) {
                listener.call(event);
            }
        }
    }
    
    private static class CallSite<Event>
    {
        private final Object owner;
        private final Listener<Event> listener;
        private final byte priority;
        
        public CallSite(final Object owner, final Listener<Event> listener, final byte priority) {
            this.owner = owner;
            this.listener = listener;
            this.priority = priority;
        }
    }
}
