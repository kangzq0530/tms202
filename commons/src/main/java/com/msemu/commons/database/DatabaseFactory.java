package com.msemu.commons.database;

import com.msemu.core.configs.DatabaseConfig;
import com.msemu.core.startup.StartupComponent;
import com.mysql.fabric.xmlrpc.base.Data;
import org.atteo.classindex.ClassIndex;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by Weber on 2018/3/14.
 */
@StartupComponent("Database")
public class DatabaseFactory {
    private static final Logger log = LoggerFactory.getLogger(DatabaseFactory.class);

    private static final AtomicReference<DatabaseFactory> instance = new AtomicReference<>();
    private SessionFactory sessionFactory;
    private List<Session> sessions;

    public DatabaseFactory() {
        initializeDatabase();
        log.info("DatabaseFactory initialized");
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

    private void initializeDatabase() {
        Iterable<Class<?>> dbClasses = ClassIndex.getAnnotated(Schema.class);
        Configuration configuration = new Configuration();
        dbClasses.forEach(configuration::addAnnotatedClass);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
                .setProperty("hibernate.connection.url", "jdbc:mysql://" + DatabaseConfig.HOST + ":"+ DatabaseConfig.PORT +
                        "/"+ DatabaseConfig.DATABASE_NAME +"?autoReconnect=true&amp;useSSL=false")
                .setProperty("hibernate.connection.username", DatabaseConfig.USERNAME)
                .setProperty("hibernate.connection.password", DatabaseConfig.PASSWORD)
                .setProperty("hibernate.enable_lazy_load_no_trans", "true")
                .setProperty("hibernate.connection.characterEncoding", "utf8")
                .setProperty("hibernate.show_sql", String.valueOf(DatabaseConfig.SHOW_SQL));

        sessionFactory = configuration.buildSessionFactory();
        sessions = new ArrayList<>();
    }

    public Session getSession() {
        Session session = sessionFactory.openSession();
        sessions.add(session);
        return session;
    }

    public void cleanUpSessions() {
        sessions.removeAll(sessions.stream().filter(s -> !s.isOpen()).collect(Collectors.toList()));
    }

    public void saveToDB(Object obj) {
        synchronized (obj) {
            try (Session session = getSession()) {
                Transaction t = session.beginTransaction();
                session.saveOrUpdate(obj);
                t.commit();
            }
        }
        cleanUpSessions();
    }

    public void saveToDB(Collection<?> list) {
        list.forEach(this::saveToDB);
    }

    public void deleteFromDB(Object obj) {
        synchronized (obj) {
            try (Session session = getSession()) {
                Transaction t = session.beginTransaction();
                session.delete(obj);
                t.commit();
            }
        }
        cleanUpSessions();
    }

    public void closeSession(Session session) {
    }
}
