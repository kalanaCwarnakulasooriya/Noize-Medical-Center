package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.util.CheckRegex;
import com.noize.medicalcenter.util.alert.Sound;
import com.noize.medicalcenter.dto.SignupFormDto;
import com.noize.medicalcenter.model.SignupFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import com.noize.medicalcenter.util.alert.AlertSound;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignupFormController implements Initializable {

    private final SignupFormModel signupFormModel = new SignupFormModel();
    private final AlertSound alertSound = new AlertSound();

    @FXML
    private JFXButton btnSignup;

    @FXML
    private Hyperlink hyperLogin;

    @FXML
    private AnchorPane signupPane;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXPasswordField txtPwd;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPhone;

    @FXML
    private JFXPasswordField txtConformPwd;

    @FXML
    private JFXTextField txtUname;

    @FXML
    private JFXComboBox<String> comRole;

    @FXML
    void btnSignup(ActionEvent event) throws SQLException, IOException {
        String name = txtName.getText();
        String contactNumber = txtPhone.getText();
        String confirmPassword = txtConformPwd.getText();
        String password = txtPwd.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String userName = txtUname.getText();
        String role = comRole.getValue();

        if (name.isEmpty() || contactNumber.isEmpty() || confirmPassword.isEmpty() || password.isEmpty() || address.isEmpty() || email.isEmpty() || userName.isEmpty() || role == null) {
            alertSound.checkSounds(Sound.INVALID);
            new AlertNotification(" Alert Message",
                    "   Please fill in all required fields",
                    "unsuccess.png",
                    "OK"
            ).start();
            return;
        }

        if (!password.equals(confirmPassword)) {
            alertSound.checkSounds(Sound.INVALID);
            new AlertNotification(" Alert Message",
                    "   Passwords do not match. Please try again.",
                    "unsuccess.png",
                    "OK"
            ).start();
            return;
        }

        int roleId = signupFormModel.getRoleIdByDescription(role);
        SignupFormDto signupFormDto = new SignupFormDto(
                userName,
                BCrypt.hashpw(password, BCrypt.gensalt()),
                name,
                email,
                contactNumber,
                address,
                roleId
        );

        if (signupFormModel.signupUser(signupFormDto)) {
//            SendGmail.sendEmailWithGmail(txtEmail.getText();
//            SendGmail.sendEmailWithGmail(
//                    "noizemedicalcenter@gmail.com",
//                    "wmlg gufr kvti vjkf",
//                    email,
//                    "Welcome to Noize Medical Center!",
//                    "Dear " + name + ",\n\nThank you for signing up. Your account has been created successfully.\n\nBest Regards,\nNoize Medical Center Team"
//            );
            alertSound.checkSounds(Sound.CONFIRM);
            new AlertNotification(" Registration Successful",
                    "   Congratulations, " + userName + "!\n   Your account has been created. Please log in to continue.",
                    "success.png",
                    "OK"
            ).start();

            returnToLoginPage();

        } else {
            alertSound.checkSounds(Sound.INVALID);
            new AlertNotification(" Signup Failed",
                    "   Please check your input data and try again.",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }

    @FXML
    void hyperLogin(ActionEvent event) throws IOException {
        returnToLoginPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadRoles();
        } catch (SQLException e) {
            alertSound.checkSounds(Sound.INVALID);
            new AlertNotification(
                    "Error",
                    "An error occurred while loading roles. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }

    private void loadRoles() throws SQLException {
        comRole.getItems().clear();
        ArrayList<String> roles = signupFormModel.getAllRoles();
        ObservableList<String> obl = FXCollections.observableArrayList();
        obl.addAll(roles);
        comRole.setItems(obl);
    }

    private void returnToLoginPage() throws IOException {
        signupPane.getChildren().clear();
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/login/LoginForm.fxml"));
        signupPane.getChildren().add(pane);
    }

    public void backOnClicked(MouseEvent mouseEvent) {
        signupPane.getChildren().clear();
        try {
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/login/LoginForm.fxml")));
            signupPane.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEmailRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("email", txtEmail.getText())) {
            txtEmail.setStyle("-fx-text-fill: green;");
        } else {
            txtEmail.setStyle("-fx-text-fill: red;");
        }
    }

    public void onNameRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("name", txtName.getText())) {
            txtName.setStyle("-fx-text-fill: green;");
        } else {
            txtName.setStyle("-fx-text-fill: red;");
        }
    }

    public void onUNameRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("username", txtUname.getText())) {
            txtUname.setStyle("-fx-text-fill: green;");
        } else {
            txtUname.setStyle("-fx-text-fill: red;");
        }
    }

    public void onMobileRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("contactNumber", txtPhone.getText())) {
            txtPhone.setStyle("-fx-text-fill: green;");
        } else {
            txtPhone.setStyle("-fx-text-fill: red;");
        }
    }

    public void onAddressRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("address", txtAddress.getText())) {
            txtAddress.setStyle("-fx-text-fill: green;");
        } else {
            txtAddress.setStyle("-fx-text-fill: red;");
        }
    }

    public void onPwdRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtPwd.getText())) {
            txtPwd.setStyle("-fx-text-fill: green;");
        } else {
            txtPwd.setStyle("-fx-text-fill: red;");
        }
    }

    public void onPwd2Release(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("password", txtConformPwd.getText())) {
            txtConformPwd.setStyle("-fx-text-fill: green;");
        } else {
            txtConformPwd.setStyle("-fx-text-fill: red;");
        }
    }
}
