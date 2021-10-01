// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections;

import java.util.stream.Stream;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.Objects;
import org.reflections.util.ClasspathHelper;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.regex.Pattern;
import java.lang.reflect.Member;
import java.lang.reflect.AnnotatedElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import org.reflections.util.Utils;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.List;

public abstract class ReflectionUtils
{
    public static boolean includeObject;
    private static List<String> primitiveNames;
    private static List<Class> primitiveTypes;
    private static List<String> primitiveDescriptors;
    
    public static Set<Class<?>> getAllSuperTypes(final Class<?> type, final Predicate<? super Class<?>>... predicates) {
        final Set<Class<?>> result = new LinkedHashSet<Class<?>>();
        if (type != null && (ReflectionUtils.includeObject || !type.equals(Object.class))) {
            result.add(type);
            for (final Class<?> supertype : getSuperTypes(type)) {
                result.addAll(getAllSuperTypes(supertype, (Predicate<? super Class<?>>[])new Predicate[0]));
            }
        }
        return Utils.filter(result, predicates);
    }
    
    public static Set<Class<?>> getSuperTypes(final Class<?> type) {
        final Set<Class<?>> result = new LinkedHashSet<Class<?>>();
        final Class<?> superclass = type.getSuperclass();
        final Class<?>[] interfaces = type.getInterfaces();
        if (superclass != null && (ReflectionUtils.includeObject || !superclass.equals(Object.class))) {
            result.add(superclass);
        }
        if (interfaces != null && interfaces.length > 0) {
            result.addAll(Arrays.asList(interfaces));
        }
        return result;
    }
    
    public static Set<Method> getAllMethods(final Class<?> type, final Predicate<? super Method>... predicates) {
        final Set<Method> result = new HashSet<Method>();
        for (final Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) {
            result.addAll(getMethods(t, predicates));
        }
        return result;
    }
    
    public static Set<Method> getMethods(final Class<?> t, final Predicate<? super Method>... predicates) {
        return Utils.filter(t.isInterface() ? t.getMethods() : t.getDeclaredMethods(), predicates);
    }
    
    public static Set<Constructor> getAllConstructors(final Class<?> type, final Predicate<? super Constructor>... predicates) {
        final Set<Constructor> result = new HashSet<Constructor>();
        for (final Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) {
            result.addAll(getConstructors(t, predicates));
        }
        return result;
    }
    
    public static Set<Constructor> getConstructors(final Class<?> t, final Predicate<? super Constructor>... predicates) {
        return (Set<Constructor>)Utils.filter(t.getDeclaredConstructors(), (Predicate<? super Constructor<?>>[])predicates);
    }
    
    public static Set<Field> getAllFields(final Class<?> type, final Predicate<? super Field>... predicates) {
        final Set<Field> result = new HashSet<Field>();
        for (final Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) {
            result.addAll(getFields(t, predicates));
        }
        return result;
    }
    
    public static Set<Field> getFields(final Class<?> type, final Predicate<? super Field>... predicates) {
        return Utils.filter(type.getDeclaredFields(), predicates);
    }
    
    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(final T type, final Predicate<Annotation>... predicates) {
        final Set<Annotation> result = new HashSet<Annotation>();
        if (type instanceof Class) {
            for (final Class<?> t : getAllSuperTypes((Class<?>)type, (Predicate<? super Class<?>>[])new Predicate[0])) {
                result.addAll(getAnnotations(t, predicates));
            }
        }
        else {
            result.addAll(getAnnotations((AnnotatedElement)type, predicates));
        }
        return result;
    }
    
    public static <T extends AnnotatedElement> Set<Annotation> getAnnotations(final T type, final Predicate<Annotation>... predicates) {
        return Utils.filter(type.getDeclaredAnnotations(), (Predicate<? super Annotation>[])predicates);
    }
    
    public static <T extends AnnotatedElement> Set<T> getAll(final Set<T> elements, final Predicate<? super T>... predicates) {
        return Utils.filter(elements, predicates);
    }
    
