package com.noize.medicalcenter.controller;

import com.noize.medicalcenter.util.AlertNotification;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    @FXML
    private AnchorPane centerPane;

    @FXML
    private Label lblDate;

    @FXML
    private AnchorPane adminPane;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTitle;

    @FXML
    void callAppointments(ActionEvent event) throws IOException {
        lblTitle.setText("Appointments");
        callPane("appointment/appointmentForm.fxml");
    }

    @FXML
    void callDashboard(ActionEvent event) throws IOException {
        lblTitle.setText("DashBoard");
        callPane("dashboard/dashboardForm.fxml");
    }

    @FXML
    void callMedicine(ActionEvent event) throws IOException {
        lblTitle.setText("Medicine");
        callPane("transaction/orderForm.fxml");
    }

    @FXML
    void callPatients(ActionEvent event) throws IOException {
        lblTitle.setText("Patients");
        callPane("patients/patientsForm.fxml");
    }

    @FXML
    void callDoctors(ActionEvent event) throws IOException {
        lblTitle.setText("Doctors");
        callPane("doctor/doctorForm.fxml");
    }

    @FXML
    void callSettings(ActionEvent event) {

    }

    @FXML
    void mouseEnter(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #1dd1a1; -fx-text-fill: white;");
    }

    @FXML
    void mouseExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:  #1e293c; -fx-text-fill: white;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTitle.setText("DashBoard");
        try {
            callPane("dashboard/dashboardForm.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDate();
        setTime();
    }

    private void setDate(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    lblDate.setText(formatter.format(currentDate));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setTime(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
                    lblTime.setText(formatter.format(currentTime));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void callPane(String path) throws IOException {
        AnchorPane newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/" + path)));
        centerPane.getChildren().setAll(newPane);
    }

    public void callStock(ActionEvent actionEvent) throws IOException {
        lblTitle.setText("Medicine");
        callPane("transaction/itemForm.fxml");
    }

    public void callPrescription(ActionEvent actionEvent) throws IOException {
        lblTitle.setText("Prescription");
        callPane("prescription/prescriptionForm.fxml");
    }

    public void callLogOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/login/LoginForm.fxml")))));
        stage.setTitle("Login Page");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

}
