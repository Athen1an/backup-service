package ru.athena.backup.service;

import ru.athena.backup.configuration.BackupSettings;

import java.util.Scanner;
import java.util.logging.Logger;

public class BackupRunner {

    private static final Logger logger = Logger.getLogger(BackupRunner.class.getName());

    private final BackupSettings backupSettings;
    private final CopyService copyService;
    private final FolderCountChecker folderCountChecker;
    private final Scanner scanner;

    public static BackupRunner withSettings(BackupSettings backupSettings) {
        CopyService copyService = new CopyService();
        FolderCountChecker folderCountChecker = new FolderCountChecker(backupSettings);
        Scanner scanner = new Scanner(System.in);
        return new BackupRunner(backupSettings, copyService, folderCountChecker, scanner);
    }

    private BackupRunner(BackupSettings backupSettings,
                         CopyService copyService,
                         FolderCountChecker folderCountChecker,
                         Scanner scanner) {
        this.backupSettings = backupSettings;
        this.copyService = copyService;
        this.folderCountChecker = folderCountChecker;
        this.scanner = scanner;
    }

    public void start() {
        logger.info(String.format("Application was running with configuration: %s", backupSettings));

        if (backupSettings.isForceBackup()) {
            runImmediately();
        } else {
            run();
        }
    }

    private void runImmediately() {
        logger.info("Backup running immediately without delete over backup folders");
        copyService.copy(backupSettings.getSourceDirectory(), backupSettings.getTargetDirectory());
    }

    private void run() {
        logger.info(getRunningMessage());

        String action = scanner.next();
        switch (action) {
            case "1" -> logger.info(String.format("Application configuration: %s", backupSettings));
            case "2" -> {
                copyService.copy(backupSettings.getSourceDirectory(), backupSettings.getTargetDirectory());
                folderCountChecker.checkAndDeleteBackupFoldersOverCount();
            }
            case "3" -> System.exit(0);
            default -> logger.warning(String.format("Action %s not found.", action));
        }

        run();
    }

    private String getRunningMessage() {
        return System.lineSeparator() + "Please, select next action: " +
                System.lineSeparator() + "1. Read configuration." +
                System.lineSeparator() + "2. Start backup." +
                System.lineSeparator() + "3. Exit program.";
    }

}
