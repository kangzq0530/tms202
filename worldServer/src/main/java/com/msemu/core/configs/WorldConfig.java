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
    @ConfigComments(comment = { "The name of the world server", "Default: 雪吉拉" })
    @ConfigProperty(name = "world.name", value = "雪吉拉")
    public static String NAME = "雪吉拉";

    @ConfigComments(comment = { "The event description", "Default: " })
    @ConfigProperty(name = "world.event.desc", value = "")
    public static String EVENT_DESC = "";

    @ConfigComments(comment = { "The event drop rate percent", "Default: 100" })
    @ConfigProperty(name = "world.event.dropRate", value = "100")
    public static int EVENT_DROP_RATE = 0;

    @ConfigComments(comment = { "The event exp rate percent", "Default: 100" })
    @ConfigProperty(name = "world.event.mesoRate", value = "100")
    public static int EVENT_EXP_RATE = 100;

    @ConfigComments(comment = { "The server state", "Default: 0" })
    @ConfigProperty(name = "world.state", value = "0")
    public static int STATE = 0;

    @ConfigComments(comment = { "Max Character in one channel" })
    @ConfigProperty(name = "world.channel.maxLoading", value = "500")
    public static int CHANNEL_MAX_LOADING = 500;

    @ConfigComments(comment = { "The channel amount of the world server", "Default: 5" })
    @ConfigProperty(name = "world.channel.count", value = "5")
    public static int CHANNEL_COUNT = 5;

    @ConfigComments(comment = { "The base port for channel", "Default: 8585" })
    @ConfigProperty(name = "world.channel.portStart", value = "8585")
    public static int PORT_START;
}
