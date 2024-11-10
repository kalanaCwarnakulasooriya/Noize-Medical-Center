package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.alert.AlertSound;
import com.noize.medicalcenter.alert.Sound;
import com.noize.medicalcenter.dto.LoginFormDto;
import com.noize.medicalcenter.model.LoginFormModel;
import com.noize.medicalcenter.notification.AlertNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.ResultSet;

public class LoginFormController {
    private LoginFormModel loginFormModel = new LoginFormModel();
    private final AlertSound alertSound = new AlertSound();

    @FXML
    private ImageView alertImage;

    @FXML
    private ImageView alertImage2;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Hyperlink hyperFPwd;

    @FXML
    private Hyperlink hyperSignup;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private JFXPasswordField txtPwd;

    @FXML
    private JFXTextField txtUname;

    private void closeWindow(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    public void loadWindow(String path, boolean isLoading) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + path));
        Parent root = loader.load();
        Stage windows = new Stage();

        if(isLoading) {
            windows.initOwner(loginPane.getScene().getWindow());
            windows.getIcons().add(
                    new Image(
                            getClass().getResourceAsStream("/asset/icon/app_logo.png")
                    )
            );
            windows.setTitle("Admin Dashboard");

        }

        windows.setScene(new Scene(root));

        try {
            windows.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLogin(ActionEvent event) throws Exception {
        LoginFormDto loginFormDto = new LoginFormDto(txtUname.getText(), txtPwd.getText());
        System.out.println(loginFormDto.toString());
        ResultSet resultSet = loginFormModel.btnLogin(loginFormDto);
        System.out.println("Mee line ekt enwa");
        refeshLoginForm();
        try {
            if (resultSet == null || !BCrypt.checkpw(loginFormDto.getPassword(), resultSet.getString("Password"))) {
                alertSound.checkSounds(Sound.INVALID);
                new AlertNotification(" Login Failed",
                        "   Invalid username or password \n   please check and try again",
                        "unsuccess.png",
                        "OK"
                ).start();
            } else {
                closeWindow(event);
                alertSound.checkSounds(Sound.CONFIRM);
                new AlertNotification(" Login Successful",
                        "   Hi, " + resultSet.getString("Name") + " Welcome back to Noize Medical Center",
                        "success.png",
                        "OK"
                ).start();

                loadDashboards(resultSet.getInt(5), event);
                System.out.println("Role: " + resultSet.getInt(5));
            }
        } catch (Exception e) {
            System.err.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void loadDashboards(int role, ActionEvent event) throws IOException {
        closeWindow(event);

        switch (role) {
            case 1:
                //TODO: Add AdminDashboardFormController path for role 1
                break;
            case 2:
                loadWindow("dashboard/adminDashboardForm.fxml", true);
                break;
            default:
                System.out.println("Invalid role");
        }
    }

    @FXML
    void hyperFPwdOnAction(ActionEvent event) {
        navigateTo("/view/login/forgetPasswordForm.fxml");
    }

    @FXML
    void hyperSignupOnAction(ActionEvent event) {
        navigateTo("/view/signup/signupForm.fxml");
    }

    public void navigateTo(String fxmlPath) {
        try {
            loginPane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));
            load.prefWidthProperty().bind(loginPane.widthProperty());
            load.prefHeightProperty().bind(loginPane.heightProperty());
            loginPane.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new AlertNotification(
                    "Error",
                    "An error occurred while loading the form. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }
    public void refeshLoginForm() {
        txtUname.setText("");
        txtPwd.setText("");
    }
}

