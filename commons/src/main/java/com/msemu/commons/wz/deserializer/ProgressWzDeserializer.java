package com.msemu.commons.wz.deserializer;

import com.msemu.commons.utils.FileUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Weber on 2018/4/20.
 */
public class ProgressWzDeserializer {
    @Getter
    @Setter(value = AccessLevel.PROTECTED)
    protected int total = 0;
    @Getter
    @Setter(value = AccessLevel.PROTECTED)
    protected int current = 0;

    protected static void createDirSafe(String path) {
        FileUtils.makeDirIfAbsent(path);
    }

}
