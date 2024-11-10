package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.tm.PatientsDto;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientsFormModel {
    public ArrayList<PatientsDto> getAllPatients() throws SQLException {
        String sql = "SELECT * FROM patient";
        ResultSet rst = CrudUtil.execute(sql);

        ArrayList<PatientsDto> patients = new ArrayList<>();
        while (rst.next()) {
            PatientsDto patientsDto = new PatientsDto(
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
        return patients;
    }

    public ArrayList<PatientsDto> searchPatients(String name) throws SQLException {
        String sql = "select * from patient where Name like ?";
        ResultSet rst = CrudUtil.execute(sql, name+"%");
        ArrayList<PatientsDto> patient = new ArrayList<>();
        while (rst.next()) {
            PatientsDto newPatients = new PatientsDto(
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
}
