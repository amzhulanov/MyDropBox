package ru.geekbrains.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileListMessage extends AbstractMessage {

    private List<String> fileListName;
    //private byte[] data;

    public List<String> getListFilename() {
        return fileListName;
    }

    //public byte[] getData() {
      //  return data;
    //}

    public FileListMessage(Path path) throws IOException {
        Files.list(path).collect(Collectors.toList()).forEach(p->fileListName.add(p.getFileName().toString()));
        /*for (Path path1:Files.list(path).collect(Collectors.toList())) {
            fileListName.add(path1.getFileName().toString());
        }*/
    }
}
