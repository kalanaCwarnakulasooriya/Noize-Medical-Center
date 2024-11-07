package com.noize.medicalcenter.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginFormDto {
    private String username;
    private String password;
}
