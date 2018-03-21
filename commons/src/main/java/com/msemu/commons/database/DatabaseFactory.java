package com.msemu.commons.database;

import com.msemu.core.configs.DatabaseConfig;
import com.msemu.core.startup.StartupComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Weber on 2018/3/14.
 */
@StartupComponent("Database")
public class DatabaseFactory {
    private static final Logger log = LoggerFactory.getLogger(DatabaseFactory.class);

    private static final AtomicReference<DatabaseFactory> instance = new AtomicReference<>();
    private final HashMap<Long, ConnectionWrapper> connections = new HashMap<>();

    public DatabaseFactory() {
        try {
            testConnection();
            log.info("DatabaseFactory initialized");
        } catch (SQLException except) {
            log.error("connection to database fail", except);
        }
    }

    public static DatabaseFactory getInstance() {
        DatabaseFactory value = instance.get();
        if (value == null) {
            synchronized (instance) {
                value = instance.get();
                if (value == null) {
                    value = new DatabaseFactory();
                    instance.set(value);
                }
            }
        }
        return value;
    }

    public Connection getConnection() {
        Long threadId = Thread.currentThread().getId();
        ConnectionWrapper connectionWrapper = connections.get(threadId);
        if (connectionWrapper == null) {
            Connection connection = connectToDatabase();
            connectionWrapper = new ConnectionWrapper(connection);
            connections.put(threadId, connectionWrapper);
        }
        return connectionWrapper.getConnection();
    }

    private void testConnection() throws SQLException {
        Connection testCon = getConnection();
        PreparedStatement pre = testCon.prepareStatement("SELECT 1");
        pre.execute();
    }

    private Connection connectToDatabase() {
        Connection connection = null;
        Properties dbProps = new Properties();
        dbProps.put("user", DatabaseConfig.USER);
        dbProps.put("password", DatabaseConfig.PASSWORD);
        dbProps.put("connectTimeout", Long.toString(DatabaseConfig.CONNECT_TIMEOUT));
        dbProps.put("socketTimeout", Long.toString(DatabaseConfig.SOCKET_TIMEOUT));
        dbProps.put("tcpNoDelay", Boolean.toString(DatabaseConfig.TCP_NO_DELAY));
        dbProps.put("autoReconnect", Boolean.toString(DatabaseConfig.AUTO_RECONNECT));
        dbProps.put("serverTimezone", "Asia/Taipei");
        dbProps.put("characterEncoding", DatabaseConfig.CHARACTER_ENCODING);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DatabaseConfig.DATABASE_IP + ":" + DatabaseConfig.DATABASE_PORT +
                            "/" + DatabaseConfig.DATABASE_NAME, dbProps
            );
        } catch (SQLException | ClassNotFoundException except) {
            log.error("connect to database error", except);
        }
        return connection;
    }

    private static class ConnectionWrapper {
        private long lastAccesstime = 0;
        private Connection connection;

        public ConnectionWrapper(Connection connection) {
            this.connection = connection;
            this.lastAccesstime = System.currentTimeMillis();
        }

        public Connection getConnection() {
            if (isExpire()) {
                try {
                    this.connection.close();
                } catch (SQLException ignored) {
                }
                this.connection = DatabaseFactory.getInstance().connectToDatabase();
            }
            this.lastAccesstime = System.currentTimeMillis();
            return this.connection;
        }

        private boolean isExpire() {
            try {
                return ((System.currentTimeMillis() - lastAccesstime) > DatabaseConfig.SOCKET_TIMEOUT) || connection.isClosed();
            } catch (SQLException except) {
                return true;
            }
        }
    }
}
