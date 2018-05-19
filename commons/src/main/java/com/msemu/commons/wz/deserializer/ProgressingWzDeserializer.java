package com.msemu.commons.wz.deserializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/4/20.
 */
public class ProgressingWzDeserializer {

    protected List<Path> listFiles(Path dir, String ext) throws IOException {
        return Files.list(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toFile().getName().endsWith(ext))
                .collect(Collectors.toList());
    }

    protected List<Path> listDirs(Path dir) throws IOException {
        return Files.list(dir).filter(Files::isDirectory)
                .collect(Collectors.toList());
    }
}
