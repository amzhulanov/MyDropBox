package ru.geekbrains.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.w3c.dom.ls.LSOutput;
import ru.geekbrains.common.AbstractMessage;
import ru.geekbrains.common.AuthMessage;
import ru.geekbrains.common.CommandMessage;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.Exception.RegPasswordException;
import ru.geekbrains.common.Exception.ResourceException;
import ru.geekbrains.common.RegMessage;

import java.io.IOException;

import static ru.geekbrains.common.CommandMessage.*;

public class RegUserController {

    private RegUserManager regUserManager;

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

        register.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                AbstractMessage am = null;
                System.out.println("Запускаю отправку пользователя на сервер логин=" + login.getText());
                try {
                    am = Network.sendLogin(new RegMessage(login.getText(), name.getText(), password.getText(), passwordRepeat.getText()));
                    System.out.println("Получен ответ от сервера");
                    if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_SUCCESS_RESPONSE)) {
                        System.out.println("Авторизация пройдена. Включаю показ экрана логина");
                        //sessionID = ((CommandMessage) am).getUser();
                        loginManager.showLoginScreen();
                    } else if (am instanceof CommandMessage && ((CommandMessage) am).getCommandMessage().equals(REG_FAIL_RESPONSE)) {
                        System.out.println("Авторизация провалена. Запускаю процедуру обработки ошибки");
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
            regUserManager.alertReg("Ошибка пароля", "Пароли должны совпадать", Alert.AlertType.WARNING);
        }
        if (((CommandMessage) am).getEx().equals(RegLoginException.class)) {
            regUserManager.alertReg("Ошибка регистрации", "Такой логин уже зарегистрирован", Alert.AlertType.WARNING);
        }
        if (((CommandMessage) am).getEx().equals(ResourceException.class)) {
            regUserManager.alertReg("Ошибка регистрации", "Просто ошибка", Alert.AlertType.WARNING);
        }
    }
}
