package com.noize.medicalcenter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login/LoginForm.fxml"));
        stage.setTitle("Login Page");
        stage.setResizable(false);
        stage.getIcons().add(
                new Image(
                        getClass().getResourceAsStream("/asset/icon/app_logo.png")
                )
        );
        stage.setScene(new Scene(root));
        stage.show();
    }
}
