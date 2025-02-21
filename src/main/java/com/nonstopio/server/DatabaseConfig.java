package com.nonstopio.server;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import com.nonstopio.shared.Person;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Load database properties from file
                Properties dbProperties = new Properties();
                try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
                    if (input == null) {
                        throw new RuntimeException("Unable to find database.properties");
                    }
                    dbProperties.load(input);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load database properties", e);
                }

                Configuration configuration = new Configuration();

                // Hibernate settings using loaded properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, dbProperties.getProperty("db.driver"));
                settings.put(Environment.URL, dbProperties.getProperty("db.url"));
                settings.put(Environment.USER, dbProperties.getProperty("db.username"));
                settings.put(Environment.PASS, dbProperties.getProperty("db.password"));
                settings.put(Environment.DIALECT, dbProperties.getProperty("db.dialect"));
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Person.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
