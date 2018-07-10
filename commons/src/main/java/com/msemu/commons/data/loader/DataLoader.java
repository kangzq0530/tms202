package com.msemu.commons.data.loader;

import lombok.Getter;

import java.io.IOException;

public abstract class DataLoader<T> implements IDataLoader<T> {

    @Getter
    T data;

}
