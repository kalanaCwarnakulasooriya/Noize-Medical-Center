package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PrescriptionFormController {

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
    private TableColumn<?, ?> dosageCol;

    @FXML
    private Label lblPName;

    @FXML
    private Label lblPrescriptionDate;

    @FXML
    private TableColumn<?, ?> mediDetailCol;

    @FXML
    private TableView<?> tblPrescription;

    @FXML
    private JFXTextField txtDosage;

    @FXML
    private JFXTextField txtMediDetails;

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
    void dosageRelease(KeyEvent event) {

    }

    @FXML
    void mediDetailRelease(KeyEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {

    }

    @FXML
    void refreshTable(ActionEvent event) {

    }

}
