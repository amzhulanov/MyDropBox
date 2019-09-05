package ru.geekbrains.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginManager {
    private Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }

    public void logout() {
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/login.fxml")
            );
            scene.setRoot((Parent) loader.load());
            LoginController controller =
                    loader.<LoginController>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            System.out.println("showLoginScreen ex="+ex);
        }
    }

    private void showMainView(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main.fxml")
            );
            scene.setRoot((Parent) loader.load());
            MainController controller =
                    loader.<MainController>getController();
            controller.initSessionID(this, sessionID);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