    public static <T extends Member> Predicate<T> withName(final String name) {
        return input -> input != null && input.getName().equals(name);
    }
    
    public static <T extends Member> Predicate<T> withPrefix(final String prefix) {
        return input -> input != null && input.getName().startsWith(prefix);
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withPattern(final String regex) {
        return input -> Pattern.matches(regex, input.toString());
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(final Class<? extends Annotation> annotation) {
        return input -> input != null && input.isAnnotationPresent(annotation);
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(final Class<? extends Annotation>... annotations) {
        return input -> input != null && Arrays.equals(annotations, annotationTypes(input.getAnnotations()));
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(final Annotation annotation) {
        return input -> input != null && input.isAnnotationPresent(annotation.annotationType()) && areAnnotationMembersMatching(input.getAnnotation(annotation.annotationType()), annotation);
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(final Annotation... annotations) {
        Annotation[] inputAnnotations;
        return input -> {
            if (input != null) {
                inputAnnotations = input.getAnnotations();
                if (inputAnnotations.length == annotations.length) {
                    return IntStream.range(0, inputAnnotations.length).allMatch(i -> areAnnotationMembersMatching(inputAnnotations[i], annotations[i]));
                }
            }
            return true;
        };
    }
    
    public static Predicate<Member> withParameters(final Class<?>... types) {
        return input -> Arrays.equals(parameterTypes(input), types);
    }
    
    public static Predicate<Member> withParametersAssignableTo(final Class... types) {
        return input -> isAssignable(types, parameterTypes(input));
    }
    
    public static Predicate<Member> withParametersAssignableFrom(final Class... types) {
        return input -> isAssignable(parameterTypes(input), types);
    }
    
    public static Predicate<Member> withParametersCount(final int count) {
        return input -> input != null && parameterTypes(input).length == count;
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Class<? extends Annotation> annotationClass) {
        return input -> input != null && annotationTypes(parameterAnnotations(input)).stream().anyMatch(input1 -> input1.equals(annotationClass));
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Annotation annotation) {
        return input -> input != null && parameterAnnotations(input).stream().anyMatch(input1 -> areAnnotationMembersMatching(annotation, input1));
    }
    
    public static <T> Predicate<Field> withType(final Class<T> type) {
        return input -> input != null && input.getType().equals(type);
    }
    
    public static <T> Predicate<Field> withTypeAssignableTo(final Class<T> type) {
        return input -> input != null && type.isAssignableFrom(input.getType());
    }
    
    public static <T> Predicate<Method> withReturnType(final Class<T> type) {
        return input -> input != null && input.getReturnType().equals(type);
    }
    
    public static <T> Predicate<Method> withReturnTypeAssignableTo(final Class<T> type) {
        return input -> input != null && type.isAssignableFrom(input.getReturnType());
    }
    
    public static <T extends Member> Predicate<T> withModifier(final int mod) {
        return input -> input != null && (input.getModifiers() & mod) != 0x0;
    }
    
    public static Predicate<Class<?>> withClassModifier(final int mod) {
        return input -> input != null && (input.getModifiers() & mod) != 0x0;
    }
    
    public static Class<?> forName(final String typeName, final ClassLoader... classLoaders) {
        if (getPrimitiveNames().contains(typeName)) {
            return getPrimitiveTypes().get(getPrimitiveNames().indexOf(typeName));
        }
        String type;
        if (typeName.contains("[")) {
            final int i = typeName.indexOf("[");
            type = typeName.substring(0, i);
            final String array = typeName.substring(i).replace("]", "");
            if (getPrimitiveNames().contains(type)) {
                type = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
            }
            else {
                type = "L" + type + ";";
            }
            type = array + type;
        }
        else {
            type = typeName;
        }
        final List<ReflectionsException> reflectionsExceptions = new ArrayList<ReflectionsException>();
        final ClassLoader[] classLoaders2 = ClasspathHelper.classLoaders(classLoaders);
        final int length = classLoaders2.length;
        int j = 0;
        while (j < length) {
            final ClassLoader classLoader = classLoaders2[j];
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                }
                catch (Throwable e) {
                    reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e));
                }
            }
            try {
                return classLoader.loadClass(type);
            }
            catch (Throwable e) {
                reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e));
                ++j;
                continue;
            }
            break;
        }
        if (Reflections.log != null) {
            for (final ReflectionsException reflectionsException : reflectionsExceptions) {
                Reflections.log.warn("could not get type for name " + typeName + " from any class loader", (Throwable)reflectionsException);
            }
        }
        return null;
    }
    
    public static <T> Set<Class<? extends T>> forNames(final Collection<String> classes, final ClassLoader... classLoaders) {
        return classes.stream().map(className -> forName(className, classLoaders)).filter(Objects::nonNull).collect((Collector<? super Object, ?, Set<Class<? extends T>>>)Collectors.toCollection((Supplier<R>)LinkedHashSet::new));
    }
    
    private static Class[] parameterTypes(final Member member) {
        return (Class[])((member != null) ? ((member.getClass() == Method.class) ? ((Method)member).getParameterTypes() : ((member.getClass() == Constructor.class) ? ((Constructor)member).getParameterTypes() : null)) : null);
    }
    
    private static Set<Annotation> parameterAnnotations(final Member member) {
        final Annotation[][] annotations = (member instanceof Method) ? ((Method)member).getParameterAnnotations() : ((member instanceof Constructor) ? ((Constructor)member).getParameterAnnotations() : null);
        return Arrays.stream(annotations).flatMap((Function<? super Annotation[], ? extends Stream<?>>)Arrays::stream).collect((Collector<? super Object, ?, Set<Annotation>>)Collectors.toSet());
    }
    
    private static Set<Class<? extends Annotation>> annotationTypes(final Collection<Annotation> annotations) {
        return annotations.stream().map((Function<? super Annotation, ?>)Annotation::annotationType).collect((Collector<? super Object, ?, Set<Class<? extends Annotation>>>)Collectors.toSet());
    }
    
    private static Class<? extends Annotation>[] annotationTypes(final Annotation[] annotations) {
        return Arrays.stream(annotations).map((Function<? super Annotation, ?>)Annotation::annotationType).toArray(Class[]::new);
    }
    
    private static void initPrimitives() {
        if (ReflectionUtils.primitiveNames == null) {
            ReflectionUtils.primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
            ReflectionUtils.primitiveTypes = (List<Class>)Arrays.asList(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE);
            ReflectionUtils.primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
        }
    }
    
    private static List<String> getPrimitiveNames() {
        initPrimitives();
        return ReflectionUtils.primitiveNames;
    }
    
    private static List<Class> getPrimitiveTypes() {
        initPrimitives();
        return ReflectionUtils.primitiveTypes;
    }
    
    private static List<String> getPrimitiveDescriptors() {
        initPrimitives();
        return ReflectionUtils.primitiveDescriptors;
    }
    
    private static boolean areAnnotationMembersMatching(final Annotation annotation1, final Annotation annotation2) {
        if (annotation2 != null && annotation1.annotationType() == annotation2.annotationType()) {
            for (final Method method : annotation1.annotationType().getDeclaredMethods()) {
                try {
                    if (!method.invoke(annotation1, new Object[0]).equals(method.invoke(annotation2, new Object[0]))) {
                        return false;
                    }
                }
                catch (Exception e) {
                    throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", method.getName(), annotation1.annotationType()), e);
                }
            }
            return true;
        }
        return false;
    }
    
    private static boolean isAssignable(final Class[] childClasses, final Class[] parentClasses) {
        if (childClasses == null) {
            return parentClasses == null || parentClasses.length == 0;
        }
        return childClasses.length == parentClasses.length && IntStream.range(0, childClasses.length).noneMatch(i -> !parentClasses[i].isAssignableFrom(childClasses[i]) || (parentClasses[i] == Object.class && childClasses[i] != Object.class));
    }
    
    static {
        ReflectionUtils.includeObject = false;
    }
}
