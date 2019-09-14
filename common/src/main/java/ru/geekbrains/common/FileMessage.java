package ru.geekbrains.common;

import ru.geekbrains.common.fileUtilities.FileUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String fileName;
    private byte[] data;
    private String user;

    public String getFileName() {
        return fileName;
    }

    public String getUser() {
        return user;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path,String user) throws IOException {
        this.fileName = path.getFileName().toString();
        this.data = Files.readAllBytes(path);
        this.user=user;
    }

    public FileMessage(String fileName, String user) {
        this.fileName = fileName;
        this.user = user;
    }

}
