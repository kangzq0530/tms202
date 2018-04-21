package com.msemu.commons.data.loader;

import com.msemu.commons.wz.WzManager;

/**
 * Created by Weber on 2018/4/21.
 */
public interface IDataLoader<T, V> {
    T load(V wzManager);
}
