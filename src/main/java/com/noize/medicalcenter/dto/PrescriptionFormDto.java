package com.noize.medicalcenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PrescriptionFormDto {
    private int id;
    private String date;
    private String mediDetails;
    private String dosage;
    private int userId;
    private String doctorId;
}
