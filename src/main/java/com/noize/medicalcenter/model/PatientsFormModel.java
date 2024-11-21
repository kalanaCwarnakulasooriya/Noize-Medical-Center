package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.tm.PatientsTM;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientsFormModel {
    public ArrayList<PatientsTM> getAllPatients() {
        String sql = "SELECT * FROM patient";
        ArrayList<PatientsTM> patients = new ArrayList<>();

        try (ResultSet rst = CrudUtil.execute(sql)) {
            while (rst.next()) {
                PatientsTM patientsDto = new PatientsTM(
                        rst.getString("Name"),
                        rst.getString("Address"),
                        rst.getString("ContactNumber"),
                        rst.getString("Email"),
                        rst.getDate("DOB"),
                        rst.getString("Gender"),
                        rst.getDate("RegistrationDate")
                );
                patients.add(patientsDto);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patients: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }

    public ArrayList<PatientsTM> searchPatients(String name) throws SQLException {
        String sql = "select * from patient where Name like ?";
        ResultSet rst = CrudUtil.execute(sql, name+"%");
        ArrayList<PatientsTM> patient = new ArrayList<>();
        while (rst.next()) {
            PatientsTM newPatients = new PatientsTM(
                    rst.getString("Name"),
                    rst.getString("Address"),
                    rst.getString("ContactNumber"),
                    rst.getString("Email"),
                    rst.getDate("DOB"),
                    rst.getString("Gender"),
                    rst.getDate("RegistrationDate")
            );
            patient.add(newPatients);
        }
        return patient;
    }

    public PatientsTM findById(String selectedContact) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM patient WHERE PatientId = ?", selectedContact);

        if (rst.next()) {
            return new PatientsTM(
                    rst.getString("Name"),
                    rst.getString("Address"),
                    rst.getString("ContactNumber"),
                    rst.getString("Email"),
                    rst.getDate("DOB"),
                    rst.getString("Gender"),
                    rst.getDate("RegistrationDate")
            );
        }
        return null;
    }
    public ArrayList<String> getAllPatientMobile() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT PatientId FROM patient");
        ArrayList<String> patientMobile = new ArrayList<>();
        while (rst.next()) {
            patientMobile.add(rst.getString("PatientId"));
        }
        return patientMobile;
    }

    public boolean deletePatient(String patientName) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM patient WHERE Name = ?",
                patientName
        );
    }

    public boolean updatePatient(PatientsTM patientsTM) throws SQLException {
        return CrudUtil.execute(
                "UPDATE patient SET Address = ?, ContactNumber = ?, Email = ?, DOB = ?, Gender = ?, RegistrationDate = ? WHERE Name = ?",
                patientsTM.getPatientsAddress(),
                patientsTM.getPatientsContactNumber(),
                patientsTM.getPatientsEmail(),
                patientsTM.getPatientsDob(),
                patientsTM.getPatientsGender(),
                patientsTM.getPatientsRegDate(),
                patientsTM.getPatientsName()
                );
    }
}
