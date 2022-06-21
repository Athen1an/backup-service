package ru.athena.backup.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BackupProperties {

    private static final String CONFIG_FILE_NAME = "backup.config";

    private final Properties properties;

    private BackupProperties(Properties properties) {
        this.properties = properties;
    }

    public static BackupProperties init() {
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(CONFIG_FILE_NAME);
            properties.load(fileReader);
        } catch (IOException e) {
            System.out.println("Catch IOException: " + e.getMessage());
        }
        return new BackupProperties(properties);
    }

    public Properties getProperties() {
        return properties;
    }
}
