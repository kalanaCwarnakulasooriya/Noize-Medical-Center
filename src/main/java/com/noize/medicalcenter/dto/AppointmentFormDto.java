package com.noize.medicalcenter.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AppointmentFormDto {
    private int id;
    private String name;
    private String age;
    private String status;
    private String description;
    private Date date;
    private String doctorId;
    private String userId;
}
