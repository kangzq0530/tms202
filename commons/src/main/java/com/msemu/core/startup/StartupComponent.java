package com.msemu.core.startup;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IndexAnnotated
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartupComponent {
    String value();

    Class<?>[] dependency() default {};
}

