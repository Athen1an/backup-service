package ru.athena.backup;

import ru.athena.backup.service.BackupRunner;

public class BackupApplication {

    public static void main(String[] args) {
        BackupRunner backupRunner = BackupRunner.init();
        backupRunner.run();
    }


}
