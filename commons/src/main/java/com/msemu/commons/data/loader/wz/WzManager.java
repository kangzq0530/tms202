/*
 * MIT License
 *
 * Copyright (c) 2018 msemu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.msemu.commons.data.loader.wz;

import com.msemu.commons.wz.WzFile;
import com.msemu.commons.wz.deserializer.WzXmlDeserializer;
import com.msemu.core.configs.CoreConfig;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Weber on 2018/4/21.
 */
public class WzManager {

    public static final String ITEM = "Item.wz";
    public static final String ETC = "Etc.wz";
    public static final String CHARACTER = "Character.wz";
    public static final String STRING = "String.wz";
    public static final String QUEST = "Quest.wz";
    public static final String SKILL = "Skill.wz";
    public static final String MAP = "Map.wz";
    public static final String MAP2 = "Map2.wz";
    public static final String MOB = "Mob.wz";
    public static final String MOB2 = "Mob2.wz";
    public static final String NPC = "Npc.wz";
    public static final String REACTOR = "Reactor.wz";

    private static final Logger log = LoggerFactory.getLogger(WzManager.class);

    @Getter
    private Map<String, WzFile> caches = new HashMap<>();

    @Getter
    private WzXmlDeserializer deserializer = new WzXmlDeserializer();

    public WzManager(String... filenames) {
        preloadWz(filenames);
    }

    protected WzFile loadWz(String name) {
        Path wzPath = Paths.get(CoreConfig.WZ_PATH + "/" + name);
        try {
            log.info("Loading ... {}", name);
            WzFile wz = deserializer.deserializeWzFile(wzPath);
            caches.put(wz.getName(), wz);
            return wz;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            log.error("preload wz error", e);
        }
        return null;
    }

    protected void preloadWz(String... filenames) {
        Arrays.stream(filenames).forEach(this::loadWz);
    }

    public WzFile getWz(String name) {
        if (caches.containsKey(name)) {
            return caches.get(name);
        }
        return loadWz(name);
    }

    public void clearData() {
        caches.values().forEach(WzFile::dispose);
        caches.clear();
    }


}
