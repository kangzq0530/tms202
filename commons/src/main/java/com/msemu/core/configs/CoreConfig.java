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
