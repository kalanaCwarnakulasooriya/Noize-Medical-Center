package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.dto.AddPatientFormDto;
import com.noize.medicalcenter.model.AddPatientsFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import com.noize.medicalcenter.util.CheckRegex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPatientsFormController implements Initializable {
    private final AddPatientsFormModel ADDPATIENTMODEL = new AddPatientsFormModel();

    private Boolean isEmailValid = false;
    private Boolean isNameValid = false;
    private Boolean isAddressValid = false;
    private Boolean isDobValid = false;
    private Boolean isCoNumValid = false;
    private Boolean isRegDateValid = false;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXComboBox<String> comboGender;

    @FXML
    private JFXTextField txtCoNum;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private DatePicker regDatePicker;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtName;

    @FXML
    void addressRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("address", txtAddress.getText())) {
            txtAddress.setStyle("-fx-text-fill: green;");
            isAddressValid = true;
        } else {
            txtAddress.setStyle("-fx-text-fill: red;");
            isAddressValid = false;
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        isDobValid = dobDatePicker.getValue() != null;
        isRegDateValid = regDatePicker.getValue() != null;

        if (isEmailValid && isNameValid && isAddressValid && isDobValid && isCoNumValid && isRegDateValid) {
            int genderId = ADDPATIENTMODEL.getGenderIdByDescription(comboGender.getValue());
            LocalDate dob = dobDatePicker.getValue();
            LocalDate regDate = regDatePicker.getValue();

            Boolean isAddedPatient = ADDPATIENTMODEL.savePatient(
                    new AddPatientFormDto(
                            0,
                            txtName.getText(),
                            txtAddress.getText(),
                            txtCoNum.getText(),
                            txtEmail.getText(),
                            dob,
                            regDate,
                            genderId,
                            1,
                            1
                    )
            );

            if (isAddedPatient) {
                new AlertNotification(
                        "Success Message",
                        "Patient added successfully",
                        "success.png",
                        "GREEN"
                ).start();
                clearFields();
            } else {
                new AlertNotification(
                        "Error Message",
                        "Failed to add patient to the list",
                        "unsuccess.png",
                        "RED"
                ).start();
            }
        } else {
            new AlertNotification(
                    "Error Message",
                    "Please fill all fields correctly and try again",
                    "unsuccess.png",
                    "RED"
            ).start();
        }
    }

    @FXML
    void coNumRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("contactNumber", txtCoNum.getText())) {
            txtCoNum.setStyle("-fx-text-fill: green;");
            isCoNumValid = true;
        } else {
            txtCoNum.setStyle("-fx-text-fill: red;");
            isCoNumValid = false;
        }
    }

    @FXML
    void emailRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("email", txtEmail.getText())) {
            txtEmail.setStyle("-fx-text-fill: green;");
            isEmailValid = true;
        } else {
            txtEmail.setStyle("-fx-text-fill: red;");
            isEmailValid = false;
        }
    }

    @FXML
    void nameRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("name", txtName.getText())) {
            txtName.setStyle("-fx-text-fill: green;");
            isNameValid = true;
        } else {
            txtName.setStyle("-fx-text-fill: red;");
            isNameValid = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadGender();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dobDatePicker.setOnAction(this::dobDatePickerOnAction);
        regDatePicker.setOnAction(this::regDatePickerOnAction);
    }

    private void loadGender() throws SQLException {
        comboGender.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        comboGender.getItems().clear();
        ArrayList<String> genders = ADDPATIENTMODEL.getAllGenders();
        ObservableList<String> obl = FXCollections.observableArrayList(genders);
        comboGender.setItems(obl);
    }

    private void clearFields() {
        txtAddress.clear();
        txtCoNum.clear();
        txtEmail.clear();
        txtName.clear();
        comboGender.getSelectionModel().clearSelection();
        dobDatePicker.setValue(null);
        regDatePicker.setValue(null);
        isEmailValid = isNameValid = isAddressValid = isDobValid = isCoNumValid = isRegDateValid = false;
    }

    @FXML
    private void dobDatePickerOnAction(ActionEvent actionEvent) {
        dobDatePicker.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        LocalDate dob = dobDatePicker.getValue();
        if (dob != null && dob.isBefore(LocalDate.now())) {
            isDobValid = true;
        } else {
            isDobValid = false;
        }
    }

    @FXML
    private void regDatePickerOnAction(ActionEvent actionEvent) {
        regDatePicker.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        LocalDate regDate = regDatePicker.getValue();
        if (regDate != null && !regDate.isAfter(LocalDate.now())) {
            isRegDateValid = true;
        } else {
            isRegDateValid = false;
        }
    }
}
