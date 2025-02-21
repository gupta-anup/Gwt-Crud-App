package com.nonstopio.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize Hibernate SessionFactory
        DatabaseConfig.getSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Close the SessionFactory when the application is shutting down
        if (DatabaseConfig.getSessionFactory() != null) {
            DatabaseConfig.getSessionFactory().close();
        }
    }
}