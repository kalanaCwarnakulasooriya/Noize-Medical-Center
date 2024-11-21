package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.AppointmentFormDto;
import com.noize.medicalcenter.dto.PrescriptionFormDto;
import com.noize.medicalcenter.dto.tm.AppointmentTM;
import com.noize.medicalcenter.dto.tm.PrescriptionTM;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionFormModel {
    public ArrayList<PrescriptionTM> getAllPrescription() throws SQLException {
        String sql = "SELECT * FROM prescription";
        ArrayList<PrescriptionTM> prescription = new ArrayList<>();
        try {
            ResultSet rst = CrudUtil.execute(sql);
            while (rst.next()) {
                PrescriptionTM prescriptionTM = new PrescriptionTM(
                        rst.getString("MedicineDetails"),
                        rst.getString("Dosage"),
                        rst.getString("PrescriptionDate"),
                        rst.getString("UserId"),
                        rst.getString("DoctorId")
//                        rst.getString("AppoId"),
//                        rst.getString("PatientId")
                );
                prescription.add(prescriptionTM);
            }
            return prescription;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isAddPrescription(String date, String medicineDetails, String dosage, String userId, String doctorId) throws SQLException {
        String sql = "INSERT INTO prescription(PrescriptionDate,MedicineDetails,Dosage,UserId,DoctorId) VALUES (?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    sql,
                    date,
                    medicineDetails,
                    dosage,
                    1,
                    doctorId
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isUpdatePrescription(PrescriptionFormDto prescriptionFormDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE prescription SET MedicineDetails = ?, Dosage = ? WHERE PrescriptionDate = ?",
                prescriptionFormDto.getMediDetails(),
                prescriptionFormDto.getDosage(),
                prescriptionFormDto.getDate()
                );
    }

    public boolean isDeletePrescription(String id) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM prescription WHERE PrescriptionDate = ?",
                id
        );
    }
}
