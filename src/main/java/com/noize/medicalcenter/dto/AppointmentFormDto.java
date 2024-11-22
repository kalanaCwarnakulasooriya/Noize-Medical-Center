package com.noize.medicalcenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AppointmentFormDto {
    private int id;
//    private String name;
    private String age;
    private String status;
    private String description;
    private String date;
    private String doctorId;
    private int userId;
}
