package ru.geekbrains.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileListMessage extends AbstractMessage {

    private List<FileAbout> fileListName=new ArrayList<>();

    public List<FileAbout> getListFilename() {
        return fileListName;
    }

    public FileListMessage(Path path) throws IOException {
        for (Object str:Files.list(path).map(Path::toFile).toArray()) {
            File file=new File(str.toString());
            BasicFileAttributes attr = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();

            fileListName.add(new FileAbout(file,file.getName(),file.length(),attr.creationTime().toString()));
        }
    }
}
