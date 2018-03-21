package com.msemu.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/14.
 */
public class ClassUtils {
    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

    public ClassUtils() {
    }

    public static Object singletonInstance(Class<?> clazz) {
        try {
            Method e = clazz.getDeclaredMethod("getInstance");
            return e.invoke(null);
        } catch (Exception var2) {
            log.error("Error while calling singleton instance of " + clazz.getSimpleName(), var2);
            return null;
        }
    }

    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Class klass = type; klass != Object.class; klass = klass.getSuperclass()) {
            List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
            methods.addAll(allMethods.stream().filter((method) -> method.getClass().isAnnotationPresent(annotation)).collect(Collectors.toList()));
        }
        return methods;
    }
}