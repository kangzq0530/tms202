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

package com.msemu.commons.database;

import com.msemu.core.configs.DatabaseConfig;
import com.msemu.core.startup.StartupComponent;
import org.atteo.classindex.ClassIndex;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
                .setProperty("hibernate.connection.url", "jdbc:mysql://" + DatabaseConfig.HOST + ":" + DatabaseConfig.PORT +
                        "/" + DatabaseConfig.DATABASE_NAME + "?autoReconnect=true&amp;useSSL=false")
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


    public void deleteFromDB(Collection<?> list) {
        list.forEach(this::deleteFromDB);
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

    public Object getObjFromDB(Class<?> clazz, int id) {
        Object o;
        try (Session session = getSession()) {
            Transaction t = session.beginTransaction();
            o = session.get(clazz, id);
            t.commit();
        }
        return o;
    }

}
