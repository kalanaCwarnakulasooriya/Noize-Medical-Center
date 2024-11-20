package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.util.CheckRegex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppointmentFormController {

    @FXML
    private TableColumn<?, ?> ageCol;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private ComboBox<?> comboPID;

    @FXML
    private ComboBox<?> comboStatus;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private Label iblAppointDate;

    @FXML
    private Label lblPName;

    @FXML
    private TableColumn<?, ?> patientNameCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableView<?> tblAppointment;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    void ageRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("password", txtAge.getText())) {
            txtAge.setStyle("-fx-text-fill: green;");
        } else {
            txtAge.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) {

    }

    @FXML
    void comboMobileOnAction(ActionEvent event) {

    }

    @FXML
    void descRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("desc", txtDescription.getText())) {
            txtDescription.setStyle("-fx-text-fill: green;");
        } else {
            txtDescription.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {

    }

    @FXML
    void refreshTable(ActionEvent event) {

    }

    public void datePickerOnAction(ActionEvent event) {
    }
}
