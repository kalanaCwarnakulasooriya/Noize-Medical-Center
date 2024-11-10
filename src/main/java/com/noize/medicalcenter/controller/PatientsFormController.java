package com.noize.medicalcenter.controller;

import com.noize.medicalcenter.dto.tm.PatientsDto;
import com.noize.medicalcenter.model.PatientsFormModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientsFormController implements Initializable {
    PatientsFormModel patientsFormModel = new PatientsFormModel();

    @FXML
    private TableColumn<PatientsDto, String> addressCol;

    @FXML
    private TableColumn<PatientsDto, String> coNumCol;

    @FXML
    private TableColumn<PatientsDto, Date> dobCol;

    @FXML
    private TableColumn<PatientsDto, String> emailCol;

    @FXML
    private TableColumn<PatientsDto, String> genderCol;

    @FXML
    private TextField lblSearch;

    @FXML
    private TableColumn<PatientsDto, String> namCol;

    @FXML
    private TableColumn<PatientsDto, Date> registerCol;

    @FXML
    private TableView<PatientsDto> tblPatient;

    @FXML
    void addPatientClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patients/addPatientsForm.fxml"));
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
    void refreshTable() throws SQLException {
        tblPatient.getItems().clear();
        ArrayList<PatientsDto> allStaff = patientsFormModel.getAllPatients();
        ObservableList<PatientsDto> patientsDtos = FXCollections.observableArrayList();
        for (PatientsDto patientsDto : allStaff) {
            patientsDtos.add(patientsDto);
        }
        tblPatient.setItems(patientsDtos);
    }

    @FXML
    void searchPatients(KeyEvent event) throws SQLException {
        ArrayList<PatientsDto> patients = patientsFormModel.searchPatients(lblSearch.getText());
        ObservableList<PatientsDto> patientTMS = FXCollections.observableArrayList();
        for (PatientsDto patientsDto : patients) {
            System.out.println(patientsDto.toString());
            patientTMS.add(patientsDto);
        }
        tblPatient.setItems(patientTMS);
    }

    @FXML
    void takeAction(ActionEvent event) {

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
}
