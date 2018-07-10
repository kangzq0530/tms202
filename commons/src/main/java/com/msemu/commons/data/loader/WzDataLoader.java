package com.msemu.commons.data.loader;

import com.msemu.commons.data.loader.wz.WzManager;
import lombok.Getter;

/**
 * Created by Weber on 2018/4/21.
 */
public abstract class WzDataLoader<T> implements IDataLoader<T> , IWzToDatExporter {

    public WzDataLoader(WzManager wzManager) {
        this.wzManager = wzManager;
    }

    @Getter
    public final WzManager wzManager;


}
