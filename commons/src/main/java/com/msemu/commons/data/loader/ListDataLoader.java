package com.msemu.commons.data.loader;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class ListDataLoader<T> implements IDataLoader<List<T>> {

    @Getter
    List<T> data = new ArrayList<>();

    abstract public T getItem(int index);

    abstract T create();

}
