package com.noize.medicalcenter;

import com.noize.medicalcenter.util.alert.AlertSound;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
    private final AlertSound alertSound = new AlertSound();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent loadingRoot = FXMLLoader.load(getClass().getResource("/view/LoadingForm.fxml"));
        Scene loadingScene = new Scene(loadingRoot);
        stage.setTitle(" Loading...");
        stage.setResizable(false);
        stage.setScene(loadingScene);
        stage.show();

        Task<Scene> loadLoginFormTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {
//                alertSound.checkSounds(Sound.CONFIRM);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginForm.fxml"));
                Parent loginRoot = fxmlLoader.load();
                return new Scene(loginRoot);
            }
        };

        loadLoginFormTask.setOnSucceeded(event -> {
            Scene loginScene = loadLoginFormTask.getValue();
            stage.setTitle("Login Page");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/asset/icon/app_logo.png")));
            stage.setResizable(false);
            stage.setScene(loginScene);
            stage.centerOnScreen();
        });

        loadLoginFormTask.setOnFailed(event -> {
            System.err.println("Failed to load the login form.");
            loadLoginFormTask.getException().printStackTrace();
        });

        new Thread(loadLoginFormTask).start();
    }
}
