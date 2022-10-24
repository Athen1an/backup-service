package ru.athena.backup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyService {

    private static final Logger logger = Logger.getLogger(CopyService.class.getName());

    public void copy(Path sourceDirectory, Path targetDirectory) {
        File source = sourceDirectory.toFile();
        if (source.exists() && source.isDirectory()) {
            if (!targetDirectory.toFile().exists()) {
                try {
                    Files.createDirectory(targetDirectory);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error occurred while create target directory.", e);
                    throw new RuntimeException(e.getMessage());
                }
            }
            File[] files = source.listFiles();
            assert files != null;
            for (File file : files) {
                if (!file.isDirectory() && !file.isHidden()) {
                    try {
                        Files.copy(file.toPath(), Paths.get(targetDirectory.toString(), file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error occurred while copy file.", e);
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
            logger.log(Level.INFO, "Backup finished.");
        } else {
            logger.log(Level.WARNING, "Source directory does not exist, backup didn't take place.");
        }
    }
}
