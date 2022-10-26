package ru.athena.backup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.athena.backup.configuration.BackupConstant.FOLDER_NAME_FORMATTER;

public class CopyService {

    private static final Logger logger = Logger.getLogger(CopyService.class.getName());

    public void copy(Path sourceDirectory, Path targetDirectory) {
        File source = sourceDirectory.toFile();
        if (source.exists() && source.isDirectory()) {
            Path backupDirectory = createBackupDirectory(targetDirectory);

            File[] files = Objects.requireNonNull(source.listFiles());
            for (File file : files) {
                copyFile(backupDirectory, file);
            }
            logger.log(Level.INFO, "Backup finished.");
        } else {
            logger.log(Level.WARNING, "Source directory does not exist, backup did not take place.");
        }
    }

    private void copyFile(Path targetDirectory, File file) {
        if (file.isDirectory() || file.isHidden()) {
            String copyWarnMsgTemplate = "File %s was not copy because it is hidden=%s or directory=%s";
            logger.log(Level.WARNING, String.format(copyWarnMsgTemplate, file.getName(), file.isHidden(), file.isDirectory()));
        } else {
            try {
                Files.copy(file.toPath(), Paths.get(targetDirectory.toString(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e, () -> String.format("Error occurred while copy file: %s", file.getName()));
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private Path createBackupDirectory(Path targetDirectory) {
        Path resolve = targetDirectory.resolve(FOLDER_NAME_FORMATTER.format(Instant.now()));
        if (resolve.toFile().exists()) return resolve;

        try {
            return Files.createDirectories(resolve);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> String.format("Error occurred while create backup directory: %s", resolve));
            throw new RuntimeException(e.getMessage());
        }
    }
}
