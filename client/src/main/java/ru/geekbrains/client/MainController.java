package ru.geekbrains.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import ru.geekbrains.common.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import static ru.geekbrains.common.CommandMessage.FILE_DELETE;
import static ru.geekbrains.common.CommandMessage.FILE_LIST_REQUEST;

public class MainController implements Initializable {

    private final String CLIENT_STORAGE="client_storage/";

    private String tfFileName;
    @FXML
    ListView<String> filesListClient;

    @FXML
    ListView<String> filesListServer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();


        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get(CLIENT_STORAGE + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                    if (am instanceof FileListMessage) {
                        refreshServerFileList((FileListMessage) am);//
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();

        refreshLocalFilesList();//first refresh
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST));//first refresh
    }

    public void pressBtnSendFileToServer() throws IOException {
        tfFileName= filesListClient.getSelectionModel().selectedItemProperty().getValue() ;
        System.out.println("tfFileName = "+tfFileName);
        Path path=Paths.get(CLIENT_STORAGE +tfFileName);
        System.out.println("pressBtnSendFileToServer = "+path.getFileName());
        Network.sendMsg(new FileMessage(path));
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST));
    }
    //download from Server
    public void pressBtnDownloadFromServer() {
        tfFileName= filesListServer.getSelectionModel().selectedItemProperty().getValue() ;
        if (tfFileName.length() > 0) {
            Network.sendMsg(new FileRequest(tfFileName));
            tfFileName = null;
        }
    }

    //reqest File list from Server for Client
    public void pressOnRequestFileListServerBtn() {
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST));
    }

    public void pressOnRequestFileListClientBtn() {
        refreshLocalFilesList();
    }

    //read File list on Client
    private void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                filesListClient.getItems().clear();
                Files.list(Paths.get(CLIENT_STORAGE)).map(p -> p.getFileName().toString()).forEach(o -> filesListClient.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    filesListClient.getItems().clear();
                    Files.list(Paths.get(CLIENT_STORAGE)).map(p -> p.getFileName().toString()).forEach(o -> filesListClient.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

//read File list on Server
    private void refreshServerFileList(FileListMessage flm) {
        if (Platform.isFxApplicationThread()) {
            filesListServer.getItems().clear();
            flm.getListFilename().forEach(o -> filesListServer.getItems().add(o.getFilename()));
        } else {
            Platform.runLater(() -> {
                filesListServer.getItems().clear();
                flm.getListFilename().forEach(o -> filesListServer.getItems().add(o.getFilename()));
            });
        }
    }
//Delete File From Server and read Filelist from Server
    public void pressBtnDeleteFromServer()  {
        tfFileName= filesListServer.getSelectionModel().selectedItemProperty().getValue() ;
        if (tfFileName.length() > 0) {
            Network.sendMsg(new CommandMessage(FILE_DELETE,new FileRequest(tfFileName)));
            tfFileName = null;
        }
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST));

    }
    //Delete File From Client and read Filelist from Client
    public void pressBtnDeleteFromClient() throws IOException {
        tfFileName= filesListClient.getSelectionModel().selectedItemProperty().getValue() ;
        if (Files.exists(Paths.get(CLIENT_STORAGE + tfFileName))) {
            Files.delete(Paths.get(CLIENT_STORAGE + tfFileName));
        }
        tfFileName = null;
        refreshLocalFilesList();

    }



}
