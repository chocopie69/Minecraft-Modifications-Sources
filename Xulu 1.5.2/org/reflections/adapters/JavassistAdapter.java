// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.adapters;

import java.util.stream.Stream;
import java.util.stream.IntStream;
import javassist.bytecode.Descriptor;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Objects;
import javassist.bytecode.AccessFlag;
import org.reflections.util.Utils;
import java.io.IOException;
import org.reflections.ReflectionsException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import org.reflections.vfs.Vfs;
import javassist.bytecode.annotation.Annotation;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import javassist.bytecode.ParameterAnnotationsAttribute;
import java.util.ArrayList;
import javassist.bytecode.AnnotationsAttribute;
import java.util.List;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.ClassFile;

public class JavassistAdapter implements MetadataAdapter<ClassFile, FieldInfo, MethodInfo>
{
    public static boolean includeInvisibleTag;
    
    @Override
    public List<FieldInfo> getFields(final ClassFile cls) {
        return (List<FieldInfo>)cls.getFields();
    }
    
    @Override
    public List<MethodInfo> getMethods(final ClassFile cls) {
        return (List<MethodInfo>)cls.getMethods();
    }
    
    @Override
    public String getMethodName(final MethodInfo method) {
        return method.getName();
    }
    
    @Override
    public List<String> getParameterNames(final MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        return this.splitDescriptorToTypeNames(descriptor);
    }
    
    @Override
    public List<String> getClassAnnotationNames(final ClassFile aClass) {
        return this.getAnnotationNames((AnnotationsAttribute)aClass.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)aClass.getAttribute("RuntimeInvisibleAnnotations")) : null);
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final FieldInfo field) {
        return this.getAnnotationNames((AnnotationsAttribute)field.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)field.getAttribute("RuntimeInvisibleAnnotations")) : null);
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final MethodInfo method) {
        return this.getAnnotationNames((AnnotationsAttribute)method.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)method.getAttribute("RuntimeInvisibleAnnotations")) : null);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final MethodInfo method, final int parameterIndex) {
        final List<String> result = new ArrayList<String>();
        final List<ParameterAnnotationsAttribute> parameterAnnotationsAttributes = Arrays.asList((ParameterAnnotationsAttribute)method.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)method.getAttribute("RuntimeInvisibleParameterAnnotations"));
        for (final ParameterAnnotationsAttribute parameterAnnotationsAttribute : parameterAnnotationsAttributes) {
            if (parameterAnnotationsAttribute != null) {
                final Annotation[][] annotations = parameterAnnotationsAttribute.getAnnotations();
                if (parameterIndex >= annotations.length) {
                    continue;
                }
                final Annotation[] annotation = annotations[parameterIndex];
                result.addAll(this.getAnnotationNames(annotation));
            }
        }
        return result;
    }
    
    @Override
    public String getReturnTypeName(final MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.lastIndexOf(")") + 1);
        return this.splitDescriptorToTypeNames(descriptor).get(0);
    }
    
    @Override
    public String getFieldName(final FieldInfo field) {
        return field.getName();
    }
    
    @Override
    public ClassFile getOrCreateClassObject(final Vfs.File file) {
        InputStream inputStream = null;
        try {
            inputStream = file.openInputStream();
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
            return new ClassFile(dis);
        }
        catch (IOException e) {
            throw new ReflectionsException("could not create class file from " + file.getName(), e);
        }
        finally {
            Utils.close(inputStream);
        }
    }
    
    @Override
    public String getMethodModifier(final MethodInfo method) {
        final int accessFlags = method.getAccessFlags();
        return AccessFlag.isPrivate(accessFlags) ? "private" : (AccessFlag.isProtected(accessFlags) ? "protected" : (this.isPublic(accessFlags) ? "public" : ""));
    }
    
    @Override
    public String getMethodKey(final ClassFile cls, final MethodInfo method) {
        return this.getMethodName(method) + "(" + Utils.join(this.getParameterNames(method), ", ") + ")";
    }
    
    @Override
    public String getMethodFullKey(final ClassFile cls, final MethodInfo method) {
        return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
    }
    
    @Override
    public boolean isPublic(final Object o) {
        final Integer accessFlags = (o instanceof ClassFile) ? ((ClassFile)o).getAccessFlags() : ((o instanceof FieldInfo) ? ((FieldInfo)o).getAccessFlags() : ((o instanceof MethodInfo) ? Integer.valueOf(((MethodInfo)o).getAccessFlags()) : null));
        return accessFlags != null && AccessFlag.isPublic((int)accessFlags);
    }
    
    @Override
    public String getClassName(final ClassFile cls) {
        return cls.getName();
    }
    
    @Override
    public String getSuperclassName(final ClassFile cls) {
        return cls.getSuperclass();
    }
    
    @Override
    public List<String> getInterfacesNames(final ClassFile cls) {
        return Arrays.asList(cls.getInterfaces());
    }
    
    @Override
    public boolean acceptsInput(final String file) {
        return file.endsWith(".class");
    }
    
    private List<String> getAnnotationNames(final AnnotationsAttribute... annotationsAttributes) {
        if (annotationsAttributes != null) {
            return Arrays.stream(annotationsAttributes).filter(Objects::nonNull).flatMap(annotationsAttribute -> Arrays.stream(annotationsAttribute.getAnnotations())).map((Function<? super Object, ?>)Annotation::getTypeName).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    private List<String> getAnnotationNames(final Annotation[] annotations) {
        return Arrays.stream(annotations).map((Function<? super Annotation, ?>)Annotation::getTypeName).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
    }
    
    private List<String> splitDescriptorToTypeNames(final String descriptors) {
        List<String> result = new ArrayList<String>();
        if (descriptors != null && descriptors.length() != 0) {
            final List<Integer> indices = new ArrayList<Integer>();
            final Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);
            while (iterator.hasNext()) {
                indices.add(iterator.next());
            }
            indices.add(descriptors.length());
            final List<Integer> list;
            result = IntStream.range(0, indices.size() - 1).mapToObj(i -> Descriptor.toString(descriptors.substring(list.get(i), list.get(i + 1)))).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        }
        return result;
    }
    
    static {
        JavassistAdapter.includeInvisibleTag = true;
    }
}
