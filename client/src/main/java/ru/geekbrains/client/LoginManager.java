package ru.geekbrains.client;
/*
*Class for demonstration all FormView
*
 */
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

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

    public void regUserShowScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/regUser.fxml")
            );
            scene.setRoot((Parent) loader.load());
            RegUserController controller =
                    loader.<RegUserController>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            System.out.println("regUserShowScreen ex=" + ex);
        }
    }

    public void alertReg(String title, String contextText, Alert.AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        alert.showAndWait();
    }
}
