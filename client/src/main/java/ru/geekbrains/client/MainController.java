package ru.geekbrains.client;

/*
 *Class for processing in/out message
 *
 * Processing all button from MainView
 */

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.geekbrains.common.*;
import ru.geekbrains.common.fileUtilities.FileUser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ResourceBundle;

import static ru.geekbrains.common.CommandMessage.*;

public class MainController implements Initializable {

    private String CLIENT_STORAGE = "client/client_storage/";

    private ObservableList<FileUser> filesListClient = FXCollections.observableArrayList();
    private ObservableList<FileUser> filesListServer = FXCollections.observableArrayList();

    @FXML
    private TableView<FileUser> clientFiles;
    @FXML
    private TableColumn<FileUser, String> fileNameColumnClient;
    @FXML
    private TableColumn<FileUser, Number> fileSizeColumnClient;
    @FXML
    private TableColumn<FileUser, String> fileDateCreateColumnClient;


    @FXML
    private TableView<FileUser> serverFiles;
    @FXML
    private TableColumn<FileUser, String> fileNameColumnServer;
    @FXML
    private TableColumn<FileUser, Number> fileSizeColumnServer;
    @FXML
    private TableColumn<FileUser, String> fileDateCreateColumnServer;


    @FXML
    private Button logoutButton;

    @FXML
    private Label sessionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        fileNameColumnClient.setCellValueFactory((cellData -> cellData.getValue().fileNameProperty()));
        fileSizeColumnClient.setCellValueFactory((cellData -> (LongProperty) cellData.getValue().fileSizeProperty()));
        fileDateCreateColumnClient.setCellValueFactory((cellData -> cellData.getValue().fileDateCreateProperty()));

        fileNameColumnServer.setCellValueFactory((cellData -> cellData.getValue().fileNameProperty()));
        fileSizeColumnServer.setCellValueFactory((cellData -> (LongProperty) cellData.getValue().fileSizeProperty()));
        fileDateCreateColumnServer.setCellValueFactory((cellData -> cellData.getValue().fileDateCreateProperty()));

        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get(CLIENT_STORAGE + ((FileMessage) am).getUser() + "/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList(((FileMessage) am).getUser());
                    }
                    if (am instanceof FileListMessage) {
                        refreshServerFileList((FileListMessage) am);//
                    }
                    if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_SUCCESS_RESPONSE)) {
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

        //TODO настроить обновление списка файлов при запуске приложения
        // refreshLocalFilesList(sessionLabel.getText());//first refresh
        // Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));//first refresh
    }

    private void setColumns() {
        fileNameColumnClient.setCellValueFactory(new PropertyValueFactory<FileUser, String>("Имя"));
        fileSizeColumnClient.setCellValueFactory(new PropertyValueFactory<FileUser, Number>("Размер"));
        fileDateCreateColumnClient.setCellValueFactory(new PropertyValueFactory<FileUser, String>("Дата"));
    }

    //send to Server
    public void pressBtnSendFileToServer() throws IOException {
        Network.sendMsg(new FileMessage(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + selectedFileClient())
                , sessionLabel.getText()));
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));
    }

    //download from Server
    public void pressBtnDownloadFromServer() {
        try {
            Network.sendMsg(new FileRequest(selectedFileServer(), sessionLabel.getText()));
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
                readForRefreshClient(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    readForRefreshClient(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void readForRefreshClient(String user) throws IOException {
        File dir = new File(CLIENT_STORAGE + user + "/");
        if (dir != null) {//TODO обработать ошибку отсутствия файлов в папке
            filesListClient.clear();
            for (File p : dir.listFiles()) {//TODO обработать вероятность NPE
                Path path = Paths.get(CLIENT_STORAGE + user + "/" + p.getName());
                BasicFileAttributes attr = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
                filesListClient.addAll(new FileUser(p.getName()
                        , attr.size()
                        , attr.creationTime().toString()
                ));
            }
            clientFiles.setItems(filesListClient);
            clientFiles.refresh();
        }

    }

    private void readForRefreshServer(FileListMessage flm) throws IOException {
        filesListServer.clear();
        for (FileAbout fileAbout : flm.getListFilename()) {
            filesListServer.addAll(new FileUser(fileAbout.getFileName()
                    , fileAbout.getFileSize()
                    , fileAbout.getFileDateCreate()
            ));
        }
        serverFiles.setItems(filesListServer);
        serverFiles.refresh();//TODO проверить необходимость refresh
    }

    //read File list on Server
    private void refreshServerFileList(FileListMessage flm)  {
        if (Platform.isFxApplicationThread()) {
            try {
                readForRefreshServer(flm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    readForRefreshServer(flm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //Delete File From Server and read Filelist from Server
    public void pressBtnDeleteFromServer() {
        try {
            Network.sendMsg(new CommandMessage(FILE_DELETE, sessionLabel.getText(), new FileRequest(selectedFileServer(), sessionLabel.getText())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Network.sendMsg(new CommandMessage(FILE_LIST_REQUEST, sessionLabel.getText()));
    }

    //Delete File From Client and read Filelist from Client
    public void pressBtnDeleteFromClient() throws IOException {
        if (Files.exists(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + selectedFileClient()))) {
            Files.delete(Paths.get(CLIENT_STORAGE + sessionLabel.getText() + "/" + selectedFileClient()));
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

    public ObservableList<FileUser> getFileUser() {
        return filesListClient;
    }

    private String selectedFileClient() {
        return clientFiles.getSelectionModel().getSelectedItem().getFileName();
    }


    private String selectedFileServer() {
        return serverFiles.getSelectionModel().getSelectedItem().getFileName();
    }
}
