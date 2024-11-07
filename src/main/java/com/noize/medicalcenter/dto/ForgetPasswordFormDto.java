package com.noize.medicalcenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ForgetPasswordFormDto {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int otp;
}
