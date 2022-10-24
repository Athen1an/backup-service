package ru.athena.backup.service;

import ru.athena.backup.configuration.BackupSettings;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackupRunner {

    private static final Logger logger = Logger.getLogger(BackupRunner.class.getName());

    private final BackupSettings backupSettings;
    private final CopyService copyService;
    private final Scanner scanner;

    public static BackupRunner init() {
        BackupSettings backupSettings = BackupSettings.init();
        CopyService copyService = new CopyService();
        Scanner scanner = new Scanner(System.in);
        return new BackupRunner(backupSettings, copyService, scanner);
    }

    private BackupRunner(BackupSettings backupSettings, CopyService copyService, Scanner scanner) {
        this.backupSettings = backupSettings;
        this.copyService = copyService;
        this.scanner = scanner;

        logger.log(Level.INFO, "Application was running with configuration: {0}", backupSettings);
    }

    public void run() {
        logger.log(Level.INFO, getActionMessage());

        String action = scanner.next();
        switch (action) {
            case "1" -> logger.log(Level.INFO, "Application configuration: {0}", backupSettings);
            case "2" -> copyService.copy(backupSettings.getSourceDirectory(), backupSettings.getTargetDirectory());
            case "3" -> System.exit(0);
            default -> logger.log(Level.WARNING, "Action {0} not found.", action);
        }

        run();
    }

    private String getActionMessage() {
        return System.lineSeparator() + "Please, select next action: " +
                System.lineSeparator() + "1. Read configuration." +
                System.lineSeparator() + "2. Start backup." +
                System.lineSeparator() + "3. Exit program.";
    }

}
