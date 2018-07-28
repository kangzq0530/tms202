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
import com.msemu.commons.reload.Reloadable;

/**
 * Created by Weber on 2018/3/18.
 */
@Reloadable(name = "world",
        group = "all")
@ConfigFile(name = "configs/world.properties")
public class WorldConfig {
    @ConfigComments(comment = {"The name of the world server", "Default: 雪吉拉"})
    @ConfigProperty(name = "world.name", value = "雪吉拉")
    public static String NAME = "雪吉拉";

    @ConfigComments(comment = {"The event description", "Default: "})
    @ConfigProperty(name = "world.event.desc", value = "")
    public static String EVENT_DESC = "";

    @ConfigComments(comment = {"The event drop rate percent", "Default: 100"})
    @ConfigProperty(name = "world.event.dropRate", value = "100")
    public static int EVENT_DROP_RATE = 0;

    @ConfigComments(comment = {"The event exp rate percent", "Default: 100"})
    @ConfigProperty(name = "world.event.mesoRate", value = "100")
    public static int EVENT_EXP_RATE = 100;

    @ConfigComments(comment = {"The server state", "Default: 0"})
    @ConfigProperty(name = "world.state", value = "0")
    public static int STATE = 0;

    @ConfigComments(comment = {"Max Character in one channel"})
    @ConfigProperty(name = "world.channel.maxLoading", value = "500")
    public static int CHANNEL_MAX_LOADING = 500;

    @ConfigComments(comment = {"The channel amount of the world server", "Default: 5"})
    @ConfigProperty(name = "world.channel.count", value = "5")
    public static int CHANNEL_COUNT = 5;
}
