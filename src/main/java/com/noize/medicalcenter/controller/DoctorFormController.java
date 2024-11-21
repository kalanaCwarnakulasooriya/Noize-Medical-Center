package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.db.DBConnection;
import com.noize.medicalcenter.dto.AddPatientFormDto;
import com.noize.medicalcenter.dto.DoctorFormDto;
import com.noize.medicalcenter.dto.ItemFormDto;
import com.noize.medicalcenter.dto.tm.DoctorTM;
import com.noize.medicalcenter.dto.tm.ItemTM;
import com.noize.medicalcenter.dto.tm.PatientsTM;
import com.noize.medicalcenter.model.DoctorFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import com.noize.medicalcenter.util.CheckRegex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DoctorFormController implements Initializable {
    private boolean isNameValid = false;
    private boolean isCoNumValid = false;
    private boolean isEmailValid = false;
    private boolean isAddressValid = false;

    DoctorFormModel doctorModel = new DoctorFormModel();

    @FXML
    private AnchorPane addPane;

    @FXML
    private TableColumn<DoctorTM, String> addressCol;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnReset;

    @FXML
    private JFXButton btnReport;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private TableColumn<DoctorTM, String> coNumCol;

    @FXML
    private TableColumn<DoctorTM, String> emailCol;

    @FXML
    private TextField lblSearch;

    @FXML
    private TableColumn<DoctorTM, String> namCol;

    @FXML
    private TableView<DoctorTM> tblDoctor;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtDocName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtMobile;

    @FXML
    void addressReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("address", txtAddress.getText())) {
            txtAddress.setStyle("-fx-text-fill: green;");
            isAddressValid = true;
        } else {
            txtAddress.setStyle("-fx-text-fill: red;");
            isAddressValid = false;
        }
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        if (tblDoctor != null) {
            DoctorTM selectedDoctor = tblDoctor.getSelectionModel().getSelectedItem();
            if (selectedDoctor == null) {
                new AlertNotification(
                        "Error",
                        "Please select a doctor to delete.",
                        "unsuccess.png",
                        "ok"
                ).start();
                return;
            }
        }
        String DocName = tblDoctor.getSelectionModel().getSelectedItem().getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Doctor ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            clearFields();
            refreshTable();
            boolean isDeleted = doctorModel.deleteDoctor(DocName);
            if (isDeleted) {
                new AlertNotification(
                        "Success",
                        "Doctor deleted...!",
                        "success.png",
                        "ok"
                ).start();
                refreshTable();
            } else {
                new AlertNotification(
                        "Error",
                        "An error occurred while deleting the doctor. Please try again later.",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
        }else {
            new AlertNotification(
                    "Error",
                    "You have canceled the delete operation.",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtDocName.clear();
        txtEmail.clear();
        txtMobile.clear();
        txtAddress.clear();

        btnDeleteItem.setDisable(true);
        btnUpdateItem.setDisable(true);
        btnSaveItem.setDisable(false);

        txtDocName.setStyle("");
        txtEmail.setStyle("");
        txtMobile.setStyle("");
        txtAddress.setStyle("");
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) throws SQLException {
        if (isNameValid && isCoNumValid && isEmailValid && isAddressValid) {
            Boolean isAddedDoctor = doctorModel.isSaveDoctor(
                    new DoctorFormDto(
                            0,
                            txtDocName.getText(),
                            txtEmail.getText(),
                            txtMobile.getText(),
                            txtAddress.getText(),
                            1
                    )
            );

            if (isAddedDoctor) {
                refreshTable();
                new AlertNotification(
                        "Success",
                        "Doctor added successfully",
                        "success.png",
                        "GREEN"
                ).start();
                clearFields();
            } else {
                new AlertNotification(
                        "Error",
                        "Failed to add doctor",
                        "unsuccess.png",
                        "RED"
                ).start();
            }
        } else {
            new AlertNotification(
                    "Error",
                    "Please fill all fields correctly",
                    "unsuccess.png",
                    "RED"
            ).start();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        String name = txtDocName.getText();
        String email = txtEmail.getText();
        String mobile = txtMobile.getText();
        String address = txtAddress.getText();

        DoctorFormDto doctorFormDto = new DoctorFormDto(
                0,
                name,
                email,
                mobile,
                address,
                1
        );
        boolean isUpdate = doctorModel.isUpdateDoctor(doctorFormDto);

        if (isUpdate){
            refreshTable();
            clearFields();
            new AlertNotification(
                    "Success",
                    "Doctor updated successfully",
                    "success.png",
                    "ok"
            ).start();
        }else {
            new AlertNotification(
                    "Error",
                    "Failed to update " + name + " to doctor",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void docNameReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("docName", txtDocName.getText())) {
            txtDocName.setStyle("-fx-text-fill: green;");
            isNameValid = true;
        } else {
            txtDocName.setStyle("-fx-text-fill: red;");
            isNameValid = false;
        }
    }

    @FXML
    void emailReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("email", txtEmail.getText())) {
            txtEmail.setStyle("-fx-text-fill: green;");
            isEmailValid = true;
        } else {
            txtEmail.setStyle("-fx-text-fill: red;");
            isEmailValid = false;
        }
    }

    @FXML
    void mobileReleased(KeyEvent event) {
        if (CheckRegex.checkRegex("contactNumber", txtMobile.getText())) {
            txtMobile.setStyle("-fx-text-fill: green;");
            isCoNumValid = true;
        } else {
            txtMobile.setStyle("-fx-text-fill: red;");
            isCoNumValid = false;
        }
    }

    @FXML
    void searchPatients(KeyEvent event) throws SQLException {
        ArrayList<DoctorTM> doctors = doctorModel.searchDoctors(lblSearch.getText());
        ObservableList<DoctorTM> doctorList = FXCollections.observableArrayList();
        for (DoctorTM doctor : doctors) {
            doctorList.add(doctor);
        }
        tblDoctor.setItems(doctorList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        coNumCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        getDoctor();
    }

    private void clearFields() {
        txtDocName.clear();
        txtEmail.clear();
        txtMobile.clear();
        txtAddress.clear();
    }

    private void refreshTable() throws SQLException {
        tblDoctor.getItems().clear();
        ArrayList<DoctorTM> allDoctors = doctorModel.getAllDoctors();
        ObservableList<DoctorTM> doctorList = FXCollections.observableArrayList(allDoctors);
        tblDoctor.setItems(doctorList);
    }

    public void getDoctor() {
        ArrayList<DoctorTM> doctorDtos = doctorModel.getDoctor();
        tblDoctor.getItems().clear();
        tblDoctor.getItems().addAll(doctorDtos);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        DoctorTM selectDoctor = tblDoctor.getSelectionModel().getSelectedItem();
        if (selectDoctor != null) {
            txtDocName.setText(selectDoctor.getName());
            txtMobile.setText(selectDoctor.getContactNumber());
            txtEmail.setText(selectDoctor.getEmail());
            txtAddress.setText(selectDoctor.getAddress());

            btnSaveItem.setDisable(true);
            btnUpdateItem.setDisable(false);
            btnDeleteItem.setDisable(false);
        }
    }

    public void reportOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("TODAY", LocalDate.now().toString());
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/Doctor_Report.jrxml")
            );
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load report..!");
            e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Data empty..!");
            e.printStackTrace();
        }
    }
}
