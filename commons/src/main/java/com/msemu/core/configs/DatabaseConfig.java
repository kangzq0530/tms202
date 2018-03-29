package com.msemu.core.configs;

import com.msemu.commons.config.annotation.ConfigComments;
import com.msemu.commons.config.annotation.ConfigFile;
import com.msemu.commons.config.annotation.ConfigProperty;
import com.msemu.commons.enums.GameServiceType;

/**
 * Created by Weber on 2018/3/29.
 */
@ConfigFile(
        name = "configs/database.properties"
)
public class DatabaseConfig {

    @ConfigComments(
            comment = {"The database Username."}
    )
    @ConfigProperty(
            name = "databaseUsername",
            value = "root"
    )
    public static String USERNAME;
    @ConfigComments(
            comment = {"The database Password."}
    )
    @ConfigProperty(
            name = "databasePassword",
            value = ""
    )
    public static String PASSWORD;
    @ConfigComments(
            comment = {"The database\'s host address."}
    )
    @ConfigProperty(
            name = "database.host",
            value = "localhost"
    )
    public static String HOST;
    @ConfigComments(
            comment = {"The port on which the database is running."}
    )
    @ConfigProperty(
            name = "database.port",
            value = "3306"
    )
    public static int PORT;
    @ConfigComments(
            comment = {"The database name."}
    )
    @ConfigProperty(
            name = "database.name",
            value = "msemu"
    )
    public static String DATABASE_NAME;
    @ConfigComments(
            comment = {"The database\'s connection url"}
    )
    @ConfigProperty(
            name = "database.showSql",
            value = "true"
    )
    public static boolean SHOW_SQL;
}

