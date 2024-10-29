package com.noize.medicalcenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignupFormDto {
    String username;
    String password;
    String address;
    String email;
    String name;
    String contactNumber;
    int role;
}
