package ru.geekbrains.client;
/*
 *Demonstration alert messsage
 */


import javafx.scene.control.Alert;
import ru.geekbrains.common.NewDirectory;

public class RegUserManager {
    private String CLIENT_STORAGE = "client/client_storage/";





    void createNewDir(String login){
        NewDirectory newDirectory=new NewDirectory();
        newDirectory.createDir(CLIENT_STORAGE,login);
    }


}
