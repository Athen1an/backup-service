package ru.athena.backup;

import ru.athena.backup.configuration.BackupProperties;

public class BackupApplication {

    public static void main(String[] args) {
        BackupProperties backupProperties = BackupProperties.init();

        System.out.println(backupProperties.getProperties());
    }
}
