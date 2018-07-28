/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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