package ru.athena.backup.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackupSettings {

    private static final Logger logger = Logger.getLogger(BackupSettings.class.getName());

    private static final String CONFIG_FILE_NAME = "backup.config";
    private static final String BACKUP_DIRECTORY = "backupDirectory";
    private static final String SOURCE_DIRECTORY = "sourceDirectory";

    private final Properties properties;
    private final Path backupDirectory;
    private final Path sourceDirectory;

    public static BackupSettings init() {
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(CONFIG_FILE_NAME);
            properties.load(fileReader);
        } catch (IOException e) {
            logger.log(Level.WARNING, CONFIG_FILE_NAME + " not found: " + e.getMessage());
        }
        return new BackupSettings(properties);
    }

    public Path getBackupDirectory() {
        return backupDirectory;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    private BackupSettings(Properties properties) {
        this.properties = properties;
        try {
            this.backupDirectory = initBackupDirectory(properties);
            this.sourceDirectory = initSourceDirectory(properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Incorrect jar path: " + e.getMessage());
        }
    }

    private Path initBackupDirectory(Properties properties) throws URISyntaxException {
        return properties.containsKey(BACKUP_DIRECTORY)
                ? Paths.get(properties.getProperty(BACKUP_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("backup");
    }

    private Path initSourceDirectory(Properties properties) throws URISyntaxException {
        return properties.containsKey(SOURCE_DIRECTORY)
                ? Paths.get(properties.getProperty(SOURCE_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
    }

    @Override
    public String toString() {
        return "BackupSettings{" +
                "properties=" + properties +
                ", backupDirectory='" + backupDirectory + '\'' +
                ", sourceDirectory='" + sourceDirectory + '\'' +
                '}';
    }
}
