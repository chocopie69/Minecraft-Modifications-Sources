// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.src.Config;
import java.lang.reflect.Method;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;
    
    public ReflectorMethod(final ReflectorClass reflectorClass, final String targetMethodName) {
        this(reflectorClass, targetMethodName, null, false);
    }
    
    public ReflectorMethod(final ReflectorClass reflectorClass, final String targetMethodName, final Class[] targetMethodParameterTypes) {
        this(reflectorClass, targetMethodName, targetMethodParameterTypes, false);
    }
    
    public ReflectorMethod(final ReflectorClass reflectorClass, final String targetMethodName, final Class[] targetMethodParameterTypes, final boolean lazyResolve) {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        if (!lazyResolve) {
            this.getTargetMethod();
        }
    }
    
    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            if (this.targetMethodParameterTypes == null) {
                final Method[] amethod = getMethods(oclass, this.targetMethodName);
                if (amethod.length <= 0) {
                    Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                    return null;
                }
                if (amethod.length > 1) {
                    Config.warn("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName);
                    for (int i = 0; i < amethod.length; ++i) {
                        final Method method = amethod[i];
                        Config.warn("(Reflector)  - " + method);
                    }
                    return null;
                }
                this.targetMethod = amethod[0];
            }
            else {
                this.targetMethod = getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
            }
            if (this.targetMethod == null) {
                Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                return null;
            }
            this.targetMethod.setAccessible(true);
            return this.targetMethod;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    public boolean exists() {
        return this.checked ? (this.targetMethod != null) : (this.getTargetMethod() != null);
    }
    
    public Class getReturnType() {
        final Method method = this.getTargetMethod();
        return (method == null) ? null : method.getReturnType();
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
    
    public static Method getMethod(final Class cls, final String methodName, final Class[] paramTypes) {
        final Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(methodName)) {
                final Class[] aclass = method.getParameterTypes();
                if (Reflector.matchesTypes(paramTypes, aclass)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    public static Method[] getMethods(final Class cls, final String methodName) {
        final List list = new ArrayList();
        final Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(methodName)) {
                list.add(method);
            }
        }
        final Method[] amethod2 = list.toArray(new Method[list.size()]);
        return amethod2;
    }
}
