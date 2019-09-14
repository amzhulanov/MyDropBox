package ru.geekbrains.common.fileUtilities;

import javafx.beans.property.*;

public class FileUser {

    private final StringProperty fileName;

    private final LongProperty fileSize;

    private StringProperty fileDateCreate;

    public FileUser(String fileName, Long fileSize, String fileDateCreate) {
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleLongProperty(fileSize);
        this.fileDateCreate = new SimpleStringProperty(fileDateCreate);
    }


    public String getFileName() {
        return fileName.get();
    }
    public void setFileName(String fileName) {
        this.fileName.set( fileName);
    }
    public StringProperty fileNameProperty(){
        return fileName;
    }


    public Number getFileSize() {
        return fileSize.get();
    }
    public void setFileSize(Long fileSize) {
        this.fileSize.set(fileSize);
    }
    public LongProperty fileSizeProperty(){
        return fileSize;
    }


    public String getFileDateCreate() {
        return fileDateCreate.get();
    }
    public void setFileDateCreate(String fileDateCreate) {
        this.fileDateCreate.set(fileDateCreate);
    }
    public StringProperty fileDateCreateProperty(){
        return fileDateCreate;
    }
}
