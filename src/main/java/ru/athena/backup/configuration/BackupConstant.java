package ru.athena.backup.configuration;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class BackupConstant {

    private BackupConstant() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public static final String FOLDER_NAME_PREFIX = "backup_";
    public static final DateTimeFormatter FOLDER_NAME_FORMATTER = DateTimeFormatter
            .ofPattern(String.format("'%s'dd.MM.yy'_'kk-mm-ss", FOLDER_NAME_PREFIX))
            .withZone(ZoneId.systemDefault());
}
