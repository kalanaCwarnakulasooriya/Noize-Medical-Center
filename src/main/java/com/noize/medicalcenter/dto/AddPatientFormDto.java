package com.noize.medicalcenter.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AddPatientFormDto {
    private int id;
    private String name;
    private String address;
    private String contactNumber;
    private String email;
    private LocalDate dob;
    private LocalDate regDate;
    private int genderId;
    private int userId;
    private int prescriptionId;
}
