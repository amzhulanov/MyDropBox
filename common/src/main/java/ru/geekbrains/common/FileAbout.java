package ru.geekbrains.common;

import java.io.File;
import java.io.Serializable;

public class FileAbout implements Serializable {

    private File file;

    private String fileName;

    private long fileSize;

    private String fileDateCreate;

    public String getFileDateCreate() {
        return fileDateCreate;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public FileAbout(File file, String fileName, long fileSize,String fileDateCreate) {
        this.file = file;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDateCreate=fileDateCreate;
    }
}
