package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.dto.AppointmentFormDto;
import com.noize.medicalcenter.dto.ItemFormDto;
import com.noize.medicalcenter.dto.tm.AppointmentTM;
import com.noize.medicalcenter.dto.tm.DoctorTM;
import com.noize.medicalcenter.dto.tm.PatientsTM;
import com.noize.medicalcenter.model.AppointmentFormModel;
import com.noize.medicalcenter.model.DoctorFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import com.noize.medicalcenter.util.CheckRegex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable {
    AppointmentFormModel appointmentFormModel = new AppointmentFormModel();
    DoctorFormModel doctorModel = new DoctorFormModel();

    boolean isNameValid = false;
    boolean isAgeValid = false;
    boolean isDescriptionValid = false;
    boolean isDateValid = false;

    @FXML
    private TableColumn<AppointmentTM, String> ageCol;

    @FXML
    private TableColumn<AppointmentTM, Date> dateCol;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private JFXTextField txtName;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private ComboBox<String> comboPID;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblName;

    @FXML
    private ComboBox<String> comboDoctor;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private TableColumn<AppointmentTM, String> descCol;

    @FXML
    private TableColumn<AppointmentTM, String> patientNameCol;

    @FXML
    private TableColumn<AppointmentTM, String> statusCol;

    @FXML
    private TableView<AppointmentTM> tblAppointment;
    
    @FXML
    private TextField lblSearch;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    void ageRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("password", txtAge.getText())) {
            txtAge.setStyle("-fx-text-fill: green;");
            isAgeValid = true;
        } else {
            txtAge.setStyle("-fx-text-fill: red;");
            isAgeValid = false;
        }
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        if (tblAppointment != null) {
            AppointmentTM selectedAppointment = tblAppointment.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                new AlertNotification(
                        "Error",
                        "Please select a appointment to delete.",
                        "unsuccess.png",
                        "ok"
                ).start();
                return;
            }
        }
        String appointmentName = tblAppointment.getSelectionModel().getSelectedItem().getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Appointment ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            clearFields();
            refreshTable();
            boolean isDeleted = appointmentFormModel.isDeleteAppointment(appointmentName);
            if (isDeleted) {
                btnSaveItem.setDisable(false);
                btnUpdateItem.setDisable(true);
                btnDeleteItem.setDisable(true);
                btnReset.setDisable(false);
                new AlertNotification(
                        "Success",
                        "Appointment deleted...!",
                        "success.png",
                        "ok"
                ).start();
                refreshTable();
            } else {
                new AlertNotification(
                        "Error",
                        "An error occurred while deleting the appointment. Please try again later.",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
        }else {
            new AlertNotification(
                    "Error",
                    "Please select a appointment to delete.",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        txtName.clear();
        txtAge.clear();
        comboStatus.getSelectionModel().clearSelection();
        txtDescription.clear();
        datePicker.setValue(null);
        comboDoctor.setValue(null);
        lblName.setText("");

        loadDoctorsId();

        btnDeleteItem.setDisable(true);
        btnUpdateItem.setDisable(true);
        btnSaveItem.setDisable(false);

        txtName.setStyle("");
        txtAge.setStyle("");
        txtDescription.setStyle("");
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) throws SQLException {
        isDateValid = datePicker.getValue() != null;

        if (isDateValid && !isNameValid && !isAgeValid && !isDescriptionValid) {
            new AlertNotification("Invalid input",
                    "Please check the input fields",
                    "unsuccess.png",
                    "ok"
            ).start();
            return;
        }
        LocalDate date = datePicker.getValue();

        if (appointmentFormModel.isAddAppointment(txtName.getText(), txtAge.getText(), comboStatus.getValue(), txtDescription.getText(), String.valueOf(date), comboDoctor.getValue())) {
            getAppointments();
            new AlertNotification(
                    "Success",
                    "Appointment added successfully",
                    "success.png",
                    "ok"
            ).start();
            getAppointments();
            clearFields();
            refreshTable();
        } else {
            new AlertNotification(
                    "Error",
                    "Failed to add " + txtName.getText() + " to the appointment list",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        isDateValid = datePicker.getValue() != null;

        String name = txtName.getText();
        String age = txtAge.getText();
        String description = txtDescription.getText();
        String status = comboStatus.getValue();
        String doctorId = comboDoctor.getValue();

        AppointmentFormDto appointmentFormDto = new AppointmentFormDto(
                0,
                name,
                age,
                status,
                description,
                String.valueOf(datePicker.getValue()),
                doctorId,
                1
        );
        boolean isUpdate = appointmentFormModel.isUpdateAppointment(appointmentFormDto);

        if (isUpdate){
            refreshTable();
            clearFields();
            new AlertNotification(
                    "Success",
                    "Appointment updated successfully",
                    "success.png",
                    "ok"
            ).start();
        }else {
            new AlertNotification(
                    "Error",
                    "Failed to update " + name + " to the appointment list",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void descRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("desc", txtDescription.getText())) {
            txtDescription.setStyle("-fx-text-fill: green;");
            isDescriptionValid = true;
        } else {
            txtDescription.setStyle("-fx-text-fill: red;");
            isDescriptionValid = false;
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        AppointmentTM selectPatient = tblAppointment.getSelectionModel().getSelectedItem();
        if (selectPatient != null) {
            txtName.setText(selectPatient.getName());
            txtAge.setText(String.valueOf(selectPatient.getAge()));
            comboStatus.setValue(selectPatient.getStatus());
            txtDescription.setText(selectPatient.getDescription());
            datePicker.setValue(LocalDate.parse(selectPatient.getDate()));
            comboDoctor.setValue(String.valueOf(selectPatient.getDoctorId()));

            btnSaveItem.setDisable(true);
            btnUpdateItem.setDisable(false);
            btnDeleteItem.setDisable(false);
        }
    }

    @FXML
    void refreshTable() throws SQLException {
        tblAppointment.getItems().clear();
        ArrayList<AppointmentTM> allAppointments = appointmentFormModel.getAllAppointments();
        ObservableList<AppointmentTM> appointmentList = FXCollections.observableArrayList(allAppointments);
        tblAppointment.setItems(appointmentList);
    }

    public void datePickerOnAction(ActionEvent event) {
        datePicker.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        LocalDate date = datePicker.getValue();
        if (date != null && date.isBefore(LocalDate.now())) {
            isDateValid = true;
        } else {
            isDateValid = false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            getAppointments();
            getStatus();
            loadDoctorsId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAppointments() throws SQLException {
        ArrayList<AppointmentTM> appointments = appointmentFormModel.getAllAppointments();
        tblAppointment.getItems().clear();
        tblAppointment.getItems().addAll(appointments);
    }

    public  void getStatus(){
        ArrayList<String> status = new ArrayList<>();
        status.add("Pending");
        status.add("Done");
        comboStatus.getItems().clear();
        comboStatus.getItems().addAll(status);
    }

    public void searchAppointment() throws SQLException {
        ArrayList<AppointmentTM> appointments = appointmentFormModel.searchAppointments(lblName.getText());
        ObservableList<AppointmentTM> appointmentList = FXCollections.observableArrayList();
        for (AppointmentTM appointment : appointments) {
            appointmentList.add(appointment);
        }
        tblAppointment.setItems(appointmentList);
    }

    public void clearFields(){
        txtAge.clear();
        comboStatus.getSelectionModel().clearSelection();
        txtName.clear();
        txtDescription.clear();
        datePicker.setValue(null);
        comboDoctor.getSelectionModel().clearSelection();
        lblName.setText("");
    }

    public void nameRelease(KeyEvent keyEvent) {
        if (CheckRegex.checkRegex("name", txtName.getText())) {
            txtName.setStyle("-fx-text-fill: green;");
            isNameValid = true;
        } else {
            txtName.setStyle("-fx-text-fill: red;");
            isNameValid = false;
        }
    }

    public void comboStatusOnAction(ActionEvent event) {
        comboStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
    }

    private void loadDoctorsId() throws SQLException {
        ArrayList<String> DoctorsId  = doctorModel.getAllDocId();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(DoctorsId);
        comboDoctor.setItems(observableList);
    }

    public void comboDoctorOnAction(ActionEvent event) throws SQLException {
        comboDoctor.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        String selectedDoctor = (String) comboDoctor.getSelectionModel().getSelectedItem();
        DoctorTM doctorTM = doctorModel.findById(selectedDoctor);

        if (doctorTM != null) {
            lblName.setText(doctorTM.getName());
        }
    }

    public void searchAppointment(KeyEvent keyEvent) throws SQLException {
        ArrayList<AppointmentTM> patients = appointmentFormModel.searchAppointments(lblSearch.getText());
        ObservableList<AppointmentTM> patientTMS = FXCollections.observableArrayList();
        for (AppointmentTM patientsDto : patients) {
            patientTMS.add(patientsDto);
        }
        tblAppointment.setItems(patientTMS);
    }
}
