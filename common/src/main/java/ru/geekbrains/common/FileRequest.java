package ru.geekbrains.common;

public class FileRequest extends AbstractMessage {
    private String filename;
    private String user;

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename,String user) {
        this.filename = filename;
        this.user=user;
    }

    public String getUser() {
        return user;
    }
}
