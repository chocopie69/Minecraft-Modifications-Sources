// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections;

import org.reflections.serializers.Serializer;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import org.reflections.adapters.MetadataAdapter;
import java.net.URL;
import org.reflections.scanners.Scanner;
import java.util.Set;

public interface Configuration
{
    Set<Scanner> getScanners();
    
    Set<URL> getUrls();
    
    MetadataAdapter getMetadataAdapter();
    
    Predicate<String> getInputsFilter();
    
    ExecutorService getExecutorService();
    
    Serializer getSerializer();
    
    ClassLoader[] getClassLoaders();
    
    boolean shouldExpandSuperTypes();
}
