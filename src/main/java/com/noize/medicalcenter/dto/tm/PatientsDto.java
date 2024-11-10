package com.noize.medicalcenter.dto.tm;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientsDto {
    private String patientsName;
    private String patientsAddress;
    private String patientsContactNumber;
    private String patientsEmail;
    private Date patientsDob;
    private String patientsGender;
    private Date patientsRegDate;
}
