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


    opens com.noize.medicalcenter.controller to javafx.fxml;
    opens com.noize.medicalcenter.model to javafx.base;
    opens com.noize.medicalcenter.dto to javafx.base;
    opens com.noize.medicalcenter.notification to javafx.base;
    opens com.noize.medicalcenter.alert to javafx.base;
    opens com.noize.medicalcenter.email to javafx.base;
    opens com.noize.medicalcenter.util to javafx.base;
    opens com.noize.medicalcenter.dto.tm to javafx.base;

    exports com.noize.medicalcenter;
    exports com.noize.medicalcenter.controller;

}