package com.msemu.commons.data.loader;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/21.
 */
public interface IDataLoader<T, V> {
    T load(V source) throws IOException;
}
