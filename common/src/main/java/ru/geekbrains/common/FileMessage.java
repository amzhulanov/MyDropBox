package ru.geekbrains.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private byte[] data;
    private String user;

    public String getFilename() {
        return filename;
    }

    public String getUser() {
        return user;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path,String user) throws IOException {
        this.filename = path.getFileName().toString();
        this.data = Files.readAllBytes(path);
        this.user=user;
    }

    public FileMessage(String filename, String user) {
        this.filename = filename;
        this.user = user;
    }
}
