package ru.geekbrains.client;
/**
 * @author JAM amzhulanov@ya.ru
 * Client DropBox
 *
 * HomeWork geekbrains.ru
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainClient extends Application {
    public LoginManager loginManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new StackPane());
         loginManager = new LoginManager(scene);
        loginManager.showLoginScreen();

        primaryStage.setTitle("Box Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
