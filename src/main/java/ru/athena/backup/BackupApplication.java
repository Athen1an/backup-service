package ru.athena.backup;

import ru.athena.backup.configuration.BackupSettings;

public class BackupApplication {

    public static void main(String[] args) {
        BackupSettings backupSettings = BackupSettings.init();
    }
}
