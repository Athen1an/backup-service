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
    private static final String INIT_CONFIG_MSG = "backup.config.init.message";
    private static final String TARGET_DIRECTORY = "backup.directory.target";
    private static final String SOURCE_DIRECTORY = "backup.directory.source";

    private final Properties properties;
    private final Path backupDirectory;
    private final Path sourceDirectory;

    public static BackupSettings init() {
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(CONFIG_FILE_NAME);
            properties.load(fileReader);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error occurred while try to read config file with message: " + e.getMessage());
            properties.setProperty(INIT_CONFIG_MSG, "configuration file was not loaded, default settings will be used");
        }
        return new BackupSettings(properties);
    }

    private BackupSettings(Properties properties) {
        this.properties = properties;
        try {
            this.backupDirectory = initBackupDirectory();
            this.sourceDirectory = initSourceDirectory();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Incorrect jar path: " + e.getMessage());
        }
    }

    public Path getBackupDirectory() {
        return backupDirectory;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    private Path initBackupDirectory() throws URISyntaxException {
        return this.properties.containsKey(TARGET_DIRECTORY)
                ? Paths.get(properties.getProperty(TARGET_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("backup");
    }

    private Path initSourceDirectory() throws URISyntaxException {
        return this.properties.containsKey(SOURCE_DIRECTORY)
                ? Paths.get(properties.getProperty(SOURCE_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
    }

    @Override
    public String toString() {
        return properties.getProperty(INIT_CONFIG_MSG, "") +
                System.lineSeparator() + "-------------------------" +
                System.lineSeparator() + "BackupSettings:" +
                System.lineSeparator() + "backupDirectory='" + backupDirectory + '\'' +
                System.lineSeparator() + "sourceDirectory='" + sourceDirectory + '\'' +
                System.lineSeparator() + "-------------------------";
    }
}
