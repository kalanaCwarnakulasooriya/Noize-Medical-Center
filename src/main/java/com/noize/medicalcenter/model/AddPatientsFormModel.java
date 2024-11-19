package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.AddPatientFormDto;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class AddPatientsFormModel {
    public int getGenderIdByDescription(String description) throws SQLException {
        String getGender = "SELECT GenderId FROM Gender WHERE Description = ?";
        ResultSet rstGender = CrudUtil.execute(getGender, description);
        try {
            if (rstGender.next()) {
                return rstGender.getInt("GenderId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public ArrayList<String> getAllGenders() throws SQLException {
        ArrayList<String> gender = new ArrayList<>();
        String sqlGender = "SELECT Description FROM gender";
        ResultSet rstGender = CrudUtil.execute(sqlGender);

        while (rstGender.next()) {
            gender.add(rstGender.getString("Description"));
        }
        return gender;
    }

    public boolean savePatient(AddPatientFormDto patientDTO) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO patient VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getEmail(),
                patientDTO.getContactNumber(),
                patientDTO.getAddress(),
                patientDTO.getDob(),
                patientDTO.getGenderId(),
                patientDTO.getRegDate(),
                patientDTO.getUserId(),
                patientDTO.getPrescriptionId()
        );
    }

}
