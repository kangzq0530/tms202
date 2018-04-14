package com.msemu.commons.utils;

import java.io.File;

/**
 * Created by Weber on 2018/4/11.
 */
public class DirUtils {

    public static void makeDirIfAbsent(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
