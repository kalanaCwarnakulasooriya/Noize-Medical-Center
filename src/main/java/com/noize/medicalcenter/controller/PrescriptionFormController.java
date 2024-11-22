package com.noize.medicalcenter.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.noize.medicalcenter.db.DBConnection;
import com.noize.medicalcenter.dto.AppointmentFormDto;
import com.noize.medicalcenter.dto.PrescriptionFormDto;
import com.noize.medicalcenter.dto.tm.AppointmentTM;
import com.noize.medicalcenter.dto.tm.DoctorTM;
import com.noize.medicalcenter.dto.tm.PatientsTM;
import com.noize.medicalcenter.dto.tm.PrescriptionTM;
import com.noize.medicalcenter.model.AppointmentFormModel;
import com.noize.medicalcenter.model.DoctorFormModel;
import com.noize.medicalcenter.model.PatientsFormModel;
import com.noize.medicalcenter.model.PrescriptionFormModel;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PrescriptionFormController implements Initializable {
    PrescriptionFormModel prescriptionFormModel = new PrescriptionFormModel();
    PatientsFormModel patientsFormModel = new PatientsFormModel();
    AppointmentFormModel appointmentFormModel = new AppointmentFormModel();
    DoctorFormModel doctorFormModel = new DoctorFormModel();

    boolean isDosageValid = false;
    boolean isMediDetailsValid = false;
    boolean isDateValid = false;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnReset;

    @FXML
    private Label lblDocName;

    @FXML
    private ComboBox<String> comboDocName;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private ComboBox<String> comboAppoID;

    @FXML
    private ComboBox<String> comboPID;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<?, ?> dosageCol;

    @FXML
    private Label lblAge;

    @FXML
    private Label lblPName;

    @FXML
    private TableColumn<?, ?> mediDetailCol;

    @FXML
    private JFXButton btnReport;

    @FXML
    private TableView<PrescriptionTM> tblPrescription;

    @FXML
    private JFXTextField txtDosage;

    @FXML
    private JFXTextField txtMediDetails;

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        if (tblPrescription != null) {
            PrescriptionTM selectedPrescription = tblPrescription.getSelectionModel().getSelectedItem();
            if (selectedPrescription == null) {
                new AlertNotification(
                        "Error Message",
                        "Please select a prescription to delete.",
                        "unsuccess.png",
                        "ok"
                ).start();
                return;
            }
        }
        String prescriptionDate = tblPrescription.getSelectionModel().getSelectedItem().getDate();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Prescription ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            clearFields();
            refreshTable();
            boolean isDeleted = prescriptionFormModel.isDeletePrescription(prescriptionDate);
            if (isDeleted) {
                btnSaveItem.setDisable(false);
                btnUpdateItem.setDisable(true);
                btnDeleteItem.setDisable(true);
                btnReset.setDisable(false);
                new AlertNotification(
                        "Success Message",
                        "Prescription deleted successfully.",
                        "success.png",
                        "ok"
                ).start();
                refreshTable();
            } else {
                new AlertNotification(
                        "Error Message",
                        "An error occurred while deleting the prescription. Please try again later.",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
        } else {
            new AlertNotification(
                    "Error Message",
                    "You have canceled the delete operation.",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtDosage.clear();
        txtMediDetails.clear();
        datePicker.setValue(null);
        comboAppoID.getSelectionModel().clearSelection();
        comboPID.getSelectionModel().clearSelection();
        comboDocName.getSelectionModel().clearSelection();
        lblAge.setText("");
        lblPName.setText("");
        lblDocName.setText("");

        btnDeleteItem.setDisable(true);
        btnUpdateItem.setDisable(true);
        btnSaveItem.setDisable(false);

        txtDosage.setStyle("");
        txtMediDetails.setStyle("");
    }

    @FXML
    void btnSaveItemOnAction(ActionEvent event) throws SQLException {
        isDateValid = datePicker.getValue() != null;

        if (isDateValid && !isDosageValid && !isMediDetailsValid) {
            new AlertNotification(
                    "Invalid input",
                    "Please check the input fields",
                    "unsuccess.png",
                    "ok"
            ).start();
            return;
        }
        LocalDate date = datePicker.getValue();

        if (prescriptionFormModel.isAddPrescription((String.valueOf(date)), txtMediDetails.getText(), txtDosage.getText(), "1", comboDocName.getValue())) {
            getPrescription();
            new AlertNotification(
                    "Success Message",
                    "Prescription added successfully",
                    "success.png",
                    "ok"
            ).start();
            getPrescription();
            clearFields();
            refreshTable();
        } else {
            new AlertNotification(
                    "Error Message",
                    "Failed to add to the Prescription list",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        isDateValid = datePicker.getValue() != null;

        String mediDetails = txtMediDetails.getText();
        String dosage = txtDosage.getText();
        String doctorId = comboDocName.getValue();

        PrescriptionFormDto prescriptionFormDto = new PrescriptionFormDto(
                0,
                String.valueOf(datePicker.getValue()),
                mediDetails,
                dosage,
                1,
                doctorId
        );
        boolean isUpdate = prescriptionFormModel.isUpdatePrescription(prescriptionFormDto);

        if (isUpdate){
            refreshTable();
            clearFields();
            new AlertNotification(
                    "Success Message",
                    "Prescription updated successfully",
                    "success.png",
                    "ok"
            ).start();
        }else {
            new AlertNotification(
                    "Error Message",
                    "Failed to update to the Prescription list",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    @FXML
    void datePickerOnAction(ActionEvent event) {
        datePicker.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        LocalDate date = datePicker.getValue();
        if (date != null && date.isBefore(LocalDate.now())) {
            isDateValid = true;
        } else {
            isDateValid = false;
        }
    }

    @FXML
    void dosageRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("dosage", txtDosage.getText())) {
            txtDosage.setStyle("-fx-text-fill: green;");
            isDosageValid = true;
        } else {
            txtDosage.setStyle("-fx-text-fill: red;");
            isDosageValid = false;
        }
    }

    @FXML
    void mediDetailRelease(KeyEvent event) {
        if (CheckRegex.checkRegex("dosage", txtMediDetails.getText())) {
            txtMediDetails.setStyle("-fx-text-fill: green;");
            isMediDetailsValid = true;
        } else {
            txtMediDetails.setStyle("-fx-text-fill: red;");
            isMediDetailsValid = false;
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        PrescriptionTM selectPrescription = (PrescriptionTM) tblPrescription.getSelectionModel().getSelectedItem();
        if (selectPrescription != null) {
            txtDosage.setText(selectPrescription.getDosage());
            txtMediDetails.setText(selectPrescription.getMediDetails());
            datePicker.setValue(LocalDate.parse(selectPrescription.getDate()));
            comboDocName.setValue(String.valueOf(selectPrescription.getDoctorId()));
//            comboAppoID.setValue(String.valueOf(selectPrescription.getAppoId()));
//            comboPID.setValue(String.valueOf(selectPrescription.getPatientId()));

            btnSaveItem.setDisable(true);
            btnUpdateItem.setDisable(false);
            btnDeleteItem.setDisable(false);
        }
    }

    @FXML
    void refreshTable() throws SQLException {
        tblPrescription.getItems().clear();
        ArrayList<PrescriptionTM> allPrescription = prescriptionFormModel.getAllPrescription();
        ObservableList<PrescriptionTM> appointmentList = FXCollections.observableArrayList(allPrescription);
        tblPrescription.setItems(appointmentList);
    }

    public void clearFields(){
        txtDosage.clear();
        txtMediDetails.clear();
        datePicker.setValue(null);
        comboAppoID.getSelectionModel().clearSelection();
        comboPID.getSelectionModel().clearSelection();
        comboDocName.getSelectionModel().clearSelection();
        lblAge.setText("");
        lblPName.setText("");
        lblDocName.setText("");
    }

    public void getPrescription() throws SQLException {
        ArrayList<PrescriptionTM> prescriptionTMS = prescriptionFormModel.getAllPrescription();
        tblPrescription.getItems().clear();
        tblPrescription.getItems().addAll(prescriptionTMS);
    }

    private void loadPatientsId() throws SQLException {
        ArrayList<String> patientsId  = patientsFormModel.getAllPatientMobile();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(patientsId);
        comboPID.setItems(observableList);
    }

    private void loadAppointmentsId() throws SQLException {
        ArrayList<String> appointmentsId  = appointmentFormModel.getAppointments();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(appointmentsId);
        comboAppoID.setItems(observableList);
    }

    private void loadDoctorsId() throws SQLException {
        ArrayList<String> DoctorsId  = doctorFormModel.getAllDocId();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(DoctorsId);
        comboDocName.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        mediDetailCol.setCellValueFactory(new PropertyValueFactory<>("mediDetails"));
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));

        try {
            getPrescription();
            loadAppointmentsId();
            loadPatientsId();
            loadDoctorsId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void comboPatientOnAction(ActionEvent event) throws SQLException {
        comboPID.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        String selectedDoctor = comboPID.getSelectionModel().getSelectedItem();
        PatientsTM patientTM = patientsFormModel.findById(selectedDoctor);

        if (patientTM != null) {
            lblPName.setText(patientTM.getPatientsName());
        }
    }

    public void comboAppointmentOnAction(ActionEvent event) throws SQLException {
        comboAppoID.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        String selectedAppointment = comboAppoID.getSelectionModel().getSelectedItem();
        AppointmentTM appointmentTM = appointmentFormModel.findById(selectedAppointment);

        if (appointmentTM != null) {
            lblAge.setText(appointmentTM.getAge());
        }
    }

    public void comboDocNameOnAction(ActionEvent event) throws SQLException {
        comboDocName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: green;");
        String selectedDoctor = (String) comboDocName.getSelectionModel().getSelectedItem();
        DoctorTM doctorTM = doctorFormModel.findById(selectedDoctor);

        if (doctorTM != null) {
            lblDocName.setText(doctorTM.getName());
        }
    }

    public void reportOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("TODAY", LocalDate.now().toString());
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/Prescription_Report.jrxml")
            );
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            new AlertNotification(
                    "Error Message",
                    "An error occurred while loading the report. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
            e.printStackTrace();
        } catch (SQLException e) {
            new AlertNotification(
                    "Error Message",
                    "Data empty. Please try again later.",
                    "unsuccess.png",
                    "OK"
            ).start();
            e.printStackTrace();
        }
    }
}
