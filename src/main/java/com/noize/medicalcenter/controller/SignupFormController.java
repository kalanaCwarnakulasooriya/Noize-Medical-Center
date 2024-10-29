package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.dto.SignupFormDto;
import com.noize.medicalcenter.model.SignupFormModel;
import com.noize.medicalcenter.notification.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SignupFormController implements Initializable {

    SignupFormModel signupFormModel = new SignupFormModel();

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
        String name = this.txtName.getText();
        String contactNumber = this.txtPhone.getText();
        String confirmPassword = this.txtConformPwd.getText();
        String password = this.txtPwd.getText();
        String address = this.txtAddress.getText();
        String email = this.txtEmail.getText();
        String userName = this.txtUname.getText();
        String role = (String) comRole.getValue();

        if (name.isEmpty() || contactNumber.isEmpty() || confirmPassword.isEmpty() || password.isEmpty() || address.isEmpty() || email.isEmpty() || userName.isEmpty() || role == null) {
            System.out.println("Please fill in all required fields.");
            new AlertNotification(" Alert Message",
                    "   Please fill in all required fields",
                    "unsuccess.png",
                    "OK"
            ).start();
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            new AlertNotification(" Alert Message",
                    "   Password do not match \n   please check your password or confirm password and try again",
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
            System.out.println("Registration successful");
            new AlertNotification(" Registration Successful",
                    "   Congratulations! " + userName + "\n   Your account has been created. Please login to continue",
                    "success.png",
                    "OK"
            ).start();
        } else {
            new AlertNotification(" Signup Failed...! Recheck your input data",
                    "   Please check your input data and try again",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
        retunToLoginPage();
    }

    @FXML
    void hyperLogin(ActionEvent event) throws IOException {
        retunToLoginPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadRoles();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }

    void loadRoles() throws SQLException {
        comRole.getItems().clear();
        ArrayList<String> roles = signupFormModel.getAllRoles();
        ObservableList<String> obl = FXCollections.observableArrayList();
        obl.addAll(roles);
        comRole.setItems(obl);
    }

    void retunToLoginPage() throws IOException {
        signupPane.getChildren().clear();
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/login/LoginForm.fxml"));
        signupPane.getChildren().add(pane);
    }
}
