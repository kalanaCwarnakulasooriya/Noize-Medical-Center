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
                "UPDATE appointment SET Status = ?, Description = ?, Date = ? WHERE Age = ?",
                appointmentFormDto.getStatus(),
                appointmentFormDto.getDescription(),
                appointmentFormDto.getDate(),
                appointmentFormDto.getAge()
                );
    }

    public Boolean isAddAppointment(String age, String status, String description, String date, String doctorId) throws SQLException {
        String sql = "INSERT INTO appointment(Age,Status,Description,Date,DoctorId,UserId) VALUES (?,?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    sql,
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

    public boolean isDeleteAppointment(String age) throws SQLException {
        return CrudUtil.execute(
                "DELETE FROM appointment WHERE Age = ?",
                age
        );
    }

    public ArrayList<AppointmentTM> searchAppointments(String age) throws SQLException {
        String sql = "select * from appointment where Age like ?;";
        ResultSet rst = CrudUtil.execute(sql, age + "%");
        ArrayList<AppointmentTM> appointments = new ArrayList<>();
        while (rst.next()) {
            AppointmentTM newAppointments = new AppointmentTM(
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

    public AppointmentTM findById(String selectName) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM appointment WHERE AppointmentId = ?",
                selectName
        );

        if (rst.next()) {
            return new AppointmentTM(
                    rst.getString("Age"),
                    rst.getString("Status"),
                    rst.getString("Description"),
                    rst.getString("Date"),
                    rst.getString("DoctorId"),
                    rst.getString("UserId")
            );
        }
        return null;
    }

    public ArrayList<String> getAppointments() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT AppointmentId FROM appointment");
        ArrayList<String> appointments = new ArrayList<>();
        while (rst.next()) {
            appointments.add(rst.getString("AppointmentId"));
        }
        return appointments;
    }
}
