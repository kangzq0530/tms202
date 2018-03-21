package com.msemu.commons.reload;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Weber on 2018/3/14.
 */
@IndexAnnotated
@Retention(RetentionPolicy.RUNTIME)
public @interface Reloadable {
    String name();

    String group();
}

