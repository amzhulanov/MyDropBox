package ru.geekbrains.common;

import java.io.File;
import java.io.Serializable;

public class FileAbout implements Serializable {

    private File file;

    private String filename;

    private long filesize;

    public String getFilename() {
        return filename;
    }

    public FileAbout(File file, String filename, long filesize) {
        this.file = file;
        this.filename = filename;
        this.filesize = filesize;
    }
}
