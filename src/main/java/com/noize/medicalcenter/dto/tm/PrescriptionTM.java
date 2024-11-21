package com.noize.medicalcenter.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PrescriptionTM {
    private String mediDetails;
    private String dosage;
    private String date;
    private String userId;
    private String doctorId;
//    private String appoId;
//    private String patientId;
}
