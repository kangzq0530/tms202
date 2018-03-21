package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;

/**
 * Created by Weber on 2018/3/14.
 */
@ConfigFile(
        name = "configs/database.properties"
)
public class DatabaseConfig {
    @ConfigComments(
            comment = {"The database server IP."}
    )
    @ConfigProperty(
            name = "databaseServerIP",
            value = "localhost"
    )
    public static String DATABASE_IP;
    @ConfigComments(
            comment = {"The database server port."}
    )
    @ConfigProperty(
            name = "databaseServerPort",
            value = "3306"
    )
    public static String DATABASE_PORT;
    @ConfigComments(
            comment = {"The database Account."}
    )
    @ConfigProperty(
            name = "databaseUser",
            value = "root"
    )
    public static String USER;

    @ConfigComments(
            comment = {"The database Password."}
    )
    @ConfigProperty(
            name = "databasePassword",
            value = ""
    )
    public static String PASSWORD;

    @ConfigComments(
            comment = {"The database Name."}
    )
    @ConfigProperty(
            name = "databaseName",
            value = "zzms"
    )
    public static String DATABASE_NAME;

    @ConfigComments(
            comment = {"The database connect server timeout."}
    )
    @ConfigProperty(
            name = "connectTimeout",
            value = "3000"
    )
    public static long CONNECT_TIMEOUT;

    @ConfigComments(
            comment = {"The database socket timeout."}
    )
    @ConfigProperty(
            name = "socketTimeout",
            value = "3600000"
    )
    public static long SOCKET_TIMEOUT;

    @ConfigComments(
            comment = {"If connecting using TCP/IP, should the driver set SO_TCP_NODELAY (disabling the Nagle Algorithm)?"}
    )
    @ConfigProperty(
            name = "tcpNoDelay",
            value = "true"
    )
    public static boolean TCP_NO_DELAY;

    @ConfigComments(
            comment = {"Auto reconnect to database."}
    )
    @ConfigProperty(
            name = "autoReconnect",
            value = "true"
    )
    public static boolean AUTO_RECONNECT;

    @ConfigComments(
            comment = {"Max reconnection times to database."}
    )
    @ConfigProperty(
            name = "maxReconnectTimes",
            value = "3"
    )
    public static String MAX_RECONNECT_TIMES;

    @ConfigComments(
            comment = {"The character encoding of database connection."}
    )
    @ConfigProperty(
            name = "characterEncoding",
            value = "UTF8"
    )
    public static String CHARACTER_ENCODING;
}
