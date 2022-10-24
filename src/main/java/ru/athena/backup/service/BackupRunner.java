package ru.athena.backup.service;

import ru.athena.backup.configuration.BackupSettings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BackupRunner {

    private static final Logger logger = Logger.getLogger(BackupRunner.class.getName());

    private final BackupSettings backupSettings;
    private final CopyService copyService;

    public static BackupRunner init() {
        BackupSettings backupSettings = BackupSettings.init();
        CopyService copyService = new CopyService();
        return new BackupRunner(backupSettings, copyService);
    }

    private BackupRunner(BackupSettings backupSettings, CopyService copyService) {
        this.backupSettings = backupSettings;
        this.copyService = copyService;

        logger.log(Level.INFO, "Application was running with configuration: {0}", backupSettings);
    }

    public void run() {
        copyService.copy(backupSettings.getSourceDirectory(), backupSettings.getTargetDirectory());
    }

}
