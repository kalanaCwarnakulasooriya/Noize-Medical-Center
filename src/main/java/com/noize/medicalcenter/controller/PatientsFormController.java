package com.noize.medicalcenter.controller;

import com.noize.medicalcenter.dto.tm.PatientsTM;
import com.noize.medicalcenter.model.PatientsFormModel;
import com.noize.medicalcenter.util.AlertNotification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientsFormController implements Initializable {
    PatientsFormModel patientsFormModel = new PatientsFormModel();

    @FXML
    private TableColumn<PatientsTM, String> addressCol;

    @FXML
    private TableColumn<PatientsTM, String> coNumCol;

    @FXML
    private TableColumn<PatientsTM, Date> dobCol;

    @FXML
    private TableColumn<PatientsTM, String> emailCol;

    @FXML
    private TableColumn<PatientsTM, String> genderCol;

    @FXML
    private TextField lblSearch;

    @FXML
    private TableColumn<PatientsTM, String> namCol;

    @FXML
    private TableColumn<PatientsTM, Date> registerCol;

    @FXML
    private TableView<PatientsTM> tblPatient;

    @FXML
    void addPatientClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPatientsForm.fxml"));
        Parent root = loader.load();
        Stage windows = new Stage();
        windows.centerOnScreen();
        windows.setResizable(false);
        windows.setTitle("  Add Patients");
        windows.getIcons().add(
                new Image(
                        getClass().getResourceAsStream("/asset/icon/app_logo.png")
                )
        );
        windows.initModality(Modality.APPLICATION_MODAL);
        windows.initOwner(lblSearch.getScene().getWindow());
        windows.setScene(new Scene(root));
        windows.show();
    }

    @FXML
    void addOrderClick(ActionEvent event) throws IOException {

    }

    @FXML
    void refreshTable() throws SQLException {
        tblPatient.getItems().clear();
        ArrayList<PatientsTM> allStaff = patientsFormModel.getAllPatients();
        ObservableList<PatientsTM> patientsDtos = FXCollections.observableArrayList();
        for (PatientsTM patientsDto : allStaff) {
            patientsDtos.add(patientsDto);
        }
        tblPatient.setItems(patientsDtos);
    }

    @FXML
    void searchPatients(KeyEvent event) throws SQLException {
        ArrayList<PatientsTM> patients = patientsFormModel.searchPatients(lblSearch.getText());
        ObservableList<PatientsTM> patientTMS = FXCollections.observableArrayList();
        for (PatientsTM patientsDto : patients) {
            System.out.println(patientsDto.toString());
            patientTMS.add(patientsDto);
        }
        tblPatient.setItems(patientTMS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namCol.setCellValueFactory(new PropertyValueFactory<>("patientsName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("patientsAddress"));
        coNumCol.setCellValueFactory(new PropertyValueFactory<>("patientsContactNumber"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("patientsEmail"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("patientsDob"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("patientsGender"));
        registerCol.setCellValueFactory(new PropertyValueFactory<>("patientsRegDate"));
        try {
            refreshTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPatientsForm.fxml"));
        Parent root = loader.load();
        Stage windows = new Stage();
        windows.centerOnScreen();
        windows.setResizable(false);
        windows.setTitle("  Add Patients");
        windows.getIcons().add(
                new Image(
                        getClass().getResourceAsStream("/asset/icon/app_logo.png")
                )
        );
//        windows.initModality(Modality.APPLICATION_MODAL);
        windows.initOwner(lblSearch.getScene().getWindow());
        windows.setScene(new Scene(root));
        windows.show();
    }


    @FXML
    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        if (tblPatient != null) {
            PatientsTM selectedPatient = tblPatient.getSelectionModel().getSelectedItem();
            if (selectedPatient == null) {
                new AlertNotification(
                        "Error",
                        "Please select a patient to delete.",
                        "unsuccess.png",
                        "ok"
                ).start();
                return;
            }
        }
        String PatientName = tblPatient.getSelectionModel().getSelectedItem().getPatientsName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this patient ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES) {
            boolean isDeleted = patientsFormModel.deletePatient(PatientName);
            if (isDeleted) {
                new AlertNotification(
                        "Success",
                        "Patient deleted...!",
                        "success.png",
                        "ok"
                ).start();
                refreshTable();
            } else {
                new AlertNotification(
                        "Error",
                        "An error occurred while deleting the patient. Please try again later.",
                        "unsuccess.png",
                        "ok"
                ).start();
            }
        }else {
            new AlertNotification(
                    "Error",
                    "Please select a patient to delete.",
                    "unsuccess.png",
                    "ok"
            ).start();
        }
    }

    public void onClicked(MouseEvent mouseEvent) {
    }
}
