package ru.athena.backup;

import ru.athena.backup.configuration.BackupSettings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BackupApplication {

    private static final Logger logger = Logger.getLogger(BackupApplication.class.getName());

    public static void main(String[] args) {
        BackupSettings backupSettings = BackupSettings.init();
        run(backupSettings);
    }

    private static void run(BackupSettings backupSettings) {
        logger.log(Level.INFO, "Application was running with configuration: {0}", backupSettings);
    }
}
