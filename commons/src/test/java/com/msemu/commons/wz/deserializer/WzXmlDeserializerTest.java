package com.msemu.commons.data.wz.deserializer;

import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.deserializer.WzXmlDeserializer;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Weber on 2018/4/20.
 */
public class WzXmlDeserializerTest {
    @Test
    public void testDeserializeWzFile() throws Exception {

        Path wzPath = Paths.get("E:\\twms120\\wz\\Character.wz");

        WzXmlDeserializer deserializer = new WzXmlDeserializer();


        WzFile charWz = deserializer.deserializeWzFile(wzPath);

    }

}