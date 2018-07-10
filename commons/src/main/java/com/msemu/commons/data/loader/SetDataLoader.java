package com.msemu.commons.data.loader;

import lombok.Getter;

import java.util.Set;

public abstract class SetDataLoader<T> implements IDataLoader <Set<T>> {

    @Getter
    Set<T> data;

    protected abstract T create();

}
