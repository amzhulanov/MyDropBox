package ru.geekbrains.client;

/*
*Class for processing in/out message
*
* Processing all button from MainView
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ru.geekbrains.common.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import static ru.geekbrains.common.CommandMessage.*;

public class MainController implements Initializable {

    private final String CLIENT_STORAGE = "client_storage/";


    @FXML
    ListView<String> filesListClient;

    @FXML
    ListView<String> filesListServer;

    @FXML
    private Button logoutButton;

    @FXML
    private Label sessionLabel;

    public void initialize() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location);
        Network.start();

        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get(CLIENT_STORAGE + ((FileMessage) am).getUser() + "/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList(((FileMessage) am).getUser());
                    }
                    if (am instanceof FileListMessage) {
                        refreshServerFileList((FileListMessage) am);//
                    }
                    if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_SUCCESS_RESPONSE)) {
                        System.out.println("Не в том месте (MainController) получен ответ об успешной регистрации пользователя в базе");
                        regUser((CommandMessage) am);
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


        refreshLocalFilesList(sessionLabel.getText());//first refresh
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));//first refresh
    }


    //send to Server
    public void pressBtnSendFileToServer() throws IOException {
        Network.sendMsg(new FileMessage(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + filesListClient.getSelectionModel().selectedItemProperty().getValue()), sessionLabel.getText()));
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));
    }

    //download from Server
    public void pressBtnDownloadFromServer() {
        try {
            Network.sendMsg(new FileRequest(filesListServer.getSelectionModel().selectedItemProperty().getValue(), sessionLabel.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //reqest File list from Server for Client
    public void pressOnRequestFileListServerBtn() {
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));
    }

    public void pressOnRequestFileListClientBtn() {
        refreshLocalFilesList(sessionLabel.getText());
    }

    //read File list on Client
    private void refreshLocalFilesList(String user) {
        if (Platform.isFxApplicationThread()) {
            try {
                filesListClient.getItems().clear();
                Files.list(Paths.get(CLIENT_STORAGE + user + "/")).map(p -> p.getFileName().toString()).forEach(o -> filesListClient.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    filesListClient.getItems().clear();
                    Files.list(Paths.get(CLIENT_STORAGE + user + "/")).map(p -> p.getFileName().toString()).forEach(o -> filesListClient.getItems().add(o));
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
    public void pressBtnDeleteFromServer() {
        try {
            Network.sendMsg(new CommandMessage(FILE_DELETE, sessionLabel.getText(), new FileRequest(filesListServer.getSelectionModel().selectedItemProperty().getValue(), sessionLabel.getText())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));
    }

    //Delete File From Client and read Filelist from Client
    public void pressBtnDeleteFromClient() throws IOException {
        if (Files.exists(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + filesListClient.getSelectionModel().selectedItemProperty().getValue()))) {
            Files.delete(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + filesListClient.getSelectionModel().selectedItemProperty().getValue()));
        }
        refreshLocalFilesList(sessionLabel.getText());
    }

    public void initSessionID(final LoginManager loginManager, String sessionID) {
        sessionLabel.setText(sessionID);
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.logout();
            }
        });
    }

    private void regUser(CommandMessage am) {

    }
}
