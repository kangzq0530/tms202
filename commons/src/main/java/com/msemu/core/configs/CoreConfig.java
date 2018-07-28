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

package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;
import com.msemu.commons.enums.GameServiceType;

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(
        name = "configs/core.properties"
)
public class CoreConfig {
    @ConfigComments(comment = {"login server service type.", "Options: Korean, KoreanTest, Taiwan, Japan, Sea, China, Tespia, Global, Brazil"})
    @ConfigProperty(name = "core.game.service.type", value = "Taiwan")
    public static GameServiceType GAME_SERVICE_TYPE;

    @ConfigComments(comment = {"Server Version"})
    @ConfigProperty(name = "core.game.service.version", value = "145")
    public static int GAME_VERSION;

    @ConfigComments(comment = {"Server Patch Version"})
    @ConfigProperty(name = "core.game.service.patch", value = "1")
    public static String GAME_PATCH_VERSION;

    @ConfigComments(comment = {"Server WZ Path"})
    @ConfigProperty(name = "core.wz.path", value = "./wz")
    public static String WZ_PATH;


    @ConfigComments(comment = {"Server Dat Path"})
    @ConfigProperty(name = "core.dat.path", value = "./dat")
    public static String DAT_PATH;


    @ConfigComments(comment = {"Server Scripts Path"})
    @ConfigProperty(name = "core.script.path", value = "./scripts")
    public static String SCRIPT_PATH;

    @ConfigComments(comment = {"Show Packet"})
    @ConfigProperty(name = "core.network.showPacket", value = "true")
    public static boolean SHOW_PACKET;

}
