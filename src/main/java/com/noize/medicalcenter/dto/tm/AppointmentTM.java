package com.noize.medicalcenter.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class AppointmentTM {
    private String name;
    private String age;
    private String status;
    private String description;
    private Date date;
}
