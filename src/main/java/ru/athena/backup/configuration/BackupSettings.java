package ru.athena.backup.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class BackupSettings {

    private static final Logger logger = Logger.getLogger(BackupSettings.class.getName());

    private static final String CONFIG_FILE_NAME = "backup.config";
    private static final String INIT_CONFIG_MSG = "backup.config.init.message";
    private static final String BACKUP_TARGET_DIRECTORY = "backup.directory.target";
    private static final String BACKUP_SOURCE_DIRECTORY = "backup.directory.source";
    private static final String BACKUP_FOLDERS_COUNT = "backup.folders.count";
    private static final String BACKUP_FORCE = "backup.force";

    private final Properties properties;
    private final Path targetDirectory;
    private final Path sourceDirectory;
    private final Integer backupFoldersCount;
    private final Boolean isForceBackup;

    public static BackupSettings init() {
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader(CONFIG_FILE_NAME);
            properties.load(fileReader);
        } catch (IOException e) {
            logger.warning(String.format("Error occurred while try to read config file with message: %s", e.getMessage()));
            properties.setProperty(INIT_CONFIG_MSG, "configuration file was not loaded, default settings will be used");
        }
        return new BackupSettings(properties);
    }

    private BackupSettings(Properties properties) {
        this.properties = properties;
        try {
            this.targetDirectory = initBackupDirectory();
            this.sourceDirectory = initSourceDirectory();
            this.backupFoldersCount = Integer.valueOf(properties.getProperty(BACKUP_FOLDERS_COUNT, "3"));
            this.isForceBackup = Boolean.valueOf(properties.getProperty(BACKUP_FORCE, "false"));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Incorrect jar path: " + e.getMessage());
        }
    }

    public Path getTargetDirectory() {
        return targetDirectory;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Integer getBackupFoldersCount() {
        return backupFoldersCount;
    }

    public Boolean isForceBackup() {
        return isForceBackup;
    }

    private Path initBackupDirectory() throws URISyntaxException {
        return this.properties.containsKey(BACKUP_TARGET_DIRECTORY)
                ? Paths.get(properties.getProperty(BACKUP_TARGET_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().resolve("backup");
    }

    private Path initSourceDirectory() throws URISyntaxException {
        return this.properties.containsKey(BACKUP_SOURCE_DIRECTORY)
                ? Paths.get(properties.getProperty(BACKUP_SOURCE_DIRECTORY))
                : Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
    }

    @Override
    public String toString() {
        return properties.getProperty(INIT_CONFIG_MSG, "") +
                System.lineSeparator() + "-------------------------" +
                System.lineSeparator() + "BackupSettings:" +
                System.lineSeparator() + BACKUP_TARGET_DIRECTORY + "='" + targetDirectory + '\'' +
                System.lineSeparator() + BACKUP_SOURCE_DIRECTORY + "='" + sourceDirectory + '\'' +
                System.lineSeparator() + BACKUP_FOLDERS_COUNT + "='" + backupFoldersCount + '\'' +
                System.lineSeparator() + BACKUP_FORCE + "='" + isForceBackup + '\'' +
                System.lineSeparator() + "-------------------------";
    }
}
