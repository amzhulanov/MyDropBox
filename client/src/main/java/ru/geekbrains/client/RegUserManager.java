package ru.geekbrains.client;

import javafx.scene.control.Alert;

public class RegUserManager {


    public void alertReg(String title, String contextText, Alert.AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);

        alert.showAndWait();
    }


}
