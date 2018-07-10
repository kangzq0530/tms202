package com.msemu.commons.data.loader;

import java.io.IOException;

/**
 * Created by Weber on 2018/4/21.
 */
public interface IDataLoader<T> {

    T getData();

    void load() throws IOException;
}
