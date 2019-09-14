package ru.geekbrains.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewDirectory {


    public boolean createDir(String defPath, String directory) {
        final Path path= Paths.get(defPath + "/" + directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                return true;
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return false;
    }
}
