package ru.athena.backup.service;

import ru.athena.backup.configuration.BackupConstant;
import ru.athena.backup.configuration.BackupSettings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FolderCountChecker {

    private static final Logger logger = Logger.getLogger(FolderCountChecker.class.getName());

    private final BackupSettings backupSettings;

    public FolderCountChecker(BackupSettings backupSettings) {
        this.backupSettings = backupSettings;
    }

    public void checkAndDeleteBackupFoldersOverCount() {
        File backupDirectory = backupSettings.getTargetDirectory().toFile();
        if (!backupDirectory.exists()) return;

        List<Path> collect = getBackupFolders(backupDirectory);
        deleteFolderOverCount(collect);
    }

    private List<Path> getBackupFolders(File backupDirectory) {
        return Arrays.stream(Objects.requireNonNull(backupDirectory.listFiles()))
                .filter(this::isNotHiddenDirectory)
                .filter(file -> file.getName().startsWith(BackupConstant.FOLDER_NAME_PREFIX))
                .map(File::toPath)
                .sorted()
                .collect(Collectors.toList());
    }

    private void deleteFolderOverCount(List<Path> backupFolders) {
        while (backupFolders.size() > backupSettings.getBackupFoldersCount()) {
            Path forRemove = backupFolders.remove(0);
            try {
                Files.walk(forRemove).sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                String warningMsgTemplate = "Folder %s was not deleted: %s, delete operation aborted";
                logger.warning(String.format(warningMsgTemplate, forRemove.toFile().getName(), e.getMessage()));
                break;
            }
        }
    }

    private boolean isNotHiddenDirectory(File file) {
        return file.isDirectory() && !file.isHidden();
    }
}
