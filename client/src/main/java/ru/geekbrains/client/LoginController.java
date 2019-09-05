package ru.geekbrains.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.geekbrains.common.*;

import java.io.IOException;
import static ru.geekbrains.common.CommandMessage.*;


public class LoginController {

    public String sessionID = null;

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    public LoginController() {
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void initManager(final LoginManager loginManager) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                try {
                    authorize();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (sessionID != null) {
                    loginManager.authenticated(sessionID);
                    sessionID = null;
                }
            }
        });
    }

    private void authorize() throws IOException, ClassNotFoundException {
        AbstractMessage am =Network.sendLogin(new AuthMessage(user.getText(), password.getText()));
        if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(AUTH_SUCCESS_RESPONSE)) {
            sessionID = ((CommandMessage) am).getUser();
        } else if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(AUTH_FAIL_RESPONSE)) {
            sessionID=null;
        }

    }
}
