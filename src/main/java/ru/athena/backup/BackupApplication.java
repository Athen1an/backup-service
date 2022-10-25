package ru.athena.backup;

import ru.athena.backup.configuration.BackupSettings;
import ru.athena.backup.service.BackupRunner;

public class BackupApplication {

    public static void main(String[] args) {
        BackupSettings backupSettings = BackupSettings.init();

        BackupRunner.withSettings(backupSettings).run();
    }

}
