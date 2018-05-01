package com.msemu.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Weber on 2018/4/11.
 */
public class FileUtils {

    public static void makeDirIfAbsent(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static String readFile(String path, Charset charset) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, charset);
    }
}
