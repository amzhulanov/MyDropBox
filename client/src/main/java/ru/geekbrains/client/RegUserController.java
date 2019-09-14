package ru.geekbrains.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.geekbrains.common.AbstractMessage;
import ru.geekbrains.common.CommandMessage;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.Exception.RegPasswordException;
import ru.geekbrains.common.Exception.ResourceException;
import ru.geekbrains.common.RegMessage;

import java.io.IOException;

import static ru.geekbrains.common.CommandMessage.*;

public class RegUserController {

    private RegUserManager regUserManager;
    private LoginManager loginManager;

    @FXML
    private TextField name;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordRepeat;

    @FXML
    private Button register;

    @FXML
    private Button cancel;

    public RegUserController() {
    }

    public void initManager(final LoginManager loginManager) {
        this.loginManager=loginManager;
        regUserManager=new RegUserManager();
        register.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                AbstractMessage am = null;
                try {
                    am = Network.sendLogin(new RegMessage(login.getText(), name.getText(), password.getText(), passwordRepeat.getText()));
                    if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_SUCCESS_RESPONSE)) {
                        regUserManager.createNewDir(login.getText());
                        loginManager.showLoginScreen();
                    } else if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_FAIL_RESPONSE)) {
                        errorReg(am);

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.showLoginScreen();
            }
        });
    }

    public void errorReg(AbstractMessage am) {
        if (((CommandMessage) am).getEx().equals(RegPasswordException.class)) {
            loginManager.alertReg("Error password", "Passwords must match", Alert.AlertType.WARNING);
        }
        if (((CommandMessage) am).getEx().equals(RegLoginException.class)) {
            loginManager.alertReg("Error registr", "This login is already registered", Alert.AlertType.WARNING);
        }
        if (((CommandMessage) am).getEx().equals(ResourceException.class)) {
            loginManager.alertReg("Error registr", "Unknown error", Alert.AlertType.WARNING);
        }
    }


}
