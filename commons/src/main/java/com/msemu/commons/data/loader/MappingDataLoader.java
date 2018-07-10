package com.msemu.commons.data.loader;

import lombok.Getter;

import java.util.Map;

public abstract class MappingDataLoader<K> implements IDataLoader<Map<Integer, K>> {

    @Getter
    Map<Integer, K> data;


    protected abstract K create();

    abstract public K getItem(Integer index);

}
