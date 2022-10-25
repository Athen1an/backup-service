package ru.athena.backup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyService {

    private static final Logger logger = Logger.getLogger(CopyService.class.getName());

    public void copy(Path sourceDirectory, Path targetDirectory) {
        File source = sourceDirectory.toFile();
        if (source.exists() && source.isDirectory()) {
            createDirectory(targetDirectory);

            File[] files = Objects.requireNonNull(source.listFiles());
            for (File file : files) {
                copyFile(targetDirectory, file);
            }
            logger.log(Level.INFO, "Backup finished.");
        } else {
            logger.log(Level.WARNING, "Source directory does not exist, backup didn't take place.");
        }
    }

    private void copyFile(Path targetDirectory, File file) {
        if (file.isDirectory() || file.isHidden()) {
            Object[] loggingParams = {file.getName(), file.isHidden(), file.isDirectory()};
            logger.log(Level.FINE, "File {0} wasn't copy because it's hidden={1} or directory={2}", loggingParams);
        } else {
            try {
                Files.copy(file.toPath(), Paths.get(targetDirectory.toString(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error occurred while copy file.", e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private void createDirectory(Path targetDirectory) {
        if (targetDirectory.toFile().exists()) return;

        try {
            Files.createDirectory(targetDirectory);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while create target directory.", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
