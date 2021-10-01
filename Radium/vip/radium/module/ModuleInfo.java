// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String label();
    
    int key() default 0;
    
    ModuleCategory category();
    
    String description() default "";
}
