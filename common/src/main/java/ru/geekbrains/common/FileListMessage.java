package ru.geekbrains.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileListMessage extends AbstractMessage {

    private List<FileAbout> fileListName=new ArrayList<>();

    public List<FileAbout> getListFilename() {
        return fileListName;
    }

    public FileListMessage(Path path) throws IOException {
        //fileListName=null;
       // Files.list(path).map(Path::toFile)
        for (Object str:Files.list(path).map(Path::toFile).toArray()) {
            File file=new File(str.toString());
            fileListName.add(new FileAbout(file,file.getName(),file.length()));
            System.out.println(str.toString());
            //fileListName.add(str.toString());
        }
        //Files.list(path).collect(Collectors.toList()).forEach(p->fileListName.add(p.getFileName().toString()));
        //fileListName=Files.list(path).collect(Collectors.toList());
       // fileListName= Files.list(path).map(Path::toFile).map(FileAbout::new).collect(Collectors.toList());
    }
}
