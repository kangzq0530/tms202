package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(
        name = "configs/threads.properties"
)
public class ThreadsConfig {
    @ConfigComments(
            comment = {"Max thread used in EventManager"}
    )
    @ConfigProperty(
            name = "events.pool.size",
            value = "10"
    )
    public static int EVENT_POOL_SIZE = 10;
}
