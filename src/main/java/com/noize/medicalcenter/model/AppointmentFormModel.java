package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.AppointmentFormDto;
import com.noize.medicalcenter.dto.tm.AppointmentTM;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentFormModel {
    public ArrayList<AppointmentTM> getAllAppointments() throws SQLException {
        String sql = "SELECT * FROM appointment";
        ArrayList<AppointmentTM> appointments = new ArrayList<>();
        try {
            ResultSet rst = CrudUtil.execute(sql);
            while (rst.next()) {
                AppointmentTM appointment = new AppointmentTM(
                        rst.getString("Name"),
                        rst.getString("Age"),
                        rst.getString("Status"),
                        rst.getString("Description"),
                        rst.getString("Date"),
                        rst.getString("DoctorId"),
                        rst.getString("UserId")
                );
                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isUpdateAppointment(AppointmentFormDto appointmentFormDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE appointment SET Age = ?, Status = ?, Description = ?, Date = ? WHERE Name = ?",
                appointmentFormDto.getAge(),
                appointmentFormDto.getStatus(),
                appointmentFormDto.getDescription(),
                appointmentFormDto.getDate(),
                appointmentFormDto.getName()
        );
    }

    public Boolean isAddAppointment(String name, String age, String status, String description, String date, String doctorId) throws SQLException {
        String sql = "INSERT INTO appointment(Name,Age,Status,Description,Date,DoctorId,UserId) VALUES (?,?,?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    sql,
                    name,
                    age,
                    status,
                    description,
                    date,
                    doctorId,
                    1
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isDeleteAppointment(String name) throws SQLException {
        return CrudUtil.execute("DELETE FROM appointment WHERE Name = ?", name);
    }

    public ArrayList<AppointmentTM> searchAppointments(String name) throws SQLException {
        String sql = "select * from appointment where Name like ?;";
        ResultSet rst = CrudUtil.execute(sql, name+"%");
        ArrayList<AppointmentTM> appointments = new ArrayList<>();
        while (rst.next()) {
            AppointmentTM newAppointments = new AppointmentTM(
                    rst.getString("Name"),
                    rst.getString("Age"),
                    rst.getString("Status"),
                    rst.getString("Description"),
                    rst.getString("Date"),
                    rst.getString("DoctorId"),
                    rst.getString("UserId")
            );
            appointments.add(newAppointments);
        }
        return appointments;
    }
}
