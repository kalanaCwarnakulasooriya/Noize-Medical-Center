package com.noize.medicalcenter.dto.tm;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DoctorTM {
    private String name;
    private String email;
    private String contactNumber;
    private String address;
}
