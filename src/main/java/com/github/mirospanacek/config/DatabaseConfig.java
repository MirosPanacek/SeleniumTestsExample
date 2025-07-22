package com.github.mirospanacek.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Logger LOG =
            LoggerFactory.getLogger(DatabaseConfig.class);
    private static  final String PROPERTIES_FILE_NAME = "db.properties";
    private String url = "";
    private String password = "";
    private String user = "";

    public DatabaseConfig() throws IOException {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE_NAME);

        if (inputStream == null) {
            throw new IOException("File app.properties not found on classpath.");
        }

        Properties properties = new Properties();
        properties.load(inputStream);
        url = properties.getProperty("url");
        password = properties.getProperty("password");
        user = properties.getProperty("user");
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "url='" + url + '\'' +
                ", password='" + password + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
