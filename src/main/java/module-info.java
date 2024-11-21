module com.noize.medicalcenter {
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires com.jfoenix;
    requires net.sf.jasperreports.core;
    requires mysql.connector.j;
    requires com.google.api.client;
    requires jbcrypt;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.media;
    requires javax.mail.api;
    requires resend.java;
    requires org.json;
    requires webcam.capture;


    opens com.noize.medicalcenter.controller to javafx.fxml;
    opens com.noize.medicalcenter.model to javafx.base;
    opens com.noize.medicalcenter.dto to javafx.base;
    opens com.noize.medicalcenter.util.alert to javafx.base;
    opens com.noize.medicalcenter.util to javafx.base;
    opens com.noize.medicalcenter.dto.tm to javafx.base;

    exports com.noize.medicalcenter;
    exports com.noize.medicalcenter.controller;

}