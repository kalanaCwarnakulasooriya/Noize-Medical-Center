package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.util.CheckRegex;
import com.noize.medicalcenter.util.alert.AlertSound;
import com.noize.medicalcenter.util.alert.Sound;
import com.noize.medicalcenter.dto.ForgetPasswordFormDto;
import com.noize.medicalcenter.util.SendGmail;
import com.noize.medicalcenter.model.ForgetPasswordFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

public class ForgetPasswordFormController {
    static int count = 3;
    ForgetPasswordFormDto forgetPwdDto;
    private final AlertSound alertSound = new AlertSound();

    @FXML
    private JFXButton btnGetData;

    @FXML
    private JFXButton btnGetOtpEmail;

    @FXML
    private JFXButton btnGetOtpPhone;

    @FXML
    private JFXButton btnNewResentPwd;

    @FXML
    private JFXButton btnResetPwd;

    @FXML
    private AnchorPane forgetPwdPane;

    @FXML
    private TextField txtFEmail;

    @FXML
    private TextField txtFPhone;

    @FXML
    private JFXTextField txtFUname;

    @FXML
    private JFXTextField txtOTP;

    @FXML
    private JFXPasswordField txtNewPwd;

    @FXML
    private JFXPasswordField txtConfirmPwd;

    @FXML
    private ImageView UImage;

    @FXML
    private ImageView otpImage;

    @FXML
    private ImageView imgHead;

    @FXML
    private Label lblHead;

    @FXML
    private ImageView imgFales;

    @FXML
    private Label lblFalse;

    @FXML
    private ImageView imgPwd1;

    @FXML
    private ImageView imgPwdI2;

    @FXML
    void btnGetDataOnAction(ActionEvent event) throws SQLException {
        forgetPwdDto = ForgetPasswordFormModel.getUserData(txtFUname.getText());
        if (forgetPwdDto == null) {
            new AlertNotification(
                    "User Not Found ! ",
                    "Please enter a valid username and try again",
                    "unsuccess.png",
                    "OK"
            ).start();
        }else {
            txtFPhone.setText(forgetPwdDto.getPhone());
            txtFEmail.setText(forgetPwdDto.getEmail());
            btnGetOtpEmail.setDisable(false);
            btnGetOtpPhone.setDisable(false);
        }
    }

    @FXML
    void btnGetOtpEmailOnAction(ActionEvent event) {
        count--;
        if (count > 0) {
            btnGetOtpEmail.setDisable(true);
            btnGetOtpPhone.setDisable(true);
            txtOTP.setDisable(false);
            btnResetPwd.setDisable(false);

            int otp = 10000 +new Random().nextInt(90000);
            txtOTP.setText(String.valueOf(otp));
            System.out.println(otp);
            forgetPwdDto.setOtp(otp);

            SendGmail.sendEmail(txtFEmail.getText(), String.valueOf(forgetPwdDto.getOtp()));

            new AlertNotification(
                    "OTP Send Successfully ! ",
                    "OTP send to your registered email \n Please check your email: " + txtFEmail.getText(),
                    "success.png",
                    "OK"
            ).start();
        }else {
            new AlertNotification(
                    "Action Required ! ",
                    "An issue occurred while sending OTP \n Please try again",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }

    @FXML
    void btnGetOtpPhoneOnAction(ActionEvent event) {

    }

    @FXML
    public void btnNewResetPwdOnAction(ActionEvent actionEvent) throws SQLException {
        if (txtNewPwd.getText().equals(txtConfirmPwd.getText()) && !txtNewPwd.getText().isEmpty() && !txtConfirmPwd.getText().isEmpty()) {
            String newPasswd = BCrypt.hashpw(txtNewPwd.getText(), BCrypt.gensalt());
            Boolean isChangeUserPW = ForgetPasswordFormModel.isChangedUserPassword(forgetPwdDto, newPasswd);

            navigateTo("/view/LoginForm.fxml");
            alertSound.checkSounds(Sound.CONFIRM);
            new AlertNotification(
                    "Password Reset Successfully ! ",
                    "Your password has been reset successfully ! \n Please enter with your new password and login again",
                    "success.png",
                    "OK"
            ).start();
        }else {
            alertSound.checkSounds(Sound.INVALID);
            new AlertNotification(
                    "Password Reset Failed ! ",
                    "Password do not match \n Please make sure that your password is correct and try again",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }

    @FXML
    void btnResetPwdOnAction(ActionEvent event) {
        if (txtOTP.getText().equals(String.valueOf(forgetPwdDto.getOtp()))) {
            txtFUname.setVisible(false);
            btnResetPwd.setVisible(false);
            txtFPhone.setVisible(false);
            txtFEmail.setVisible(false);
            btnGetOtpPhone.setVisible(false);
            btnGetOtpEmail.setVisible(false);
            txtOTP.setVisible(false);
            btnGetData.setVisible(false);
            otpImage.setVisible(false);
            UImage.setVisible(false);
            lblHead.setVisible(false);
            imgHead.setVisible(false);

            txtNewPwd.setVisible(true);
            txtConfirmPwd.setVisible(true);
            btnNewResentPwd.setVisible(true);
            lblFalse.setVisible(true);
            imgFales.setVisible(true);
            imgPwd1.setVisible(true);
            imgPwdI2.setVisible(true);

            new AlertNotification(
                    "OTP Verified ! & Password Reset Successfully ! ",
                    "Your password has been reset successfully ! \n Please enter with your new password and login again",
                    "success.png",
                    "OK"
            ).start();
        }else {
            new AlertNotification(
                    "Invalid OTP ! ",
                    "Please enter a valid OTP and try again",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }

    public void backOnClicked(MouseEvent mouseEvent) {
        forgetPwdPane.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/LoginForm.fxml")));
            forgetPwdPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateTo(String fxmlPath) {
        try {
            forgetPwdPane.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));
            load.prefWidthProperty().bind(forgetPwdPane.widthProperty());
            load.prefHeightProperty().bind(forgetPwdPane.heightProperty());
            forgetPwdPane.getChildren().add(load);
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

    public void onPwd2Release(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtConfirmPwd.getText())) {
            txtConfirmPwd.setStyle("-fx-text-fill: green;");
        } else {
            txtConfirmPwd.setStyle("-fx-text-fill: red;");
        }
    }

    public void onPwdRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtNewPwd.getText())) {
            txtNewPwd.setStyle("-fx-text-fill: green;");
        } else {
            txtNewPwd.setStyle("-fx-text-fill: red;");
        }
    }

    public void onOtpRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtOTP.getText())) {
            txtOTP.setStyle("-fx-text-fill: green;");
        } else {
            txtOTP.setStyle("-fx-text-fill: red;");
        }
    }

    public void onUnameRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("username", txtFUname.getText())) {
            txtFUname.setStyle("-fx-text-fill: green;");
        } else {
            txtFUname.setStyle("-fx-text-fill: red;");
        }
    }
}
