// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.adapters;

import org.reflections.util.Utils;
import java.lang.reflect.Modifier;
import org.reflections.ReflectionUtils;
import org.reflections.vfs.Vfs;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Member;
import java.lang.reflect.Field;

public class JavaReflectionAdapter implements MetadataAdapter<Class, Field, Member>
{
    @Override
    public List<Field> getFields(final Class cls) {
        return Arrays.asList(cls.getDeclaredFields());
    }
    
    @Override
    public List<Member> getMethods(final Class cls) {
        final List<Member> methods = new ArrayList<Member>();
        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
        methods.addAll(Arrays.asList(cls.getDeclaredConstructors()));
        return methods;
    }
    
    @Override
    public String getMethodName(final Member method) {
        return (method instanceof Method) ? method.getName() : ((method instanceof Constructor) ? "<init>" : null);
    }
    
    @Override
    public List<String> getParameterNames(final Member member) {
        final Class<?>[] parameterTypes = (member instanceof Method) ? ((Method)member).getParameterTypes() : ((member instanceof Constructor) ? ((Constructor)member).getParameterTypes() : null);
        return (List<String>)((parameterTypes != null) ? Arrays.stream(parameterTypes).map((Function<? super Class<?>, ?>)JavaReflectionAdapter::getName).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()) : Collections.emptyList());
    }
    
    @Override
    public List<String> getClassAnnotationNames(final Class aClass) {
        return this.getAnnotationNames(aClass.getDeclaredAnnotations());
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final Field field) {
        return this.getAnnotationNames(field.getDeclaredAnnotations());
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final Member method) {
        final Annotation[] annotations = (method instanceof Method) ? ((Method)method).getDeclaredAnnotations() : ((method instanceof Constructor) ? ((Constructor)method).getDeclaredAnnotations() : null);
        return this.getAnnotationNames(annotations);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final Member method, final int parameterIndex) {
        final Annotation[][] annotations = (method instanceof Method) ? ((Method)method).getParameterAnnotations() : ((method instanceof Constructor) ? ((Constructor)method).getParameterAnnotations() : null);
        return this.getAnnotationNames((Annotation[])((annotations != null) ? annotations[parameterIndex] : null));
    }
    
    @Override
    public String getReturnTypeName(final Member method) {
        return ((Method)method).getReturnType().getName();
    }
    
    @Override
    public String getFieldName(final Field field) {
        return field.getName();
    }
    
    @Override
    public Class getOrCreateClassObject(final Vfs.File file) throws Exception {
        return this.getOrCreateClassObject(file, (ClassLoader[])null);
    }
    
    public Class getOrCreateClassObject(final Vfs.File file, final ClassLoader... loaders) throws Exception {
        final String name = file.getRelativePath().replace("/", ".").replace(".class", "");
        return ReflectionUtils.forName(name, loaders);
    }
    
    @Override
    public String getMethodModifier(final Member method) {
        return Modifier.toString(method.getModifiers());
    }
    
    @Override
    public String getMethodKey(final Class cls, final Member method) {
        return this.getMethodName(method) + "(" + Utils.join(this.getParameterNames(method), ", ") + ")";
    }
    
    @Override
    public String getMethodFullKey(final Class cls, final Member method) {
        return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
    }
    
    @Override
    public boolean isPublic(final Object o) {
        final Integer mod = (o instanceof Class) ? ((Class)o).getModifiers() : ((o instanceof Member) ? Integer.valueOf(((Member)o).getModifiers()) : null);
        return mod != null && Modifier.isPublic(mod);
    }
    
    @Override
    public String getClassName(final Class cls) {
        return cls.getName();
    }
    
    @Override
    public String getSuperclassName(final Class cls) {
        final Class superclass = cls.getSuperclass();
        return (superclass != null) ? superclass.getName() : "";
    }
    
    @Override
    public List<String> getInterfacesNames(final Class cls) {
        final Class[] classes = cls.getInterfaces();
        return (List<String>)((classes != null) ? Arrays.stream(classes).map((Function<? super Class, ?>)Class::getName).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()) : Collections.emptyList());
    }
    
    @Override
    public boolean acceptsInput(final String file) {
        return file.endsWith(".class");
    }
    
    private List<String> getAnnotationNames(final Annotation[] annotations) {
        return Arrays.stream(annotations).map(annotation -> annotation.annotationType().getName()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
    }
    
    public static String getName(final Class type) {
        if (type.isArray()) {
            try {
                Class cl = type;
                int dim = 0;
                while (cl.isArray()) {
                    ++dim;
                    cl = cl.getComponentType();
                }
                return cl.getName() + Utils.repeat("[]", dim);
            }
            catch (Throwable t) {}
        }
        return type.getName();
    }
}
