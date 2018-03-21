package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(
        name = "configs/timer.properties"
)
public class TimerConfig {
    @ConfigProperty(
            name = "world.timer.core.pool.size",
            value = "5"
    )
    public static int WORLD_TIMER_CORE_POOL_SIZE;
    @ConfigProperty(
            name = "map.timer.core.pool.size",
            value = "5"
    )
    public static int MAP_TIMER_CORE_POOL_SIZE;
    @ConfigProperty(
            name = "effect.timer.core.pool.size",
            value = "5"
    )
    public static int EFFECT_TIMER_CORE_POOL_SIZE;
    @ConfigProperty(
            name = "event.timer.core.pool.size",
            value = "5"
    )
    public static int EVENT_TIMER_CORE_POOL_SIZE;
    @ConfigProperty(
            name = "etc.timer.core.pool.size",
            value = "5"
    )
    public static int ETC_TIMER_CORE_POOL_SIZE;
}
