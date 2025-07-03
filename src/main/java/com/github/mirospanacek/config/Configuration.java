package com.github.mirospanacek.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Logger LOG =
            LoggerFactory.getLogger(Configuration.class);
    private FileReader fileReader;
    Properties properties;
    /**
     * Maven creates the file
     */
    private static final String MAVEN_PROPERTIES_FILE = "app.properties";

    public Configuration() throws IOException {
        LOG.info(getClass().getClassLoader().toString());
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(MAVEN_PROPERTIES_FILE);
        if (inputStream == null) {
            throw new IOException("File app.properties not found on classpath (target/test-classes).");
        }
        properties.load(inputStream);

    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public String getBrowser() {
        return properties.getProperty("browser", "");
    }

    public boolean getInspection() {
        String inspection = properties.getProperty("inspection", "false");
        return Boolean.parseBoolean(inspection);
    }
}
