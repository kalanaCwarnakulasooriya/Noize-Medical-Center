package com.noize.medicalcenter.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.util.ImageUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.util.CheckRegex;
import com.noize.medicalcenter.util.UserIdQrEncryption;
import com.noize.medicalcenter.util.alert.AlertSound;
import com.noize.medicalcenter.util.alert.Sound;
import com.noize.medicalcenter.dto.LoginFormDto;
import com.noize.medicalcenter.model.LoginFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        refeshLoginForm();
        try {
            if (resultSet == null || !BCrypt.checkpw(loginFormDto.getPassword(), resultSet.getString("Password"))) {
                alertSound.checkSounds(Sound.INVALID);
                new AlertNotification(
                        " Login Failed",
                        "   Invalid username or password \n   please check and try again",
                        "unsuccess.png",
                        "OK"
                ).start();
            } else {
                closeWindow(event);
                alertSound.checkSounds(Sound.CONFIRM);
                new AlertNotification(
                        " Login Successful",
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
                loadWindow("adminDashboardForm.fxml", true);
                break;
            case 2:
                loadWindow("adminDashboardForm.fxml", true);
                break;
            default:
                System.out.println("Invalid role");
        }
    }

    @FXML
    void hyperFPwdOnAction(ActionEvent event) {
        navigateTo("/view/forgetPasswordForm.fxml");
    }

    @FXML
    void hyperSignupOnAction(ActionEvent event) {
        navigateTo("/view/signupForm.fxml");
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
                    "Error Message",
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

    public void onUnameRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("username", txtUname.getText())) {
            txtUname.setStyle("-fx-text-fill: green;");
        } else {
            txtUname.setStyle("-fx-text-fill: red;");
        }
    }

    public void onPwdRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtPwd.getText())) {
            txtPwd.setStyle("-fx-text-fill: green;");
        } else {
            txtPwd.setStyle("-fx-text-fill: red;");
        }
    }

    public void qrCodeScanOnAction(ActionEvent event) throws Exception {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setImageSizeDisplayed(true);

        webcam.open();

        JFrame window = new JFrame("QR Code Scanner");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        String qrCode = "8vQiEnkR3IJfLNZ2G1eQrg==";
        while (window.isVisible()){
            BufferedImage image = webcam.getImage();
            String filename = "selfie.jpg";

            ImageIO.write(image, ImageUtils.FORMAT_JPG, new File("selfie.jpg"));

            qrCode = QrCodeFormController.readQRCodeFromFile("selfie.jpg");
            System.out.println(qrCode);
            Thread.sleep(1500);
            if(qrCode != null){
                webcam.close();
                window.dispose();
                break;
            }
        }
        try {
            alertSound.checkSounds(Sound.CONFIRM);
            webcam.close();
        } catch (Exception e) {
            alertSound.checkSounds(Sound.INVALID);
            throw new RuntimeException(e);
        }
        if (qrCode == null) {
            qrCode = "8vQiEnkR3IJfLNZ2G1eQrg==";
        }
        System.out.println(UserIdQrEncryption.decrypt(qrCode));
        loadDashboards(Integer.parseInt(UserIdQrEncryption.decrypt(qrCode)), event);
    }
}

