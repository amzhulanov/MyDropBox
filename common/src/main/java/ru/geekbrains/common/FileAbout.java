package ru.geekbrains.common;

import java.io.File;
import java.io.Serializable;

public class FileAbout implements Serializable {

    private File file;

    private String filename;

    private long filesize;

    public File getFileAbout() {
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public FileAbout(File file, String filename, long filesize) {
        this.file = file;
        this.filename = filename;
        this.filesize = filesize;
    }
}
